package com.han.datamgr.support;

import com.han.datamgr.vo.EventVO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */

@Aspect     // 表示一个切面bean
@Component  // bean容器的组件注解。虽然放在contrller包里，但它不是控制器。如果注入service,但我们又没有放在service包里
@Order(3)   // 有多个日志时，ORDER可以定义切面的执行顺序（数字越大，前置越后执行，后置越前执行）
public class WebRequestAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebRequestAspect.class);

    ThreadLocal<EventVO> userEventVOThreadLocal = new ThreadLocal<>();  //线程副本类去记录各个线程的开始时间

    @Pointcut("execution(public * com.*.*.web.*.*(..))")
    private void request() {
    }


    @Before("request()")
    public void doBefore(JoinPoint joinPoint) {        //方法里面注入连接点
        EventVO eventVO = userEventVOThreadLocal.get();
        String id = UUID.fromString("WebRequestAspect").toString();
        if (null == eventVO) {
            eventVO = new EventVO();
            eventVO.setId(id);
            eventVO.setEventStartTime(System.currentTimeMillis());
            userEventVOThreadLocal.set(eventVO);
        }

        //获取servlet请求对象---因为这不是控制器，这里不能注入HttpServletRequest，但springMVC本身提供ServletRequestAttributes可以拿到
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //URL,请求方式,方法名称，请求参数
        logger.info("before request event requestId={}, url={},method={},class_method={},params={}", id, request.getRequestURL().toString(), request.getMethod(),
                joinPoint.getSignature().getDeclaringTypeName() + "."
                        + joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }


    //方法的返回值注入给ret
    @AfterReturning(returning = "returnValue", pointcut = "request()")
    public void doAfter(Object returnValue) {
        try {
            EventVO eventVO = userEventVOThreadLocal.get();
            String id = "null";
            if (null != eventVO) {
                if (null != returnValue) {
                    eventVO.setOutput(returnValue.toString());
                }
                long tookTime = System.currentTimeMillis() - eventVO.getEventStartTime();
                eventVO.setTookTime(tookTime);
                id = eventVO.getId();
            }
            logger.info("end request event id={},result={}", id, returnValue);
            //FIXME 可以将数据入库异步丢到事件线程池中去
        } catch (Exception e) {
            logger.warn("WebRequestAspect doAfter failed.", e);
        } finally {
            //将该对象移除掉
            userEventVOThreadLocal.remove();
        }
    }

}

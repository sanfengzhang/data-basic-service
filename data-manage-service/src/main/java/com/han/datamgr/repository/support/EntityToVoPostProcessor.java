package com.han.datamgr.repository.support;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

import java.lang.reflect.Method;

/**
 * @author: Hanl
 * @date :2019/10/14
 * @desc:
 */
public class EntityToVoPostProcessor implements RepositoryProxyPostProcessor {

    @Override
    public void postProcess(ProxyFactory proxyFactory, RepositoryInformation repositoryInformation) {

    }

    static enum EntityToVoAdvice implements MethodInterceptor {

        INSTANCE;

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            methodInvocation.proceed();
            return null;
        }
    }
}




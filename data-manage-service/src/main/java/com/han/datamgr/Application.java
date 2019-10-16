package com.han.datamgr;

import com.han.datamgr.repository.support.BaseRepositoryFactoryBean;
import com.han.datamgr.support.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
public class Application {
    public static void main(String[] args) {

        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(Application.class, args);
        SpringContextUtil.setApplicationContext(configurableApplicationContext);
    }
}

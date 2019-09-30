package com.han.datamgr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@SpringBootApplication
@EnableTransactionManagement
public class Application {
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}

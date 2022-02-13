package com.spring.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.spring.test.RedisClientTest;
import com.spring.util.ApplicationContextUtil;

@ComponentScan(basePackages={"com.spring"}) 
@ComponentScan(basePackages={"com.spring.test"}) 
@ComponentScan(basePackages={"com.spring.util"})
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

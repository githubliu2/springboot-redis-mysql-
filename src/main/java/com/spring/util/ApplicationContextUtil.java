package com.spring.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext = null;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	System.out.print(applicationContext);
        if(this.applicationContext == null) {
            this.applicationContext = applicationContext;
        }
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public  <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }
}

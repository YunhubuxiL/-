package com.spring.service;

import com.spring.spring.BeanPostProcessor;
import com.spring.spring.Component;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessorBeforeInitialization(String beanName, Object bean) {
        if(beanName.equals("userService")){
            System.out.println("哈哈");
        }
        return bean;
    }

    @Override
    public Object postProcessorAfterInitialization(String beanName, Object bean) {
        if(beanName.equals("userService")){
            System.out.println("我哈哈哈");
        }
        return bean;
    }
}

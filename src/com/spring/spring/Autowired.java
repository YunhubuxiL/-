package com.spring.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//依赖注入注解
//运行时生效
@Retention(RetentionPolicy.RUNTIME)
//只能写方法
@Target(ElementType.FIELD)
public @interface Autowired {

}

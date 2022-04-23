package com.spring.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//配置文件注解
//运行时生效
@Retention(RetentionPolicy.RUNTIME)
//只能写在类上面
@Target(ElementType.TYPE)
public @interface Component {
    //Bean名字
    String value() default "";
}

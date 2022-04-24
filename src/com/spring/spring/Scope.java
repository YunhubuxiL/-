package com.spring.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
//只能写在类上面
@Target(ElementType.TYPE)
public @interface Scope {
    String value() ;
}

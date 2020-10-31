package com.sty.ne.modularimpl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: tian
 * @UpdateDate: 2020/10/30 9:40 PM
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Parameter {

    //不填写那么的注解值表示该属性名就是key, 填写了就用注解值作为key
    //从getIntent()方法中获取传递参数值
    String name() default "";
}

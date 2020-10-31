package com.sty.ne.modularimpl.api.core;

/**
 * 参数Parameter加载接口
 * @Author: tian
 * @UpdateDate: 2020/10/30 9:33 PM
 */
public interface ParameterLoad {

    /**
     * 目标对象.属性名 = target.getIntent().属性类型("注解值or属性名")；完成赋值
     * @param target 目标对象，如：MainActivity(中的某些属性)
     */
    void loadParameter(Object target);
}

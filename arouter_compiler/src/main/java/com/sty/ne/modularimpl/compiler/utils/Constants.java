package com.sty.ne.modularimpl.compiler.utils;

/**
 * 常量
 * @Author: tian
 * @UpdateDate: 2020/10/14 10:20 PM
 */
public class Constants {
    //注解处理器支持的注解类型
    public static final String AROUTER_ANNOTATION_TYPES = "com.sty.ne.modularimpl.annotation.ARouter";

    //每个子模块的模块名
    public static final String MODULE_NAME = "moduleName";
    //用于存放APT生成的类文件
    public static final String APT_PACKAGE = "packageNameForAPT";

    //String全类名
    public static final String STRING = "java.lang.String";
    //Activity的全类名
    public static final String ACTIVITY = "android.app.Activity";
}

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
    //包名前缀封装
    public static final String BASE_PACKAGE = "com.sty.ne.modularimpl.api";
    //路由Group加载接口
    public static final String AROUTER_GROUP = BASE_PACKAGE + ".core.ARouterLoadGroup";
    //路由组Group对应的详细PATH加载接口
    public static final String AROUTER_PATH = BASE_PACKAGE + ".core.ARouterLoadPath";

    //路由组Group对应的详细PATH，方法名
    public static final String PATH_METHOD_NAME = "loadPath";
    //路由组Group对应的详细Path，参数名
    public static final String PATH_PARAMETER_NAME = "pathMap";
    //路由Group，参数名
    public static final String GROUP_PARAMETER_NAME = "groupMap";
    //路由Group，方法名
    public static final String GROUP_METHOD_NAME = "loadGroup";

    //APT生成的路由组Group对应的详细Path类文件名
    public static final String PATH_FILE_NAME = "ARouter$Path$$";
    //APT生成的路由组Group类文件名
    public static final String GROUP_FILE_NAME = "ARouter$$Group$$";
}

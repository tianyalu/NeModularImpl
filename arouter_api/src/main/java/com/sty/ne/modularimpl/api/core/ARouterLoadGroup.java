package com.sty.ne.modularimpl.api.core;

import java.util.Map;

/**
 * 路由组Group对外提供加载数据接口
 * @Author: tian
 * @UpdateDate: 2020/10/14 9:53 PM
 */
public interface ARouterLoadGroup {

    /**
     * 加载路由组Group数据
     * 比如："app",ARouter$$Path$$app.class(实现了ARouterLoadPath接口)
     * @return key:"app", value:"app"分组对应的路由详细对象类
     */
    Map<String, Class<? extends ARouterLoadPath>> loadGroup();
}

package com.sty.ne.modularimpl.test;

import com.sty.ne.modularimpl.api.ARouterLoadGroup;
import com.sty.ne.modularimpl.api.ARouterLoadPath;

import java.util.HashMap;
import java.util.Map;

/**
 * 模拟ARouter路由器的组文件
 * @Author: tian
 * @UpdateDate: 2020/10/14 10:29 PM
 */
public class ARouter$$Group$$app implements ARouterLoadGroup {
    @Override
    public Map<String, Class<? extends ARouterLoadPath>> loadGroup() {
        Map<String, Class<? extends ARouterLoadPath>> groupMap = new HashMap<>();
        groupMap.put("app", ARouter$$Path$$app.class);
        return groupMap;
    }
}
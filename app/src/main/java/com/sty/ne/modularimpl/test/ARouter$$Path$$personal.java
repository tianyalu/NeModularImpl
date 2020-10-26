package com.sty.ne.modularimpl.test;

import com.sty.ne.modularimpl.annotation.model.RouterBean;
import com.sty.ne.modularimpl.api.core.ARouterLoadPath;
import com.sty.ne.modularimpl.order.Order_MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 模拟ARouter路由器的组文件对应的路径
 * @Author: tian
 * @UpdateDate: 2020/10/14 10:29 PM
 */
public class ARouter$$Path$$personal implements ARouterLoadPath {
    @Override
    public Map<String, RouterBean> loadPath() {
        Map<String, RouterBean> pathMap = new HashMap<>();
        pathMap.put("/personal/Personal_MainActivity",
                RouterBean.create(RouterBean.Type.ACTIVITY,
                Order_MainActivity.class,
                "/personal/Personal_MainActivity",
                "personal"));
        return pathMap;
    }
}

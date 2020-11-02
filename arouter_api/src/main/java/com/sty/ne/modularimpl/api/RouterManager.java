package com.sty.ne.modularimpl.api;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import com.sty.ne.modularimpl.annotation.model.RouterBean;
import com.sty.ne.modularimpl.api.core.ARouterLoadGroup;
import com.sty.ne.modularimpl.api.core.ARouterLoadPath;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 路由表管理 --> 两个模块之间没有任何依赖，但是可以通讯，可以传参
 * 该类用来寻找我们自动生成的类
 * @Author: tian
 * @UpdateDate: 2020/11/2 9:32 PM
 */
public class RouterManager {
    //寻找 group  path
    private String group; //路由的组名 app order personal
    private String path; //路由的路径 order/Order_MainActivity

    //单例模式
    private static volatile RouterManager instance;

    //提高性能，缓存 LRU
    private LruCache<String, ARouterLoadGroup> groupLruCache;
    private LruCache<String, ARouterLoadPath> pathLruCache;
    //为了拼接，例如：ARouter$$Group$$order
    private static final String FILE_GROUP_NAME = "ARouter$$Group$$";

    private RouterManager() {
        groupLruCache = new LruCache<>(100);
        pathLruCache = new LruCache<>(100);
    }

    public static RouterManager getInstance() {
        if(instance == null) {
            synchronized (RouterManager.class) {
                if(null == instance) {
                    instance = new RouterManager();
                }
            }
        }
        return instance;
    }

    /**
     * @param path 例如：/order/Order_MainActivity
     * @return
     */
    public BundleManager build(String path) {
        if(TextUtils.isEmpty(path) || !path.startsWith("/")) {
            throw new IllegalArgumentException("不按常理出牌, 请检查path路径，正确写法，如：/order/Order_MainActivity");
        }
        //自己添加代码健壮性检查

        if(path.lastIndexOf("/") == 0) { //只写了一个/
            throw new IllegalArgumentException("不按常理出牌, 请检查path路径，正确写法，如：/order/Order_MainActivity");
        }

        //用户按照自己的规范来
        //截取组名 /order/Order_MainActivity  finalGroup = order
        String finalGroup = path.substring(1, path.indexOf("/", 1)); //finalGroup = order
        if(TextUtils.isEmpty(finalGroup)) {
            throw new IllegalArgumentException("不按常理出牌, 请检查path路径，正确写法，如：/order/Order_MainActivity");
        }

        //用户传递的，完全合格
        this.path = path;
        this.group = finalGroup;

        return new BundleManager();
    }

    /**
     * 真正的 模块一和模块二交互和传参
     * @param context
     * @param bundleManager
     * @return
     */
    private Object navigation(Context context, BundleManager bundleManager) {
        //寻找 例如： ARouter$$Group$$order
        String groupClassName = context.getPackageName() + ".apt." + FILE_GROUP_NAME + group;
        Log.d("sty", "navigation: groupClassName: " + groupClassName);

        try {
            //读取路由组Group类文件
            ARouterLoadGroup loadGroup = groupLruCache.get(group);
            if(null == loadGroup) { //缓存里面没有东西
                //加载APT路由组Group类文件，例如：ARouter$$Group$$order
                Class<?> aClass = Class.forName(groupClassName);
                //初始化类文件
                loadGroup = (ARouterLoadGroup) aClass.newInstance();
                //保存到缓存中
                groupLruCache.put(group, loadGroup);
            }

            if(loadGroup == null || loadGroup.loadGroup().isEmpty()) {
                throw new RuntimeException("路由表Group报废了...");
            }

            //如果能够走到这里来 表明 Map<String, Class<? extends ARouterLoadPath>> map 可以拿到
            //处理ARouter$$Path$$order，读取路由path文件
            ARouterLoadPath loadPath = pathLruCache.get(path);
            if(null == loadPath) {
                Class<? extends ARouterLoadPath> clazz = loadGroup.loadGroup().get(group); //ARouter$$Path$$app.class
                loadPath = clazz.newInstance();
                //保存到缓存中
                pathLruCache.put(path, loadPath);
            }

            if(loadPath == null || loadPath.loadPath().isEmpty()) {
                throw new RuntimeException("路由表Path报废了...");
            }

            //执行 ARouter$$Path$$order 的 loadPath --> Map<String, RouterBean>
            RouterBean routerBean = loadPath.loadPath().get(path);
            if(routerBean != null) {
                switch (routerBean.getType()) {
                    case ACTIVITY:
                        Intent intent = new Intent(context, routerBean.getClazz()); //Order_mainActivity.class
                        intent.putExtras(bundleManager.getBundle());
                        context.startActivity(intent);
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    //参数配置  单一职责
    public static class BundleManager{
        private Bundle bundle = new Bundle();

        //对外界提供，可以携带参数的方法
        public BundleManager withString(@NonNull String key, @Nullable String value) {
            bundle.putString(key, value);
            return this;
        }

        public BundleManager withBoolean(@NonNull String key, @Nullable boolean value) {
            bundle.putBoolean(key, value);
            return this;
        }

        public BundleManager withInt(@NonNull String key, @Nullable int value) {
            bundle.putInt(key, value);
            return this;
        }

        //可以继续扩充 ...

        public BundleManager withBundle(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        //导航 模块一 和 模块二 交互
        public Object navigation(Context context) {
            //不应该在这里直接做--> 单一职责
            return RouterManager.getInstance().navigation(context, this);
        }

        public Bundle getBundle() {
            return bundle;
        }
    }

}

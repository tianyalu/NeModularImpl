# 组件化路由架构设计与实现

[TOC]

## 一、组件化路由设计

### 1.1 原理

####1.1,1 设计结构

整体结构如下图所示：

![image](https://github.com/tianyalu/NeModularImpl/raw/master/show/modular_router_structure_design.png)

* 为什么需要组名？

分组加载，按需加载，减少不必要的模块加载，可以减少内存开销。

* 生成这些文件干嘛用？

首先通过组文件记录根据组名找到`Arouter$$Path$$app.class`文件（路径文件记录），然后根据该文件通过路径名称找到相应的类的`Class`。

#### 1.1.2 `RouterBean`对象

```java
public enum Type {
  ACTIVITY
}
//枚举类型：Activity
private Type type;
//类节点
private Element element;
//注解使用的类对象
private Class<?> clazz;
//路由地址
private String path;
//路由组
private String group;
```

`Element`类节点为什么也要存起来？

> 1. 因为将来我们要在注解处理器中循环拿到每个类节点，方便赋值和调用；
> 2. `Element`是`javax.lang`包下的，不属于`Android Library`。

#### 1.1.3 `ARouterLoadGroup`接口

```java
/**
 * 路由Group加载数据接口
 */
public interface ARouterLoadGroup {
  /**
   * 加载路由组Group数据
   * 比如："app",ARouter$$Path$$app.class(实现了ARouterLoadPath接口)
   * @return key:"app", value:"app"分组对应的路由详细对象类
   */
    Map<String, Class<? extends ARouterLoadPath>> loadGroup();
}
```

` Map<String, Class<? extends ARouterLoadPath>>`:

> 1. 需要生成路由路径文件后，才能生成路由组文件；
> 2. 接口方式容易扩展、通用。

#### 1.1.4 `ARouterLoadPath`接口

```java
/**
 * 路由组Group对应的详细Path加载数据接口
 * 比如：app分组对应有哪些类需要加载
 */
public interface ARouterLoadPath {
  /**
   * 加载路由组Group中的Path详细数据
   * 比如："app"分组下有这些信息：
   * @return key:"/app/MainActivity", value:MainActivity信息封装到RouterBean对象中
   */
    Map<String, RouterBean> loadPath();
}
```

` Map<String, RouterPath>`:

> 1. `OOP`思想，让单纯的`targetClass`变成更灵活的`RouterBean`对象；
> 2. 接口方式容易扩展、通用。

#### 1.1.5 `javapoet`生成的`ARouter$$Path$$order`类
```java
public class ARouter$Path$$order implements ARouterLoadPath {
  @Override
  public Map<String, RouterBean> loadPath() {
    Map<String, RouterBean> pathMap = new HashMap<>();
    pathMap.put("/order/Order2Activity", RouterBean.create(RouterBean.Type.ACTIVITY, Order2Activity.class, "/order/Order2Activity", "order"));
    pathMap.put("/order/Order_MainActivity", RouterBean.create(RouterBean.Type.ACTIVITY, Order_MainActivity.class, "/order/Order_MainActivity", "order"));
    return pathMap;
  }
}
```

#### 1.1.6 `javapoet`生成的`ARouter$$Group$$order`类

```java
public class ARouter$$Group$$order implements ARouterLoadGroup {
  @Override
  public Map<String, Class<? extends ARouterLoadPath>> loadGroup() {
    Map<String, Class<? extends ARouterLoadPath>> groupMap = new HashMap<>();
    groupMap.put("order", ARouter$Path$$order.class);
    return groupMap;
  }
}
```

#### 1.1.7 使用

```java
    private void onBtnOrderClicked() {
        //最终集成化模式，所有子模块APT生成的类文件都会打包到apk中
        ARouterLoadGroup loadGroup = new ARouter$$Group$$order();
        Map<String, Class<? extends ARouterLoadPath>> groupMap = loadGroup.loadGroup();
        // app - order
        Class<? extends ARouterLoadPath> clazz = groupMap.get("order");

        //类加载技术
        ARouterLoadPath path = null;
        try {
            path = clazz.newInstance();
            Map<String, RouterBean> pathMap = path.loadPath();
            //获取/order/Order_MainActivity
            RouterBean routerBean = pathMap.get("/order/Order_MainActivity");
            if(routerBean != null) {
                Intent intent = new Intent(this, routerBean.getClazz());
                intent.putExtra("name", "sty");
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

### 1.2 实现原理小结

路由架构的目的是为了找到目标`Activity`的`Class`，这里通过注解处理器自定义了`ARouter`注解，作用在`Activity`之上；编译时通过注解处理器解析每个注解，然后利用`javapoet`技术生成`ARouter$$Path$$order`类，该类实现`ARouterLoadPath`接口，维护了**路径-->目标`Activity`的`Class`的映射**；然后对每个`ARouter$Path$$order`类利用`javapoet`生成`ARouter$$Group$$order`类，该类实现了`ARouterLoadGroup`接口，维护了**组名-->`ARouter$$Path$$order`类的映射**。使用时根据组名找到`ARouter$$Path$$order`类，然后通过该类利用路径找到对应的`Activity`的`Class`，最终实现路由跳转。
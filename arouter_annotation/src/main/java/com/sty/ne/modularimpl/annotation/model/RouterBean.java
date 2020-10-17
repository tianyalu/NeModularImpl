package com.sty.ne.modularimpl.annotation.model;

import javax.lang.model.element.Element;

/**
 * PathBean对象的升级版
 * @Author: tian
 * @UpdateDate: 2020/10/14 9:58 PM
 */
public class RouterBean {

    public enum Type {
        ACTIVITY
    }
    //枚举类型：Activity
    private Type type;
    //类节点
    private Element element;
    //被@ARouter注解的类对象
    private Class<?> clazz;
    //路由地址
    private String path;
    //路由组
    private String group;

    private RouterBean(Builder builder) {
        this.element = builder.element;
        this.path = builder.path;
        this.group = builder.group;
    }

    private RouterBean(Type type, Class<?> clazz, String path, String group) {
        this.type = type;
        this.clazz  = clazz;
        this.path = path;
        this.group = group;
    }

    //对外提供一种简单的实例化方法
    public static RouterBean create(Type type, Class<?> clazz, String path, String group) {
        return new RouterBean(type, clazz, path, group);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Type getType() {
        return type;
    }

    public Element getElement() {
        return element;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getPath() {
        return path;
    }

    public String getGroup() {
        return group;
    }

    public static final class Builder {
        //类节点
        private Element element;
        //路由地址
        private String path;
        //路由组
        private String group;

        public Builder setElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        //最后的build或create方法，往往是做参数的校验或者初始化工作
        public RouterBean build() {
            if(path == null || path.length() == 0) {
                throw new IllegalArgumentException("path必填项为空，如：/app/MainActivity");
            }
            return new RouterBean(this);
        }
    }

    @Override
    public String toString() {
        return "RouterBean{" +
                "type=" + type +
                ", element=" + element +
                ", clazz=" + clazz +
                ", path='" + path + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}

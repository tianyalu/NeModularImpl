package com.sty.ne.modularimpl.compiler;


import com.google.auto.service.AutoService;
import com.sty.ne.modularimpl.annotation.ARouter;
import com.sty.ne.modularimpl.annotation.model.RouterBean;
import com.sty.ne.modularimpl.compiler.utils.Constants;
import com.sty.ne.modularimpl.compiler.utils.EmptyUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * @Author: tian
 * @UpdateDate: 2020/10/12 9:13 PM
 */
//通过AutoService来自动生成注解处理器，用来做注册，类似在Manifest中注册Activity
//build/classes/java/main/META-INF/services/javax.annotation.processing.Processor
@AutoService(Processor.class)
//该注解处理器需要处理哪一种注解的类型
@SupportedAnnotationTypes(Constants.AROUTER_ANNOTATION_TYPES)
//需要用什么样的JDK版本来编译，来进行文件的生成
@SupportedSourceVersion(SourceVersion.RELEASE_7)
//注解处理器能够接受的参数  在app 的build.gradle文件中配置
@SupportedOptions({Constants.MODULE_NAME, Constants.APT_PACKAGE})
public class ARouterProcessor extends AbstractProcessor {
    //操作Element工具类
    private Elements elementUtils;

    //type(类信息) 工具类
    private Types typeUtils;

    //用来输出警告、错误等日志
    private Messager messager;

    //文件生成器,类/资源，Filer用来创建新的类文件，class文件以及辅助文件
    private Filer filer;

    //子模块名，如：app/order/personal，需要拼接类名时用到（必传）ARouter$$Group$$order
    private String moduleName;

    //包名，用于存放APT生成的类文件
    private String packageNameForAPT;

    //临时map存储，用来存放路由组Group对应的详细Path类对象，生成路由路径类文件时遍历
    //key:组名"app"  value:"app"组的路由路径“ARouter$$Path$$app.class”
    private Map<String, List<RouterBean>> tempPathMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();

        //通过ProcessingEnvironment去获取对应的参数
        Map<String, String> options = processingEnvironment.getOptions();
        if(!EmptyUtils.isEmpty(options)) {
            moduleName = options.get(Constants.MODULE_NAME);
            packageNameForAPT = options.get(Constants.APT_PACKAGE);
            //不能像Android中Log.e的写法-->会报错
            //messager.printMessage(Diagnostic.Kind.ERROR, content);
            messager.printMessage(Diagnostic.Kind.NOTE, "moduleName >>> " + moduleName);
            messager.printMessage(Diagnostic.Kind.NOTE, "packageName >>> " + packageNameForAPT);
        }

        //必传参数判空（乱码问题：添加JAVA控制台输出中文乱码）
        if(EmptyUtils.isEmpty(moduleName) || EmptyUtils.isEmpty(packageNameForAPT)) {
            throw new RuntimeException("注解处理器需要的参数moduleName或者packageName为空，请在对应build.gradle配置参数");
        }
    }

    /**
     * 相当于main函数，开始处理注解
     * 注解处理器的核心方法，处理具体的注解，生成Java文件
     * @param set 使用了支持处理注解的节点集合（类上面写了注解）
     * @param roundEnvironment 当前或是之前的运行环境，可以通过该对象查找找到的注解
     * @return true 表示后续处理器不会再处理（已经处理完成）
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if(set.isEmpty()) {
            return false;
        }

        //获取项目中所有使用了ARouter注解的节点
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ARouter.class);
        if(!EmptyUtils.isEmpty(elements)) {
            //解析元素
            parseElements(elements);
        }
        //遍历所有的类节点
        for (Element element : elements) {
            //类节点之上就是包节点
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            //获取简单类名
            String className = element.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE, "被注解的类有：" + className);
            //最终我们想要生成的类文件，如：MainActivity$$ARouter
            String finalClassName = className + "$$ARouter";

            try {
                JavaFileObject sourceFile = filer.createSourceFile(packageName + "."
                        + finalClassName);
                Writer writer = sourceFile.openWriter();
                //设置包名
                writer.write("package " + packageName + ";\n");
                writer.write("public class " + finalClassName + " {\n");
                writer.write("public static Class<?> findTargetClass(String path) {\n");
                //获取类之上@ARouter注解的path值
                ARouter aRouter = element.getAnnotation(ARouter.class);
                writer.write("if (path.equalsIgnoreCase(\"" + aRouter.path() + "\")) {\n");
                writer.write("return " + className + ".class;\n}\n");
                writer.write("return null;\n");
                writer.write("}\n}");

                //非常重要
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void parseElements(Set<? extends Element> elements) {
        //通过Element工具类获取Activity类型
        TypeElement activityType = elementUtils.getTypeElement(Constants.ACTIVITY);
        //显示类信息
        TypeMirror activityMirror = activityType.asType();

        for (Element element : elements) {
            //获取每个元素的类信息
            TypeMirror elementMirror = element.asType();
            messager.printMessage(Diagnostic.Kind.NOTE, "遍历的元素信息为： " + elementMirror.toString());

            //获取每个类上的@ARouter注解对应的path值
            ARouter aRouter = element.getAnnotation(ARouter.class);
            //路由详细信息，封装到实体类
            RouterBean bean = new RouterBean.Builder()
                    .setGroup(aRouter.group())
                    .setPath(aRouter.path())
                    .setElement(element)
                    .build();
            //高级判断，@ARouter注解仅仅是作用在类之上，并且是规定的Activity
            if(typeUtils.isSubtype(elementMirror, activityMirror)) {
                bean.setType(RouterBean.Type.ACTIVITY);
            }else {
                throw new RuntimeException("@ARouter注解目前仅限用于Activity之上");
            }

            //复制临时的map存储以上信息，用来遍历时生成代码
            valueOfPathMap(bean);
        }

        //1.生成路由的详细Path类文件，如：ARouter$$Path$$app


        //2.生成路由组Group类文件（没有Path类文件的话是找不到的），如：ARouter$$Group$$app
    }

    private void valueOfPathMap(RouterBean bean) {
        messager.printMessage(Diagnostic.Kind.NOTE, "RouterBean >> " + bean.toString());
        if(checkRouterPath(bean)) {
            //开始赋值
            List<RouterBean> routerBeans = tempPathMap.get(bean.getGroup());
            //如果从Map看中找不到key
            if (EmptyUtils.isEmpty(routerBeans)) {
                routerBeans = new ArrayList<>();
                routerBeans.add(bean);
                tempPathMap.put(bean.getGroup(), routerBeans);
            } else { //找到了key，直接加入临时集合
                for (RouterBean routerBean : routerBeans) {
                    if (!bean.getPath().equalsIgnoreCase(routerBean.getPath())) {
                        routerBeans.add(bean); //todo ????
                    }
                }
            }
        }else {
            messager.printMessage(Diagnostic.Kind.NOTE, "@ARouter注解未按规范，如：/app/MainActivity");
        }
    }

    /**
     * 校验@ARouter注解的值，如果group未填写就从必填项path中截取数据
     * @param bean 路由详细信息，最终实体封装类
     * @return
     */
    private boolean checkRouterPath(RouterBean bean) {
        String group = bean.getGroup();
        String path = bean.getPath();

        //@ARouter注解的path值，必须要以 / 开头（模仿阿里ARouter路由架构）
        if(EmptyUtils.isEmpty(path) || !path.startsWith("/")) {
            messager.printMessage(Diagnostic.Kind.NOTE, "@ARouter注解未按照规范，如：/app/MainActivity");
            return false;
        }

        // 比如开发者代码为：path = "/MainActivity"
        if(path.lastIndexOf("/") == 0) {
            messager.printMessage(Diagnostic.Kind.NOTE, "@ARouter注解未按照规范，如：/app/MainActivity");
            return false;
        }

        //从第一个 / 到第二个 / 中间截取组名
        String finalGroup = path.substring(1, path.indexOf("/", 1)); //返回从 fromIndex 位置开始查找指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1
        //比如开发者代码为：path = "/MainActivity/MainActivity/MainActivity"
        if(finalGroup.contains("/")) {
            messager.printMessage(Diagnostic.Kind.NOTE, "@ARouter注解未按照规范，如：/app/MainActivity");
            return false;
        }

        //@ARouter注解中有group赋值
        if(!EmptyUtils.isEmpty(group) && !group.equals(moduleName)) {
            messager.printMessage(Diagnostic.Kind.NOTE, "@ARouter注解未中的group值必须和当前子模块名相同");
            return false;
        }else {
            bean.setGroup(finalGroup);
        }


        return true;
    }
}

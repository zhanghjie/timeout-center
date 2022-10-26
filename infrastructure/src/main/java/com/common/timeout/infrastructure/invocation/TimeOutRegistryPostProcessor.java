package com.common.timeout.infrastructure.invocation;

import com.common.timeout.infrastructure.invocation.cglib.PersistenceServiceCallbackFilter;
import com.common.timeout.infrastructure.invocation.cglib.TimeoutCenterLogMethodInterceptor;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Timer
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/10/21 14:26
 */
@Component
public class TimeOutRegistryPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Set<Class<?>> classSet = getAnnotationClasses("com.common.timeout");
        Map<String, Class> classNameMap = classSet.stream().collect(Collectors.toMap(Class::getName, Function.identity()));
        if (Objects.nonNull(classNameMap.get(bean.getClass().getName()))) {
            // 2. 根据目标对象生成代理对象
            TimeoutCenterLogMethodInterceptor proxy = new TimeoutCenterLogMethodInterceptor(bean.getClass());
            // 获取 CGLIB 代理类
            return proxy.getProxy();
        }
        return bean;
    }

    private Set<Class<?>> getClasses(String packageName) {
        // 第一个class类的集合
        //List<Class<?>> classes = new ArrayList<Class<?>>();
        Set<Class<?>> classes = new HashSet<>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                Set<String> classNames = null;
                String protocol = url.getProtocol();
                //代码在构建包内则都在根目录下 在其它包都是jar内
                if ("file".equals(protocol)) {
                    classNames = getClassNameFromDir(url.getPath(), packageName);
                    for (String path : classNames) {
                        if (path.endsWith(".class")) {
                            continue;
                        }
                        classes.add(Class.forName(path));
                    }
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return classes;
    }

    public void addClass(Set<Class<?>> classes, String filePath, String packageName) throws Exception {
        File[] files = new File(filePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        assert files != null;
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String classsName = fileName.substring(0, fileName.lastIndexOf("."));
                if (!packageName.isEmpty()) {
                    classsName = packageName + "." + classsName;
                }
                doAddClass(classes, classsName);
            }

        }
    }

    public void doAddClass(Set<Class<?>> classes, final String className) throws Exception {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return super.loadClass(name);
            }
        };
        classes.add(classLoader.loadClass(className));
    }


    public <A extends Annotation> Set<Class<?>> getAnnotationClasses(String packageName) {
        //找用了annotationClass注解的类
        Set<Class<?>> controllers = new HashSet<>();
        Set<Class<?>> clsList = getClasses(packageName);
        if (clsList != null && clsList.size() > 0) {
            for (Class<?> cls : clsList) {
                if (cls.getAnnotation(Service.class) != null) {
                    controllers.add(cls);
                }
            }
        }
        return controllers;
    }

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath    文件路径
     * @param packageName 类名集合
     * @return 类的完整名称
     */
    private static Set<String> getClassNameFromDir(String filePath, String packageName) {
        Set<String> className = new HashSet<>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        for (File childFile : files) {
            if (childFile.isDirectory()) {
                className.addAll(getClassNameFromDir(childFile.getPath(), packageName + "." + childFile.getName()));
            } else {
                String fileName = childFile.getName();
                if (fileName.endsWith(".class") && !fileName.contains("$")) {
                    className.add(packageName + "." + fileName.replace(".class", ""));
                }
            }
        }
        return className;
    }

}

package com.spring.spring;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

public class ApplicationContext {
    private Class configClass;

    public ApplicationContext(Class configClass) {
        this.configClass = configClass;
        //查看有没有ComponentScan注解
        if(configClass.isAnnotationPresent(ComponentScan.class)){
            //如果有获取改直接
            ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            //获取路径
            String path = componentScan.value();
            path = path.replace(".","/");
            //去扫描.class 文件
            ClassLoader classLoader =  ApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());
            System.out.println(file);
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for(File f:files){
                    String fileName = f.getAbsolutePath();
                    System.out.println(fileName);
                    if(fileName.endsWith(".class")){
                       String className = fileName.substring(fileName.indexOf("com"),fileName.indexOf(".class"));
                        className = className.replace("\\",".");
                        System.out.println(className);
                        try {
                            Class<?> aClass = classLoader.loadClass(className);

                            if(aClass.isAnnotationPresent(Component.class));{

                            }
                        }catch (ClassNotFoundException e){
                            e.printStackTrace();
                        }


                    }
                }
            }
        }
    }
    public Object getBean(String beanName){
        return null;
    }
}

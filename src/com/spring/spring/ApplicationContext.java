package com.spring.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    private Class configClass;
    private ConcurrentHashMap<String,BeanDefinition> concurrentHashMap = new ConcurrentHashMap<>();
    //单例池
    private ConcurrentHashMap<String,Object> singletonObjects = new ConcurrentHashMap<>();
    //集合
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();
    public ApplicationContext(Class configClass) {
        this.configClass = configClass;
        //查看有没有ComponentScan注解 获取bean对象放到concurrentHashMap里面去
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
            //查看路径是否正确
            if(file.isDirectory()){
                File[] files = file.listFiles();
                //获取文件下的文件
                for(File f:files){
                    String fileName = f.getAbsolutePath();
                    System.out.println(fileName);
                    if(fileName.endsWith(".class")){
                       String className = fileName.substring(fileName.indexOf("com"),fileName.indexOf(".class"));
                        className = className.replace("\\",".");
                        System.out.println(className);
                        try {
                            //得到类名
                            Class<?> aClass = classLoader.loadClass(className);

                            //是否存在注解,判断是否是Bean对象
                            if(aClass.isAnnotationPresent(Component.class)){
                                //判断是否继承了BeanPostProcessor接口
                                if(BeanPostProcessor.class.isAssignableFrom(aClass)){
                                    BeanPostProcessor newInstance = (BeanPostProcessor) aClass.newInstance();
                                    beanPostProcessorList.add(newInstance);
                                }

                                Component component = aClass.getAnnotation(Component.class);
                                String beanName = component.value();
                                //如果beanName为空
                                if(beanName.equals("")){
                                    beanName = Introspector.decapitalize(aClass.getSimpleName());
                                }
                                BeanDefinition beanDefinition = new BeanDefinition();
                                //判断是否为单例对象
                                if(aClass.isAnnotationPresent(Scope.class)){
                                    Scope scope = aClass.getAnnotation(Scope.class);
                                    beanDefinition.setScope(scope.value());
                                }else{
                                    //默认是单例对象
                                    beanDefinition.setScope("singleton");
                                }
                                beanDefinition.setType(aClass);
                                concurrentHashMap.put(beanName,beanDefinition);
                            }
                        }catch (ClassNotFoundException e){
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        //创建单例bean对象 遍历concurrentHashMap将所有的单例bean放到singletonObjects里面去
        for(String beanName:concurrentHashMap.keySet()){
            BeanDefinition beanDefinition = concurrentHashMap.get(beanName);
            if(beanDefinition.getScope().equals("singleton")){
                Object bean = createBean(beanName,beanDefinition);
                //放到单例池中去
                singletonObjects.put(beanName,bean);
            }
        }
    }
    //创建bean对象方法
    private Object createBean(String beanName,BeanDefinition beanDefinition){
        Class aClass = beanDefinition.getType();
        try {
            //获取对象
            Object instance = aClass.getConstructor().newInstance();

            //对 对象中的方法进行依赖注入
            for (Field field : aClass.getDeclaredFields()) {
                if(field.isAnnotationPresent(Autowired.class)){
                    field.setAccessible(true);
                    //去getBean方法中去找这个值
                    field.set(instance,getBean(field.getName()));
                }
            }
            //回调
            if(instance instanceof  BeanNameAware){
                ((BeanNameAware) instance).setBeanName(beanName);
            }
            //初始化前的方法
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                beanPostProcessor.postProcessorBeforeInitialization(beanName,instance);
            }

            //初始化
            if(instance instanceof  InitializingBean){
                ((InitializingBean) instance).afterPropertiesSet();
            }

            //初始化后 的方法
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                beanPostProcessor.postProcessorAfterInitialization(beanName,instance);
            }


            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
    //获取bean 如果是单例直接去singletonObjects里面寻找如果是
    public Object getBean(String beanName){
        //判断对象是单例还是多例对象
        BeanDefinition beanDefinition = concurrentHashMap.get(beanName);
        if(beanDefinition==null){
            throw  new NullPointerException();
        }else{
            String scope = beanDefinition.getScope();
            if(scope.equals("singleton")){
                //单例情况
                Object bean = singletonObjects.get(beanName);
                if(bean==null){
                    Object bean1 = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName,bean1);
                }
                return bean;
            }else{
                //多例情况
                return createBean(beanName,beanDefinition);
            }
        }

    }
}

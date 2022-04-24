package com.spring.service;

import com.spring.spring.*;

@Component("userService")
@Scope("hahaha")
public class UserService implements BeanNameAware, InitializingBean {
    @Autowired
    private AdminService adminService;

    private String beanName;
    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
    public void test(){
        System.out.println(adminService);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("12312312312312");
    }
}

package com.reflex.test.springContent;

public class Context {
    public static Object getBean(String beanid) {
        return ClassPathXmlApplicationContext.getInstance().getBean(beanid);
    }
}

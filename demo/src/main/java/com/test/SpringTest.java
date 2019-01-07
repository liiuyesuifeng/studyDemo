package com.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;

public class SpringTest {
    private static ApplicationContext context;

    @Test
    public void test1() {
        context.getBean("aaa");
    }
}

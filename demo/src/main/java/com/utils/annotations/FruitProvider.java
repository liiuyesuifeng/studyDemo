package com.utils.annotations;

import java.lang.annotation.*;

@Target(ElementType.FIELD)//表示注解作用范围
@Retention(RetentionPolicy.RUNTIME)//表示注解保留范围
@Documented
public @interface FruitProvider {
    public int id() default -1;

    public String name() default "";

    public String address() default "";
}

package com.utils.annotations;

import java.lang.annotation.*;

@Target(ElementType.FIELD)//表示注解作用范围
@Retention(RetentionPolicy.RUNTIME)//表示注解保留范围
@Documented
public @interface FruitColor {
    /**
     * 水果的颜色
     */
    public enum Color {
        BLUE, RED, GREEN
    }

    Color fruitColor() default Color.GREEN;
}

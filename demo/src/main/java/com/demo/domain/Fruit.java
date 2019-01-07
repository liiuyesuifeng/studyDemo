package com.demo.domain;

import com.utils.annotations.FruitColor;
import com.utils.annotations.FruitName;
import com.utils.annotations.FruitProvider;

public class Fruit {
    @FruitName("苹果")
    private int name;
    @FruitColor(fruitColor = FruitColor.Color.BLUE)
    private String color;

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "name:" + name + "colro" + color;
    }
}

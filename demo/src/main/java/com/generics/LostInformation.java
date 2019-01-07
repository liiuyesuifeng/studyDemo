package com.generics;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utils.PrintUtil;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class Forb {
}

class Fnorkle {
}

class Quark<Q> {

}

class Particle<POSITION, MOMENTUM> {
}

public class LostInformation {
    @Test
    public void test() {
        String a = new String("bb");
        List<Forb> list = Lists.newArrayList();
        Map<Forb, Fnorkle> map = Maps.newHashMap();
        Quark<Fnorkle> quark = new Quark<>();
        Particle<Long, Double> p = new Particle<>();
        PrintUtil.print(Arrays.toString(list.getClass().getTypeParameters()));
        PrintUtil.print(Arrays.toString(map.getClass().getTypeParameters()));
        PrintUtil.print(Arrays.toString(quark.getClass().getTypeParameters()));
        PrintUtil.print(Arrays.toString(p.getClass().getTypeParameters()));
        //getTypeParameters()返回当前类中声明的泛型参数
        PrintUtil.print(Arrays.toString(a.getClass().getTypeParameters()));

    }

    @Test
    public void creatArrayTest() {
        ArrayMaker<String> s = new ArrayMaker<>(String.class);
        String s1 = "aaa";
        //PrintUtil.print(s1 instanceof String);
        // s.create(5);
        s.set(s1);
    }
}

class ArrayMaker<T> {
    private Class<T> tClass;
    private List<T> list = Lists.newArrayList();

    public ArrayMaker(Class<T> kind) {
        this.tClass = kind;
    }

    T[] create(int size) {
        PrintUtil.print(tClass);
        return (T[]) Array.newInstance(tClass, size);
    }

    public void set(T t) {
        if (tClass.isInstance(t)) {
            PrintUtil.print(tClass.isInstance(t));
            list.add(t);
        }
    }
}

package com.reflex.test;

import com.demo.domain.Fruit;
import com.demo.domain.Holder;
import com.generics.Generator;
import com.reflex.test.springContent.ApplicationContext;
import com.reflex.test.springContent.ClassPathXmlApplicationContext;
import com.reflex.test.springContent.Context;
import com.utils.FruitInfoUtil;
import com.utils.PrintUtil;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        Master master = (Master) Context.getBean("proxyBean");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("***********************************");
        master.WalkDog();
        int a = 5;
        int b = 4;
        double c = a * b / 2;
        PrintUtil.print(16 << 1);
        Map<String, String> maps = new HashMap<>();
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("aa", "bb");
        linkedHashMap.get("aa");
        maps.put("aa", "bb");
        maps.get("aa");
    }

    @Test
    public void test() throws Exception {
//        Fruit fr=new Fruit();
//        fr.toString();
//        FruitInfoUtil.getFruitInfo(fr);
//        String a="";
//        ObjectInputStream in =new ObjectInputStream(new FileInputStream(a));
//        List<Holder> list = (List<Holder>) in.readObject();
        int num = 22;
        PrintUtil.print(Integer.toBinaryString(num));
        PrintUtil.print(num + ">> 1=" + (num >> 1));
        PrintUtil.print(Integer.toBinaryString(num >> 1) + " | " + (num >> 1));
        PrintUtil.print(Integer.toBinaryString(num >> 2) + " | " + (num >> 2));
        PrintUtil.print(Integer.toBinaryString(num >> 3) + " | " + (num >> 3));
        PrintUtil.print(Integer.toBinaryString(num << 1) + " | " + (num << 1));
        PrintUtil.print(Integer.toBinaryString(num << 2) + " | " + (num << 2));
        PrintUtil.print(Integer.toBinaryString(num << 3) + " | " + (num << 3));
        PrintUtil.print((1 << 30) + " | " + Integer.MAX_VALUE);
        PrintUtil.print(Integer.toBinaryString(1 << 30) + " | " + (1 << 30));
        PrintUtil.print(Integer.toBinaryString(1));
// Map<String,String> maps=new HashMap<>(37);
//        PrintUtil.print(Integer.toBinaryString(1 << 30)+"|"+Integer.MAX_VALUE);

    }

    @Test
    public void test2() {
        PrintUtil.print(Integer.toBinaryString(-1));
    }

    interface inter1<T> {
    }

    class show1<T> implements inter1<T> {
    }

    class show2<T> extends show1<T> implements inter1<T> {
    }

    ;
}

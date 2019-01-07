package com.demo.domain;

import com.generics.Customer;
import com.generics.Generator;
import com.generics.LinkedStack;
import com.google.common.collect.Lists;
import com.reflex.test.Dog;
import com.utils.PrintUtil;

import java.util.*;

public class TestD {
    int i;
}

class junit {

    public static void main(String[] agrs) {
//        LinkedStack<String> linkedStack=new LinkedStack<>();
//        linkedStack.push("aaaa");
//        linkedStack.push("bbbbb");
//        linkedStack.push("啛啛喳喳");
//        linkedStack.push("CCC");
//        Generator<Customer> cus=Customer.generator();
//        Customer next = cus.next();
//        PrintUtil.print(next.toString());toString
        List<String> list = Lists.newArrayList();
        List<Integer> list1 = Lists.newArrayList();
        PrintUtil.print(list.equals(list1));
        Class a = list.getClass();
        Class a1 = list1.getClass();
        PrintUtil.print("list.getClass():" + a + "      list1.getClass():" + a1);
        PrintUtil.print(a == a1);

    }

    public static void sets() {
        Set<String> set1 = new HashSet<>();
        set1.add("111");
        set1.add("222");
        Set<String> set2 = new HashSet<>();
        set2.add("111");
        set2.add("333");
        //  set2.retainAll(set1);//保留两个集合相同的部分
        set2.removeAll(set1);
        for (String a : set2) {
            System.out.println(a);
        }
    }

    public static Dog dog() {
        return new Dog() {
            @Override
            public void bark() {
                PrintUtil.print("aaaa");
            }
        };
    }
}

package com.utils;

import java.util.Arrays;
import java.util.Collection;

public class PrintUtil {
    public static void print(Object ob) {
        System.out.println(ob);
    }

    public static <T> void print(Collection<T> ob) {
        StringBuffer sb = new StringBuffer();
        for (T t : ob) {
            sb.append(t.toString() + "|");
        }
        print(sb.toString());
    }

    public static <T> void print(T[] ts) {
        print(Arrays.asList(ts));
    }
    public static void PrintFileCollection(Collection<?> c){
        print(fileFormat(c));
    }
    public static String fileFormat(Collection<?> c){
        if(c.isEmpty() || c.size() == 0){
            return "[]";
        }
        StringBuffer result = new StringBuffer("[");
        for(Object elem : c){
            if(c.size()!=1){
                result.append("\n ");
            }
            result.append(elem);
        }
        if(c.size()!= 1){
            result.append("\n ");
        }
        result.append("]");
        return result.toString();
    }
}

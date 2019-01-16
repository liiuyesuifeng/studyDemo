package com.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PrintUtil {
    public synchronized static void print(Object ob) {
        System.out.println(ob);
    }

    public static <T> void print(Collection<T> con) {
        print(getCollectionPrintStr(con));
    }

    public static void print(Iterator it) {
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            sb.append(it.next());
        }
        print(sb.toString());
    }

    public static <T> void print(T[] t) {
        print(getArraysPrintStr(t));
    }

    public static <T> String getCollectionPrintStr(Collection<T> con) {
        StringBuffer strs = StringBufferFactory.getStrs();
        for (T t : con) {
            strs.append(t.toString() );
        }
        return strs.toString();
    }

    public static <T> String getArraysPrintStr(T[] t) {
        if (t instanceof Object) {
            List<T> ts = Arrays.asList(t);
            return getCollectionPrintStr(ts);
        }
        return "";
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
class StringBufferFactory {
    public static StringBuffer getStrs() {
        return new StringBuffer();
    }
}

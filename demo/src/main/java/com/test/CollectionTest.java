package com.test;

import com.utils.PrintUtil;
import org.junit.jupiter.api.Test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionTest {
    private static ReferenceQueue<VeryBig> rq = new ReferenceQueue<>();

    @Test
    public void test1() {
        List<String> list = new ArrayList<>();
        list.add("ccc");
        Collections.sort(list);
        int aaa = Collections.binarySearch(list, "aaa");
        PrintUtil.print(aaa);
    }

    @Test
    public void test2() {
        Collection<String> con = new ArrayList<>();
        Iterator<String> iterator = con.iterator();
        con.add("c");
//        iterator.next();
        CopyOnWriteArrayList<String> cons = new CopyOnWriteArrayList<>();
        Iterator<String> iterator1 = cons.iterator();
        cons.add("b");
        iterator1.next();
    }

    @Test
    public void test3() {
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        weakHashMap.put("aa", "bb");

    }

    public void checkQueue() {
        Reference<? extends VeryBig> inq = rq.poll();
        if (inq != null) {
            PrintUtil.print("In queur " + inq.get());
        }

    }

    @Test
    public void test4() {
        LinkedHashMap<String, String> parma = new LinkedHashMap<>();
        parma.put("aa", "bb");
        parma.put("cc", "dd");
        Map<String, String> pa = new HashMap<>();
        pa.put("aa", "bb");
    }
}

class VeryBig {
    private static final int SIZE = 10000;
    private long[] la = new long[SIZE];
    private String ident;

    public VeryBig(String ident) {
        this.ident = ident;
    }

    @Override
    public String toString() {
        return ident;
    }

    @Override
    protected void finalize() throws Throwable {
        PrintUtil.print("Filanlize " + ident);
    }
}

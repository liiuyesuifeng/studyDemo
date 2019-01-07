package com.test;

import com.example.demo.ThreadDomain.Liftoff;
import com.example.demo.ThreadDomain.TaskWithResult;
import com.utils.NewUtils;
import com.utils.PrintUtil;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadTest {
    public static int [] ints = {Thread.MAX_PRIORITY,Thread.MIN_PRIORITY,Thread.NORM_PRIORITY};
    public static void test1(){
        Random random = new Random(3);

        ExecutorService exec = Executors.newCachedThreadPool();// 创建缓存线程
//        ExecutorService exec = Executors.newFixedThreadPool(2);//创建有限的线程执行
//        ExecutorService exec = Executors.newSingleThreadExecutor();//创建单一线程

        int i = 0;
        while (true){
            int i1 = random.nextInt(2);
            i++;
            if(i == 20){
                exec.shutdown();
                break;

            }
            exec.execute(new Liftoff("A" + i,ints[i1]));
        }
    }
    public void test2() throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        List<Future<String>> arrayList = new ArrayList<>();
        int i=0;
        while (true){
            i++;
            if(i == 20){
                exec.shutdown();
                break;

            }
            Future<String> submit = exec.submit(new TaskWithResult(i));

            arrayList.add(submit);
            PrintUtil.print(submit.isDone() + "submit thread boolean");
        }
        for(Future<String> fr : arrayList){
            PrintUtil.print(fr.isDone() + "for thread boolean");
            PrintUtil.print(fr.get());
            PrintUtil.print("----------------------------------------------");
        }
    }
    public static void main(String [] args) throws Exception{
        test1();

    }

}

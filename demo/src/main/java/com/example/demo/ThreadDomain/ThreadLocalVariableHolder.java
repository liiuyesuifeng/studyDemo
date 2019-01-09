package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadLocalVariableHolder {
    //声明互不干扰的线程变量
    private static ThreadLocal<Integer> value = new ThreadLocal<Integer>(){
        private Random rd = new Random(47);
        protected synchronized Integer initialValue(){
            return rd.nextInt(10000);
        }
    };
    public static int get(){
        return value.get();
    }
    public static void increment(){
        value.set(value.get() + 1);
    }
    public static void main(String [] agrs) throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0 ;i<5;i++){
            exec.execute(new Accessor(i));
        }
        TimeUnit.MILLISECONDS.sleep(3);
        exec.shutdownNow();


    }

}
class Accessor implements Runnable{
    private final int id;
    public Accessor(int id){
        this.id = id;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            ThreadLocalVariableHolder.increment();
            PrintUtil.print(this);
            Thread.yield();
        }
    }

    @Override
    public String toString() {
        return "#" + id + " ：" + ThreadLocalVariableHolder.get();
    }
}
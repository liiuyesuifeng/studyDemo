package com.example.demo.ThreadDomain;

import ch.qos.logback.core.util.TimeUtil;
import com.utils.PrintUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class SimpleDaems implements Runnable {
    @Override
    public void run() {
        try{
            while(true){
                TimeUnit.MILLISECONDS.sleep(100);
                PrintUtil.print(Thread.currentThread() + " " + this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void test3() throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool(new DeamThreadFactory());
        for(int i =0;i<10;i++){
//            Thread th = new Thread(new SimpleDaems());
////            th.setDaemon(true);
////            th.start();
            exec.execute(new DeamFromFactory());
        }
        PrintUtil.print("All deams start");
        TimeUnit.MILLISECONDS.sleep(500);
    }

    /**
     * 通过设置UncaughtException 捕获线程异常
     */
    public static void test4() {
        ExecutorService exec = Executors.newCachedThreadPool(new HandleThreadFactory());
        try{
            exec.execute(new ExceotionThread2());
        }catch (Exception e){
            e.printStackTrace();
            PrintUtil.print("捕获");
        }
    }
    /**
     * 通过设置UncaughtException 捕获线程异常
     */
    public static void test5() {
        //设置线程捕获静态域，下面所有线程都会捕获异常
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        ExecutorService exec = Executors.newCachedThreadPool();
        try{
            exec.execute(new ExceotionThread());
        }catch (Exception e){
            e.printStackTrace();
            PrintUtil.print("捕获");
        }
    }
    public static void main(String [] args){
        test5();

    }
}
class DeamThreadFactory implements ThreadFactory{

    @Override
    public Thread newThread(Runnable r) {
        Thread th = new Thread(r);
        th.setDaemon(true);
        return th;
    }
}
class DeamFromFactory implements Runnable{
    @Override
    public void run() {
        try{
            while(true){
                TimeUnit.MILLISECONDS.sleep(100);
                PrintUtil.print(Thread.currentThread() + " " + this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class Sleeper extends Thread{
    private int time;
    public Sleeper(int time,String name){
        super(name);
        this.time = time;
        start();
    }

    @Override
    public void run() {
        try{
            sleep(time);
        }catch (Exception e){
//            PrintUtil.print();
        }

    }
}
class ExceotionThread implements Runnable{
    public void run(){
        throw new RuntimeException();
    }
}
class ExceotionThread2 implements Runnable{
    @Override
    public void run() {
        Thread th = Thread.currentThread();
        PrintUtil.print("run by " + th);
        PrintUtil.print("eh  " + th.getUncaughtExceptionHandler());
        throw new RuntimeException();
    }
}

/**
 * 集成异常捕获，并打印异常
 */
class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        PrintUtil.print("caught" );
//        e.printStackTrace();
    }
}

/**
 *
 * 创建Thread工厂
 * 在线程创建时设置线程出现异常捕获类UncaughtException
 */
class HandleThreadFactory implements ThreadFactory{

    @Override
    public Thread newThread(Runnable r) {
        PrintUtil.print(this + " creating new Thread");
        Thread th = new Thread(r);
        PrintUtil.print(" create " + th);
        //设置异常捕获
        th.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        PrintUtil.print("eh " + th.getUncaughtExceptionHandler());
        return th;
    }
}
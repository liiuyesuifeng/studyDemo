package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Liftoff implements Runnable {
    protected int countDown = 10;
    private static int taskCount = 0;
    private final int id = taskCount ++;
    private Random rd = new Random(10);
    private String name;
    private int priorty;
    public Liftoff(){}
    public Liftoff(int countDown){
        this.countDown = countDown;
    }
    public Liftoff(String name,int priorty){
        this.name = name;
        this.priorty = priorty;
    }


    @Override
    public String toString() {
        return "#" + id + "#"+name + "(" + (countDown>0?countDown:"Liftoff!") + ")," + Thread.currentThread();
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(priorty);
        while(countDown-- > 0){
            PrintUtil.print(toString());
//            Thread.yield();
            try{
//                int i = rd.nextInt(10);
//                PrintUtil.print(i);
//                TimeUnit.SECONDS.sleep(i);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}

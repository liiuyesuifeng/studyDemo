package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 公园人数统计demo
 * 多线程
 * 公园每个大门人数和总人数之间统计
 * @author lhr
 */
public class OrnamentalGarden {
    public static void main(String [] args) throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        //入口有5个
        for(int i =0;i < 5;i++){
            exec.execute(new Entrance(i));
        }
        //线程休眠 秒*3
        TimeUnit.SECONDS.sleep(3);
        Entrance.cancel();
        //等待所有哦线程任务完成后关闭
        exec.shutdown();
        //主线程等待千分之一毫秒 * 250 返回当前线程池中是否还有运行的线程，没有返回true 否则返回fasle
        if(!exec.awaitTermination(250,TimeUnit.MICROSECONDS)){
            PrintUtil.print("Some task were not terminated");
        }
        PrintUtil.print("Total :" + Entrance.getTotalCount());
        PrintUtil.print("Sum of Entrance : " + Entrance.sumEntrances());
    }
}

/**
 * 统计类
 */
class Count{
    private int count = 0;
    private Random rd = new Random(47);

    public synchronized int increment(){
        int temp = count;
//        if(rd.nextBoolean()){//随机返回一个boolean
////            Thread.yield();//线程让步
//        }
        return (count = ++temp);
    }
    public synchronized int value(){
        return count;
    }
}

/**
 * 入口类
 */
class Entrance implements Runnable{
    private static Count count = new Count();
    private static List<Entrance> entrances = new ArrayList<>();
    private int number = 0;
    private final int id;
    //volatile 原子性，线程每一次读数据会从最新内存中获取
    private static volatile boolean canceled = false;
    public static void cancel(){
        canceled = true;
    }
    public Entrance(int id){
        this.id = id;
        entrances.add(this);
    }

    @Override
    public void run() {
        while(!canceled){
            synchronized (this){
                ++number;
                PrintUtil.print(this + "\tTotal" + count.increment());
            }

            try{
                //休眠线程千分之一秒* 100
                TimeUnit.MILLISECONDS.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
                PrintUtil.print("sleep interrupted");
            }
        }
        PrintUtil.print("Stopping" + this);
    }
    public synchronized int getValue(){
        return number;
    }
    public static int getTotalCount(){
        return count.value();
    }
    public static int sumEntrances(){
        int sum = 0;
        for(Entrance entrance : entrances){
            sum += entrance.getValue();
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Entrance" + id + ":" + getValue();
    }
}

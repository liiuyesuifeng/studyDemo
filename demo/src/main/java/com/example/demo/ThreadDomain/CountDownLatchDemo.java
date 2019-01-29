package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 计数消锁demo
 *
 */
public class CountDownLatchDemo {
    static final int SIZE = 100;
    public static void main(String [] agrs){
        ExecutorService exec = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(SIZE);
        for(int i =0;i<10;i++){
            exec.execute(new WaitingTask(latch));
        }
        for(int i = 0;i<SIZE;i++){
            exec.execute(new TaskPortion(latch));
        }
        PrintUtil.print("Latch all task");
        exec.shutdownNow();
    }
}
class TaskPortion implements Runnable{
    private static int counter = 0;
    private final int id = counter ++;
    private static Random rand = new Random(47);
    private final CountDownLatch latch;
    TaskPortion(CountDownLatch latch){
        this.latch = latch;
    }

    @Override
    public void run() {
        try{
            doWork();
            latch.countDown();
        }catch (InterruptedException ie){

        }
    }
    public void doWork() throws InterruptedException{
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
        PrintUtil.print(this + "competed");
    }

    @Override
    public String toString() {
        return String.format("%1$-3d ",id);
    }
}
class WaitingTask implements Runnable{
    private static int counter = 0;
    private final int id = counter ++;
    private final CountDownLatch latch;
    WaitingTask(CountDownLatch latch){
        this.latch = latch;
    }

    @Override
    public void run() {
        try{
            latch.await();
            PrintUtil.print("Latch barrier passed for " + this);
        }catch (InterruptedException ie){
            PrintUtil.print(this + "interrupted");
        }
    }

    @Override
    public String toString() {
        return String.format("WaitingTask %1$-3d ",id);
    }
}
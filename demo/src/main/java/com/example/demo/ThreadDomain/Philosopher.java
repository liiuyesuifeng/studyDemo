package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//筷子持有者 ： 哲学家 实体
public class Philosopher implements Runnable{
    private Chopstick left;//左边筷子
    private Chopstick right;//右面筷子
    private final int id ;//人员编号
    private final int ponderFactor;//印象因素
    //休眠方法
    private void pause() throws InterruptedException{
        if(ponderFactor == 0)
            return;
        TimeUnit.MILLISECONDS.sleep(500);
    }
    public Philosopher(int id ,int ponderFactor,Chopstick left,Chopstick right){
        this.id = id;
        this.ponderFactor = ponderFactor;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()) {
                PrintUtil.print(this + "\tThinking");
                pause();
                right.take();
                PrintUtil.print(this + "\tgrabbing right");//获取右边筷子
                left.take();
                PrintUtil.print(this + "\tgrabbing left");//获取左边筷子
                PrintUtil.print(this + "\teating");
                pause();
                left.drop();
                right.drop();

            }
        }catch (InterruptedException e){
            PrintUtil.print(this + "    " + "exiting via interrupt");
        }
    }

    @Override
    public String toString() {
        return "Philosopher\t" + id;
    }
    public static void main(String [] agrs) throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick left = new Chopstick();
        Chopstick right = new Chopstick();
        Philosopher p1 = new Philosopher(1,0,left,right);
        Philosopher p2 = new Philosopher(2,0,left,right);
        exec.submit(p1);
        exec.submit(p2);
        TimeUnit.SECONDS.sleep(2);
        exec.shutdownNow();
    }
}
class Chopstick{
    //获取标志
    private boolean taken = false;
    //获取
    public synchronized void take() throws InterruptedException{
        while(taken){
            wait();
        }
        taken = true;
    }
    //释放
    public synchronized void drop(){
        taken = false;
        notifyAll();
    }
}
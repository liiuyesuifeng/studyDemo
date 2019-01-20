package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * notify 和 notifyAll对比
 * notify唤醒一条正在等当前锁的线程
 * notifyAll唤醒所有正在等待当前锁的线程
 *
 */
public class NotifyVSNotifyAll {
    public static void main(String [] args) throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0;i<5;i++){
            exec.execute(new Task());
        }
        exec.execute(new Task2());
        Timer timer = new Timer();//定时器
        timer.scheduleAtFixedRate(new TimerTask() {
            boolean prod = true;
            @Override
            public void run() {
                if(prod){
                    PrintUtil.print("\nnotify()");
                    Task.blocker.prod();
                    prod = false;
                }else{
                    PrintUtil.print("\nnotifyall()");
                    Task.blocker.prodAll();
                    prod = true;
                }
            }
        },400,400);
    }
}
class Blocker {
    synchronized void waitingCall(){
        try{
            while(!Thread.interrupted()){
                wait();
                PrintUtil.print(Thread.currentThread() + "");
            }
        }catch (InterruptedException ie){
            PrintUtil.print("中断异常");
        }
    }
    synchronized void prod(){notify();}
    synchronized void prodAll(){notifyAll();}
}
class Task implements Runnable{
    static Blocker blocker = new Blocker();

    @Override
    public void run() {
        blocker.waitingCall();
    }
}
class Task2 implements Runnable{
    static Blocker blocker = new Blocker();

    @Override
    public void run() {
        blocker.waitingCall();
    }
}


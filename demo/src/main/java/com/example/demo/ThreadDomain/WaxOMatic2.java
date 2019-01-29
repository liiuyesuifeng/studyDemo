package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
public class WaxOMatic2 {
    public static void main(String [] agrs) throws Exception{
        CarOw car = new CarOw();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new WaxOffOw(car));
        exec.execute(new WaxOnOw(car));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}
class CarOw{
    private Lock lock = new ReentrantLock();
    //监听机制
    private Condition condition = lock.newCondition();
    //上蜡
    private boolean waxOn = false;
    public void waxed(){
        lock.lock();
        try{
            waxOn = true;//准备抛光/ready to buff
            condition.signalAll();//唤醒所有等待的线程
        }finally {
            lock.unlock();
        }

    }

    public void buffed(){
        lock.lock();
        try{
            waxOn = false;//准备下一层蜡
            condition.signalAll();
        }finally {
            lock.unlock();
        }

    }
    public void waitForWaxing() throws InterruptedException {
        lock.lock();
        try {
            while (waxOn == false) {
                condition.await();
            }
        } finally {
            lock.unlock();
        }

    }
    public void waitForBuffing() throws InterruptedException {
        lock.lock();
        try {
            while (waxOn == true) {
                condition.await();
            }
        } finally {
            lock.unlock();
        }

    }
}
class WaxOnOw implements Runnable{
    private CarOw car;
    public WaxOnOw(CarOw car){
        this.car = car;
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                PrintUtil.print("Wax on !");
                TimeUnit.MILLISECONDS.sleep(200);
                car.waxed();
                car.waitForBuffing();
            }
        }catch (InterruptedException ie){
            PrintUtil.print("Exitsing via interrupt");
        }
        PrintUtil.print("Ending Wax on task");
    }
}
class WaxOffOw implements Runnable{
    private CarOw car;
    public WaxOffOw(CarOw car){
        this.car = car;
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()){
                car.waitForWaxing();
                PrintUtil.print("Wax off");
                TimeUnit.MILLISECONDS.sleep(200);
                car.buffed();

            }
        }catch (InterruptedException ie){
            PrintUtil.print("Exiting via interput");
        }
        PrintUtil.print("Ending Wax off task");
    }
}


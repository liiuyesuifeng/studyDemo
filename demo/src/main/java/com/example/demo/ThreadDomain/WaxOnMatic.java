package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * wait and notify notifyall demo
 * knowledge/知识点 ：线程协作
 */
public class WaxOnMatic {
    public static void main(String [] agrs) throws Exception{
        Car car = new Car();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOn(car));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();

    }
}
class Car{
    //上蜡
    private boolean waxOn = false;
    public synchronized void waxed(){
        waxOn = true;//准备抛光/ready to buff
        notifyAll();
    }

    public synchronized void buffed(){
        waxOn = false;//准备下一层蜡
        notifyAll();
    }
    public synchronized void waitForWaxing() throws InterruptedException{
        while(waxOn == false){
            wait();
        }
    }
    public synchronized void waitForBuffing() throws InterruptedException{
        while(waxOn == true){
            wait();
        }
    }
}
class WaxOn implements Runnable{
    private Car car;
    public WaxOn(Car car){
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
class WaxOff implements Runnable{
    private Car car;
    public WaxOff(Car car){
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

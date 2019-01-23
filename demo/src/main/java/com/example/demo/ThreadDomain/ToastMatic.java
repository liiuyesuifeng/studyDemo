package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 吐司模型
 * 通过队列BlockQueue线程同步
 */
public class ToastMatic {
    public static void main(String [] args) throws Exception{
        ToastQueue dryQueue = new ToastQueue(),
                butterQueue = new ToastQueue(),
                finishedQueue = new ToastQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Toaster(dryQueue));
        exec.execute(new Butterer(dryQueue,butterQueue));
        exec.execute(new Jammer(butterQueue,finishedQueue));
        exec.execute(new Eater(finishedQueue));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();

    }
}
class Toast{
    public enum Staus{
        DRY,BUTTERRD,JAMMED
    }
    private Staus staus = Staus.DRY;
    private final int id;
    public Toast(int id){
        this.id = id;
    }
    public void butter(){
        staus = Staus.BUTTERRD;
    }
    public void jam(){
        staus = Staus.JAMMED;
    }

    public Staus getStaus() {
        return staus;
    }
    public int getId(){
        return this.id;
    }

    @Override
    public String toString() {
        return "Toast " + id + ":" +staus;
    }
}
class ToastQueue extends LinkedBlockingQueue<Toast>{}

class Toaster implements Runnable{
    private ToastQueue toastQueue;
    private int count = 0;
    private Random rd = new Random(47);
    public Toaster(ToastQueue toasts){
        this.toastQueue = toasts;
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                TimeUnit.MILLISECONDS.sleep(100 + rd.nextInt(500));
                Toast t = new Toast(count ++);
                PrintUtil.print(t);
                toastQueue.put(t);
            }
        }catch (InterruptedException ie){
            PrintUtil.print("Toaster is Interrupted");
        }
        PrintUtil.print("Toaster off");

    }
}
class Butterer implements Runnable{
    private ToastQueue dryQueue,butteredQueue;
    public Butterer(ToastQueue dry,ToastQueue buttered){
        dryQueue = dry;
        butteredQueue = buttered;
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                Toast t = dryQueue.take();
                t.butter();
                PrintUtil.print(t);
                butteredQueue.put(t);
            }
        }catch (InterruptedException ie){
            PrintUtil.print("Butterer is Interrupted");
        }
        PrintUtil.print("Butterer off");
    }
}


class Jammer implements Runnable{
    private ToastQueue butteredQueue,finishedQueue;
    public Jammer(ToastQueue buttered,ToastQueue finished){
        butteredQueue = buttered;
        finishedQueue = finished;
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                Toast t = butteredQueue.take();
                t.jam();
                PrintUtil.print(t);
                finishedQueue.put(t);
            }
        }catch (InterruptedException ine){
            PrintUtil.print("Jammer is Interrupted");
        }
        PrintUtil.print("Jammer off");
    }
}

class Eater implements Runnable{
    private ToastQueue finishedQueue;
    private int counter = 0;
    public Eater(ToastQueue finished){
        finishedQueue = finished;
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                Toast t = finishedQueue.take();
                if(t.getId() != counter ++ ||t.getStaus() != Toast.Staus.JAMMED){
                    PrintUtil.print(">>>>>Error :" + t);
                    System.exit(1);
                }else{
                    PrintUtil.print("Chomp !" + t);
                }
            }
        }catch (InterruptedException ie){
            PrintUtil.print("Eater Interrupted");
        }
        PrintUtil.print("Eater off");
    }
}
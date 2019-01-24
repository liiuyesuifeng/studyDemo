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
 * 同步队列在调用put时会释放放入对象的锁并唤醒其他操作这个对象的线程
 */
public class ToastMatic {
    public static void main(String [] args) throws Exception{
        ToastQueue dryQueue = new ToastQueue(),
                butterQueue = new ToastQueue(),
                finishedQueue = new ToastQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Butterer(dryQueue,butterQueue));
        exec.execute(new Toaster(dryQueue));
        exec.execute(new Jammer(butterQueue,finishedQueue));
        exec.execute(new Eater(finishedQueue));
        TimeUnit.MILLISECONDS.sleep(100);
        exec.shutdownNow();

    }
}
class Toast{
    //吐司三种状态 ：制作 涂抹 果酱
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

/**
 * 制作吐司
 */
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
                //休眠随机时间
//                TimeUnit.MILLISECONDS.sleep(100 + rd.nextInt(500));
                //制作吐司
                Toast t = new Toast(count ++);
                PrintUtil.print(t);
                //放入队列尾部
                toastQueue.put(t);
            }
        }catch (InterruptedException ie){
            PrintUtil.print("Toaster is Interrupted");
        }
        PrintUtil.print("Toaster off");

    }
}
//涂抹黄油
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
                //获取已经制作完成的吐司
                Toast t = dryQueue.take();
                //修改状态
                t.butter();
                PrintUtil.print(t);
                //将吐司放入涂抹黄油完成的队列
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
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
//        TimeUnit.MILLISECONDS.sleep(500);
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
        test1();
    }

    /**
     * 打破第四种死锁条件，将最后一个人获取筷子的方式做改变
     * 先获取最左边的筷子后获取最右边的筷子
     * @throws Exception
     */
    public static void test3() throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick [] chopsticks = new Chopstick[5];
        for(int i = 0; i<5 ; i++){
            chopsticks[i] = new Chopstick();
        }
        for(int i = 0; i <5;i++){
            if(i < 4){
                exec.execute(new Philosopher(i,5,chopsticks[i],chopsticks[i+1]));
            }else{
                exec.execute(new Philosopher(i,5,chopsticks[0],chopsticks[i]));
            }
//            PrintUtil.print((i + 1)%5);
//            exec.execute(new Philosopher(i,5,chopsticks[i],chopsticks[(i + 1)%5]));
        }
        TimeUnit.SECONDS.sleep(10);
        exec.shutdownNow();
    }
    /**
     * 发生死锁条件
     * 1、互斥条件。任务中使用资源至少有一个不能共享
     * 2、至少有一个任务他必须持有一个资源且正在等待获取另一个当前被别的任务持有的资源
     * 3、资源不能被任务抢占，任务必须把资源释放当做普通事件
     * 4、必须循环等待，一个任务等待其他任务所持有的资源，后者又在等待另一个任务所持有的资源，
     * 这样所有的热舞都会循环等待陷入死锁
     * 其中 1、2、3条件属于难控制一类，所以得需改4条件
     * @throws Exception
     */
    public static void test2() throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick [] chopsticks = new Chopstick[5];
        for(int i = 0; i<5 ; i++){
            chopsticks[i] = new Chopstick();
        }
        for(int i = 0; i <5;i++){
            PrintUtil.print((i + 1)%5);
            exec.execute(new Philosopher(i,5,chopsticks[i],chopsticks[(i + 1)%5]));
        }
        TimeUnit.SECONDS.sleep(10);
        exec.shutdownNow();
    }
    public static void test1() throws Exception{
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
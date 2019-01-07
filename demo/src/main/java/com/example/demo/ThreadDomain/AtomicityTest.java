package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicityTest implements Runnable {
    private int i = 0;
    private AtomicInteger ai = new AtomicInteger(0);
    public  int getValue(){
        return i;
    }
    public  int getValueAI(){
        return ai.get();
    }
    private synchronized void evenIncrement(){
        i++;
        i++;
    }
    private  void evenIncrementAI(){
        ai.addAndGet(2);
    }

    @Override
    public void run() {
        while (true){
            try{
                TimeUnit.MILLISECONDS.sleep(150);
//                evenIncrement();
                evenIncrementAI();
                PrintUtil.print(getValueAI() + " " +Thread.currentThread().getName());
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    public static void main(String [] args) throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        AtomicityTest at = new AtomicityTest();
        exec.execute(at);
        while(true){
//            int val = at.getValue();
            int val = at.getValueAI();
            PrintUtil.print(val);
            /*
            出现奇数原因是因为线程在修改i的过程中其他线程调用了getvalue方法获取了
            解决 在getvalu上加上同步锁
            使用原子类代替Int
             */

            if(val % 2 != 0){
                PrintUtil.print(val);
                System.exit(0);
            }
        }
    }
}

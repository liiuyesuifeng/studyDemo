package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EvenChecker implements Runnable {
    private IntGenerator generator;
    private final int id;
    public EvenChecker(IntGenerator generator,int id) {
        this.generator = generator;
        this.id = id;
    }

    @Override
    public void run() {
        PrintUtil.print(generator.isCanceled());
        while(!generator.isCanceled()){
            int val = generator.next();
            PrintUtil.print("val " + val + " ThreadName :" + Thread.currentThread().getName() + " " +generator.isCanceled() );
            try{
                TimeUnit.MILLISECONDS.sleep(1500);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(val % 2 !=0){
                PrintUtil.print(val + "not event");
                generator.cancel();
            }
        }
    }
    public static void test(IntGenerator gp,int count){
        PrintUtil.print("---------测试demo----------");
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0;i < count;i++){
            PrintUtil.print("--------------------");
            exec.execute(new EvenChecker(gp,i));
        }
        exec.shutdown();
    }
    public static void test(IntGenerator gp){
        /*
        当一个线程在调用next方法时，
        其他线程也刚好在调用就会造成多个线程操作同一个数据，
        数据结果就会异常导致程序退出
         */
        PrintUtil.print("----------start----------");
        test(gp,20);
    }
}

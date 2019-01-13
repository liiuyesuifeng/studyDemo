package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Interrupting {
    private static ExecutorService exec = Executors.newCachedThreadPool();
    static void test(Runnable rb) throws InterruptedException{
        Future<?> f = exec.submit(rb);
        //千分之一秒 * 100 = 0.1s
        TimeUnit.MILLISECONDS.sleep(100);
        PrintUtil.print("中断 ： " +rb.getClass().getName());
        f.cancel(true);//中断当前线程
        PrintUtil.print("中断发送到 " +rb.getClass().getName());
    }

    /**
     * 文件流读取(nio除外)和等待获取锁形成的阻塞无法被Interrup所中断，休眠等待可以但是会抛出中断异常
     * @param args
     * @throws Exception
     */
    public static void main(String [] args) throws Exception{
        test(new SleepBlocked());
        test(new IOBlocked(System.in));
        test(new SynchronizedBlock());
        TimeUnit.SECONDS.sleep(3);
        PrintUtil.print("异常终止退出");
        System.exit(0);

    }
}
class SleepBlocked implements Runnable{
    @Override
    public void run() {
        try{
            //休眠100s
            TimeUnit.SECONDS.sleep(100);
        }catch (InterruptedException ine){
            PrintUtil.print("InterruptedException");
        }
        PrintUtil.print("Exiting SleepBlocked,Run()");
    }
}
class IOBlocked implements Runnable{
    private InputStream in;
    public IOBlocked(InputStream is){
        in = is;
    }

    @Override
    public void run() {
        PrintUtil.print("Wating for read()");
        try{
            in.read();
        }catch (IOException io){
            PrintUtil.print(111);
            if(Thread.currentThread().isInterrupted()){
                PrintUtil.print("中断阻塞io");
            }else{

                throw new RuntimeException(io);
            }
        }

    }
}
class SynchronizedBlock implements Runnable{
    public synchronized void f(){
        while(true){
            Thread.yield();
        }
    }

    public SynchronizedBlock(){
        new Thread(){
            @Override
            public void run() {
                f();
            }
        }.start();
    }

    @Override
    public void run() {
        PrintUtil.print("尝试调用f()");
        f();
        PrintUtil.print("退出SynchronizedBlock.run()");
    }
}
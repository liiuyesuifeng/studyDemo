package com.example.demo.ThreadDomain;

import com.utils.NewUtils;
import com.utils.PrintUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
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


    public static void main(String [] args) throws Exception{
        test3();
    }
    public static void test3() throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        ServerSocket server = new ServerSocket(8080);
        InetSocketAddress isa = new InetSocketAddress("localhost",8080);
        SocketChannel open = SocketChannel.open(isa);
        SocketChannel open1 = SocketChannel.open(isa);
        Future<?> submit = exec.submit(new NIOBlack(open));
        exec.execute(new NIOBlack(open1));
        exec.shutdown();
        TimeUnit.SECONDS.sleep(1);
        PrintUtil.print("关闭线程");
        submit.cancel(true);
        TimeUnit.SECONDS.sleep(1);
        PrintUtil.print("关闭资源");
        open1.close();
    }

    /**
     * 验证IO关闭流来结束线程
     * @throws Exception
     */
    public static void test2() throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        ServerSocket server = new ServerSocket(8080);
        InputStream inputStream = new Socket("localhost", 8080).getInputStream();
        exec.execute(new IOBlocked(inputStream));
        exec.execute(new IOBlocked(System.in));
        TimeUnit.MILLISECONDS.sleep(100);
        PrintUtil.print("停止所有线程！！！！");
        exec.shutdownNow();
        TimeUnit.SECONDS.sleep(1);
        PrintUtil.print("关闭 ：" + inputStream.getClass().getName());
        inputStream.close();
        TimeUnit.SECONDS.sleep(1);
        PrintUtil.print("关闭 ： " + System.in.getClass().getName());
        System.in.close();
    }
    /**
     * 文件流读取(nio除外)和等待获取锁形成的阻塞无法被Interrup所中断，
     * 休眠等待可以但是会抛出中断异常
     * （文件流线程中断：关闭资源或者使用nio读取，等待阻塞解决方案：尝试使用lock
     * 锁，中的lockInterruptibly获取可中断的锁）
     * @throws Exception
     */
    public static void test1() throws Exception{
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
        PrintUtil.print("退出IOBlocked");
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
class NIOBlack implements Runnable{
    private final SocketChannel sc;
    public NIOBlack(SocketChannel sc){
        this.sc = sc ;
    }

    @Override
    public void run() {
        try{
            PrintUtil.print("等待读取：" + this.getClass().getName());
            sc.read(ByteBuffer.allocate(1));
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        PrintUtil.print("结束NIOBlack.run()");

    }
}
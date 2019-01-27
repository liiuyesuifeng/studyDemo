package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.io.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 线程协作
 * 多线程任务数据读取
 * 管道通信机制
 */
public class PipedIO {
    public static void main(String [] agrs) throws Exception{
        Sender sender = new Sender();
        Receive receive = new Receive(sender);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(sender);
        exec.execute(receive);
        TimeUnit.SECONDS.sleep(4);
        exec.shutdownNow();

    }
}
class Sender implements Runnable{
    private Random rand = new Random(47);
    //创建写入管道
    private PipedWriter out = new PipedWriter();
    private PipedOutputStream ps = new PipedOutputStream();
    public PipedWriter getPipedWriter(){
        return out;
    }
    public PipedOutputStream getPipedOutputStream(){
        return ps;
    }

    @Override
    public void run() {
        try {
//            OutputStreamWriter osw = new OutputStreamWriter(ps);
//            BufferedWriter bw = new BufferedWriter(osw);
            while (true) {
                for (char c = 'A'; c <= 'z'; c++) {
                    //写入数据
                    out.write("哈哈");
                   // bw.write("哈哈");
                    TimeUnit.MILLISECONDS.sleep(1000);
                }
            }
        }catch (IOException ioe){
            PrintUtil.print(ioe + " Sender write interrupted");
        }catch (InterruptedException e){
            PrintUtil.print(e + " Sender write interrupted");
        }
    }
}
class Receive implements Runnable{
    private PipedReader in;
    private BufferedReader br;
    public Receive(Sender sender) throws IOException{
        in = new PipedReader(sender.getPipedWriter());
        br = new BufferedReader(in);
    }

    @Override
    public void run() {
        try{
            while (true){
                //读取数据
//                PrintUtil.print("Read :" +in.read() + "," );

                PrintUtil.print("Read :" + br.readLine()+ "," );
            }
        }catch (IOException ioe){
            PrintUtil.print(ioe + "Receive read expection");
        }
    }
}

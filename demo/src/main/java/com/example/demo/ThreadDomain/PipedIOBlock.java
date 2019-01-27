package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.io.*;
import java.util.Random;
import java.util.concurrent.*;

public class PipedIOBlock {
    public static void main(String [] agrs) throws Exception{
        LinkedBlockingQueue<String> linkBlock = new LinkedBlockingQueue<>();
        SenderBlock sender = new SenderBlock(linkBlock);
        ReceiveBlock receive = new ReceiveBlock(linkBlock);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(sender);
        exec.execute(receive);
        TimeUnit.SECONDS.sleep(4);
        exec.shutdownNow();

    }
}

class SenderBlock implements Runnable{
    private BlockingQueue<String> sender;

    public SenderBlock(BlockingQueue<String> queue){
        sender = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (char c = 'A'; c <= 'z'; c++) {
                    //写入数据
                    sender.put("哈哈");
                    // bw.write("哈哈");
                    TimeUnit.MILLISECONDS.sleep(1000);
                }
            }
        }catch (InterruptedException e){
            PrintUtil.print(e + " Sender write interrupted");
        }
    }
}
class ReceiveBlock implements Runnable{
    private BlockingQueue<String> receive;
    public ReceiveBlock(BlockingQueue<String> queue){
        receive = queue;
    }

    @Override
    public void run() {
        try{
            while (true){
                //读取数据
//                PrintUtil.print("Read :" +in.read() + "," );
// receive.take();
                PrintUtil.print("Read :" + receive.take() + "," );
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

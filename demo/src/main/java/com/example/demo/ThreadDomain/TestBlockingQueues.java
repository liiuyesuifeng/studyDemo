package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class TestBlockingQueues {
    static void getKey(){
        try{
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        }catch (IOException IOE){
            throw new RuntimeException(IOE);
        }
    }
    static void getKey(String msg){
        PrintUtil.print(msg);
        getKey();
    }
    static void test(String msg,BlockingQueue<Liftoff> queue){
        PrintUtil.print(msg);
        LiftOffRunner runner = new LiftOffRunner(queue);
        Thread t = new Thread(runner);
        t.start();
        for(int i = 0;i<5;i++){
            runner.add(new Liftoff(5));
        }
        getKey("Press 'Enter' (" + msg + ")");
        t.interrupt();
        PrintUtil.print("Finshed " + msg + " test");
    }
    public static void main(String [] args) throws Exception{
        test("LinkedBlockQueue",new LinkedBlockingDeque<Liftoff>());
        test("ArrayBlockQueue", new ArrayBlockingQueue<Liftoff>(3));
        test("LinkedBlockQueue", new SynchronousQueue<Liftoff>());
    }
}
class LiftOffRunner implements Runnable{
    private BlockingQueue<Liftoff> rockets;
    public LiftOffRunner (BlockingQueue<Liftoff> queue){
        rockets = queue;
    }
    public void add(Liftoff liftoff){
        try{
            rockets.put(liftoff);
        }catch (InterruptedException ine){
            PrintUtil.print("InterruptedException dring add()");
        }

    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            try{
                Liftoff rocket = rockets.take();
                rocket.run();
            }catch (InterruptedException ine){
                PrintUtil.print("InterruptedException diring take");
            }
            PrintUtil.print("Exting LiftOffRunner");
        }
    }

}

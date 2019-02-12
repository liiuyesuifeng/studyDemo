package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class HorseRace {
    static final int FINISH_LINE = 75;
    private List<Horse> horses = new ArrayList<>();
    private ExecutorService exec = Executors.newCachedThreadPool();
    private CyclicBarrier barrier;
    public HorseRace(int nHourses,final int pause){
        barrier = new CyclicBarrier(nHourses, new Runnable() {
            @Override
            public void run() {
                StringBuilder s = new StringBuilder();
                for(int i = 0;i<FINISH_LINE;i++){
                    s.append("=");
                }
                PrintUtil.print(s);
                for(Horse horse : horses){
                    PrintUtil.print(horse.tracks());
                }
                for (Horse horse : horses){
                    if(horse.getStrides() >= FINISH_LINE){
                        PrintUtil.print(horse + "won!");
                        exec.shutdownNow();
                        return ;
                    }
                }
                try{
                    TimeUnit.MILLISECONDS.sleep(pause);
                }catch (InterruptedException ie){
                    PrintUtil.print("barrier - action sleep Interrupted");
                }
            }
        });
        for (int i =0 ;i<nHourses;i++){
            Horse horse = new Horse(barrier);
            horses.add(horse);
            exec.execute(horse);
        }
    }
    public static void main(String [] args){
        int nHouse = 7;
        int paus = 200;
        new HorseRace(nHouse,paus);
    }
}
class Horse implements Runnable{
    private static int counter = 0;
    private final int id = counter ++;
    private int strides = 0;
    private static Random rand = new Random(47);
    private static CyclicBarrier barrier;
    public synchronized int getStrides(){
        return strides;
    }
    public Horse(CyclicBarrier barrier){
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                synchronized (this){
                    strides += rand.nextInt(3);
                }
                barrier.await();
            }
        }catch (InterruptedException ie){
            PrintUtil.print("Interrupted");
        }catch (BrokenBarrierException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Horse " + id +"\t";
    }
    public String tracks (){
        StringBuilder s = new StringBuilder();
        for (int i =0;i<getStrides();i++){
            s.append("*");
        }
        s.append(id);
        return s.toString();
    }
}

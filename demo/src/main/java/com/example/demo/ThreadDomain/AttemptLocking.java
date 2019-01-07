package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AttemptLocking {
    private ReentrantLock lock = new ReentrantLock();
    public void untimed(){
        boolean captured = lock.tryLock();
        try{
            PrintUtil.print("trylock() " + captured);
        }finally {
            if(captured){
                lock.unlock();
            }
        }
    }
    public void timed(){
        boolean captured = false;
        try{
            captured = lock.tryLock(2, TimeUnit.SECONDS);
        }catch (InterruptedException ite){
            throw new RuntimeException(ite);
        }
        try{
            PrintUtil.print("tryLock(2, TimeUnit.SECONDS) " + captured);
        }finally {
            if (captured){
                lock.unlock();
            }
        }
    }
    public static void main(String [] args){
        final AttemptLocking al = new AttemptLocking();
        al.untimed();
        al.timed();
        new Thread(){
            {setDaemon(true);}

            @Override
            public void run() {
                al.lock.lock();
                PrintUtil.print("acquired");
            }
        }.start();
        Thread.yield();
        al.untimed();
        al.timed();
    }
}

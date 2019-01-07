package com.example.demo.ThreadDomain;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EvenGenerator extends IntGenerator {
    private int currentEvenVal = 0;
    //AtomicInteger原子操作Int类型，其他基本类型同上
    private AtomicInteger atmoicCurrentEvent = new AtomicInteger(0);
    private Lock lock = new ReentrantLock();
    @Override
    //synchronized
//    public  int next() {
//        try{
//            lock.lock();
//            ++ currentEvenVal;
//            Thread.yield();
//            ++ currentEvenVal;
//            return currentEvenVal;
//        }finally {
//            lock.unlock();
//        }
//
//    }
    //synchronized
    public  int next() {
        try{
            atmoicCurrentEvent.addAndGet(2);
//            Thread.yield();
//            atmoicCurrentEvent.addAndGet(1);
            return atmoicCurrentEvent.get();
        }finally {
           // lock.unlock();
        }

    }

//    public int getCurrentEvenVal() {
//        return currentEvenVal;
//    }
    public int getCurrentEvenVal() {
        return atmoicCurrentEvent.get();
    }

    public static void main(String [] agrs){
        EvenChecker.test(new EvenGenerator());
    }
}

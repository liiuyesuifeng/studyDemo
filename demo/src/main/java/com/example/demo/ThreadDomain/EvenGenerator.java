package com.example.demo.ThreadDomain;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EvenGenerator extends IntGenerator {
    private int currentEvenVal = 0;
    private Lock lock = new ReentrantLock();
    @Override
    //synchronized
    public  int next() {
        try{
            lock.lock();
            ++ currentEvenVal;
            Thread.yield();
            ++ currentEvenVal;
            return currentEvenVal;
        }finally {
            lock.unlock();
        }

    }

    public int getCurrentEvenVal() {
        return currentEvenVal;
    }

    public static void main(String [] agrs){
        EvenChecker.test(new EvenGenerator());
    }
}

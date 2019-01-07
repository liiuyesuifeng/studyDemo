package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程临界区（在某个区域加锁）
 * @auto lhr
 */
public class CriticalSection {

    static  void testApproaches(PairManager pman1,PairManager pman2){
        ExecutorService exec = Executors.newCachedThreadPool();
        PairManipulator pm1 = new PairManipulator(pman1);
        PairManipulator pm2 = new PairManipulator(pman2);
        PairChecker pCheck1 = new PairChecker(pman1);
        PairChecker pCheck2 = new PairChecker(pman2);
        exec.execute(pm1);
        exec.execute(pm2);
        exec.execute(pCheck1);
        exec.execute(pCheck2);
        try{
            TimeUnit.MILLISECONDS.sleep(500);
        }catch (InterruptedException ite){
            ite.printStackTrace();
            PrintUtil.print("Sleep interrupted");
        }
        PrintUtil.print("pm1" + pm1 + " \npm2" + pm2);
        System.exit(0);


    }
    public static void main(String [] args){
        PairManager pm1 = new PairManager1();
        PairManager pm2 = new PairManager2();
        testApproaches(pm1,pm2);
    }
}
class Pair{
    private int x;
    private int y;
    public Pair(int x,int y){
        this.x = x;
        this.y = y;
    }
    public Pair(){}
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void incrementX(){
        x++;
    }
    public void incrementY(){
        y++;
    }

    @Override
    public String toString() {
        return "X ：" + x + "    Y : " + y;
    }
    public class PairValuesNotEqualException extends RuntimeException{
        public PairValuesNotEqualException(){
            super();
        }
    }
    public void checkState(){
        if(x != y){
            throw new PairValuesNotEqualException();
        }
    }
}
abstract class PairManager{
    AtomicInteger checkCount = new AtomicInteger(0);
    protected  Pair pair = new Pair();
    private List<Pair> storage = Collections.synchronizedList(new ArrayList<>());
    public synchronized Pair getPair(){
        return new Pair(pair.getX(),pair.getY());
    }
    protected void store(Pair p){
        storage.add(p);
        try{
            TimeUnit.MILLISECONDS.sleep(50);
        }catch (InterruptedException ite){
            ite.printStackTrace();
        }
    }
    public abstract void increment();
}
class PairManager1 extends PairManager{

    @Override
    public synchronized void increment() {
        pair.incrementX();
        pair.incrementY();
        store(getPair());
    }
}
class PairManager2 extends PairManager{
    @Override
    public void increment() {
        Pair temp;
        synchronized (this){
            pair.incrementX();
            pair.incrementY();
            temp = getPair();
        }
        store(temp);
    }
}
class PairManipulator implements Runnable{
    private PairManager pm;
    public PairManipulator(PairManager pm){
        this.pm = pm;
    }

    @Override
    public void run() {
        while(true){
            pm.increment();
        }
    }

    @Override
    public String toString() {
        return "pair :" + pm.getPair() + " checkCounter =" + pm.checkCount.get();
    }
}
class PairChecker implements Runnable{
    private PairManager pm;
    public PairChecker(PairManager pm){
        this.pm = pm;
    }
    @Override
    public void run() {
        while(true){
            pm.checkCount.incrementAndGet();
            pm.getPair().checkState();
        }
    }
}

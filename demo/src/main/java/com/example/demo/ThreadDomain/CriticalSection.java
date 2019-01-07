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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程临界区（在某个区域加锁）
 * 代码块加锁比整个方法加锁效率高
 * @auto lhr
 */
public class CriticalSection {

    static  void testApproaches(PairManager pman1,PairManager pman2,PairManager pman3,PairManager pman4){
        ExecutorService exec = Executors.newCachedThreadPool();
       // for(int i=0;i<10;i++){
            PairManipulator pm1 = new PairManipulator(pman1);
            PairManipulator pm2 = new PairManipulator(pman2);
            PairManipulator pm3 = new PairManipulator(pman3);
            PairManipulator pm4 = new PairManipulator(pman4);
            PairChecker pCheck1 = new PairChecker(pman1);
            PairChecker pCheck2 = new PairChecker(pman2);
            PairChecker pCheck3 = new PairChecker(pman3);
            PairChecker pCheck4 = new PairChecker(pman4);
            exec.execute(pm1);
            exec.execute(pm2);
            exec.execute(pm3);
            exec.execute(pm4);
            exec.execute(pCheck1);
            exec.execute(pCheck2);
            exec.execute(pCheck3);
            exec.execute(pCheck4);
            try{
                TimeUnit.MILLISECONDS.sleep(500);
            }catch (InterruptedException ite){
                ite.printStackTrace();
                PrintUtil.print("Sleep interrupted");
            }
            PrintUtil.print(" pm1" + pm1 + " \tpm2" + pm2 + "\t pm3"+pm3 );
     //   }


        System.exit(0);


    }
    public static void main(String [] args){
        PairManager pm1 = new PairManager1("pm1");
        PairManager pm2 = new PairManager2("pm2");
        PairManager pm3 = new PairManager3("pm3");
        PairManager pm4 = new PairManager3("pm4");
        testApproaches(pm1,pm2,pm3,pm4);

    }
}
class Pair{
    private int x;
    private int y;
    private String name;
    public Pair(int x,int y){
        this.x = x;
        this.y = y;
    }
    public Pair(String name){
        this.name = name;
    }
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
        return "name :"+ name + "X ：" + x + "    Y : " + y;
    }
    public class PairValuesNotEqualException extends RuntimeException{
        public PairValuesNotEqualException(){
            super("error" + Pair.this);
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
    protected String name;
    protected  Pair pair = new Pair(name);
    private List<Pair> storage = Collections.synchronizedList(new ArrayList<>());
    public PairManager(String name){
        this.name = name;
        PrintUtil.print(name);
    }
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
    public String getName(){
        return name;
    }
    public abstract void increment();
}
class PairManager1 extends PairManager{
    public PairManager1(String name){
        super(name);
    }
    @Override
    public synchronized void increment() {
        pair.incrementX();
        pair.incrementY();
        store(getPair());
    }
}
class PairManager2 extends PairManager{
    public PairManager2(String name){
        super(name);
    }
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
class PairManager3 extends PairManager{
    public PairManager3(String name){
        super(name);
    }
    private  Lock lock = new ReentrantLock();
    //不加synchronized关键字，X和Y 不相同校验抛出异常
    @Override
    public void increment() {
        lock.lock();
        try{
            pair.incrementX();
            pair.incrementY();
            store(getPair());
        }finally {
            lock.unlock();
        }
    }
}
class PairManager4 extends PairManager{
    public PairManager4(String name){
        super(name);
    }
    private  Lock lock = new ReentrantLock();
    @Override
    public void increment() {
        Pair temp;
        lock.lock();
        try{
            pair.incrementX();
            pair.incrementY();
            temp = getPair();
        }finally {
            lock.unlock();
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
            PrintUtil.print(toString() + "\t\t" + Thread.currentThread().getName());
        }
    }

    @Override
    public String toString() {
        return "name : " + pm.getName() +"pair :" + pm.getPair() + " checkCounter =" + pm.checkCount.get();
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

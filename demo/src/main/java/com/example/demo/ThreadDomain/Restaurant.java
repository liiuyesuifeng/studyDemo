package com.example.demo.ThreadDomain;

import com.utils.PrintUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 线程协作，生产者和消费者
 * @Eg : 餐馆厨师和服务员
 */

/**
 * 餐厅
 */
public class Restaurant {
    Meal meal;
    ExecutorService exec = Executors.newCachedThreadPool();
    //服务员：消费者
    WaitPerson waitPerson = new WaitPerson(this);
    //厨师 ： 生产者
    Chef chef = new Chef(this);
    BusBoy busBoy = new BusBoy(this);
    public Restaurant(){
        exec.execute(chef);
        exec.execute(waitPerson);
        exec.execute(busBoy);
    }
    public static void main(String [] args){
        new Restaurant();
    }
}

/**
 * 食物
 */
class Meal{
    //编号
    private final int orderNum;
    private volatile boolean waitFood = false;
    public Meal(int orderNum){
        this.orderNum = orderNum;
    }
    public void setWaitFood(){
        waitFood = true;
    }
    public boolean getWaitFood(){
        return waitFood;
    }
    @Override
    public String toString() {
        return "Meal " + orderNum;
    }
}

/**
 * 服务员
 */
class WaitPerson implements Runnable{
    private Restaurant restaurant;
    public WaitPerson(Restaurant s){
        this.restaurant = s;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    //判断是否有餐
                    while (restaurant.meal == null || restaurant.meal.getWaitFood() == true) {
                        //等待
                        wait();
                    }
                }
                //服务员有餐
                PrintUtil.print("WaitPerson got" + restaurant.meal);
                //锁定厨师
                synchronized (restaurant.busBoy){
                    restaurant.meal.setWaitFood();
                    //通知清理食物
                    restaurant.busBoy.notify();
                }
            }
        } catch (InterruptedException ie) {
            PrintUtil.print("WaitPerson Interrupted");
        }
    }
}

/**
 * 厨师
 */
class Chef implements Runnable{
    private Restaurant restaurant;
    private int count = 0;

    public Chef(Restaurant r) {
        this.restaurant = r;
    }

    @Override
    public void run() {
        try{
            //判断当前线程状态是否中断
            while(!Thread.interrupted()){
                synchronized (this){
                    //食物是否被端走
                    while(restaurant.meal != null){
                        //等待
                        wait();
                    }

                }
                //判断生产总数是否 == 10
                if(count ++ == 10){
                    //食物没有，窗口关闭
                    PrintUtil.print(" out of food ,closing");
                    //停止所有线程
                    restaurant.exec.shutdownNow();
                    /*
                    加上return后 右面代码将不会运行
                    return ;
                     */
                }
                //订单来了
                PrintUtil.print("Order up!");
                //锁定服务员
                synchronized (restaurant.waitPerson){
                    //放入食物
                    restaurant.meal = new Meal(count);
                    //通知服务员有餐
                    restaurant.waitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        }catch (InterruptedException ie){
            PrintUtil.print("Chef interrupted ");
        }
    }
}
class BusBoy implements Runnable{
    private Restaurant restaurant;
    public BusBoy(Restaurant s){
        this.restaurant = s;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    //判断食物是否被消费
                    while (restaurant.meal == null) {
                        wait();
                    }
                }
                //食物已经被消费
                PrintUtil.print("Clean up the" + restaurant.meal);
                synchronized (restaurant.chef){
                    //清理食物
                    restaurant.meal = null;
                    restaurant.chef.notifyAll();
                }
            }
        } catch (InterruptedException e) {

        }
    }
}
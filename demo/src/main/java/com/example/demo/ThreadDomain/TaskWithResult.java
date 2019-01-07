package com.example.demo.ThreadDomain;

import java.util.concurrent.Callable;

public class TaskWithResult implements Callable<String> {
    private int id ;
    public TaskWithResult(){}
    public TaskWithResult(int id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "result String to" + id;
    }

    @Override
    public String call() throws Exception {
        return "result String to" + id;
    }

}

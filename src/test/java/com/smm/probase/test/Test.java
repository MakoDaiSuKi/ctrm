package com.smm.probase.test;

import java.util.concurrent.*;

/**
 * Created by zhenghao on 2016/7/5.
 *
 *
 */
public class Test {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(2);
        //创建两个有返回值的任务
        Callable c1 = new MyCallable("A");
        Callable c2 = new MyCallable("B");

        System.out.println("--开启线程");

        //执行任务并获取Future对象
        Future f1 = pool.submit(c1);
        Future f2 = pool.submit(c2);

        //从Future对象上获取任务的返回值，并输出到控制台
        System.out.println(">>>"+f1.get().toString());
        System.out.println(">>>"+f2.get().toString());
        //关闭线程池
        pool.shutdown();


        System.out.println("--执行完成");
    }

}



class MyCallable implements Callable {
    private String oid;

    MyCallable(String oid) {
        this.oid = oid;

        int max = 100;

        for(int i=0;i<max;i++){

            System.out.println(i+"---- "+ oid);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object call() throws Exception {
        return oid+"任务返回的内容";
    }
}
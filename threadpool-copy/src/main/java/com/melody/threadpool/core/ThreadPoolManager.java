package com.melody.threadpool.core;

import java.util.ArrayList;

/**
 *  线程池管理器（PoolManager）:用于创建并管理线程池，采用单例模式
 *  @author zqhuangc
 */
public class ThreadPoolManager {

    public static volatile ThreadPoolManager mPool;

    public final int max_pool = 2;

    public final int max_Tasks = 15;

    public static ArrayList<Worker> init_pools;
    // 空闲线程轮询间隔时间
    private int idleThreadPollTime = 50;
    //任务监测线程
    private TaskMonitor monitor;

    private ThreadPoolManager(){}

    static {
        init_pools = new ArrayList<>(1);
    }

    public static ThreadPoolManager getInstance() {
        synchronized (ThreadPoolManager.class){
            if (mPool == null) {
                mPool = new ThreadPoolManager();
            }

            return mPool;
        }

    }


    //获取空闲线程
    public  Worker getIdleThread(){
        Worker working = null;
        while(true){
            synchronized(init_pools){
                for (int i = 0; i < max_pool; i++) {
                    //Worker working = init_pools.get(i);
                    working = init_pools.get(i);
                    if (!working.isrunning) {
                        // System.out.println("工作将由闲置线程" + working.getThreadTag() + "执行");
                        return working;
                    }
                }
            }
            try {
                Thread.sleep(5000);//放弃CPU,若干时间后重新获取空闲线程
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

    public void init(int num) {
        System.out.println("线程池初始化开始。。。");
        Worker worker = null;
        int max_pool = num < this.max_pool ? this.max_pool : num;
        for (int i = 0; i < max_pool; i++) {
            worker = new Worker("initThread"+i);
            init_pools.add(worker);
            worker.start();
        }
        monitor = new TaskMonitor();
        monitor.start();
        System.out.println("结束初始化线程池...");

    }


}

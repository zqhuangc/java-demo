package com.melody.threadpool.core;

/**
 * 任务监控：检测空闲线程并检测待运行任务
 * @author zqhuangc
 */
public final class TaskMonitor extends Thread {
    //private ThreadPoolManager threadPool;
    private int GetWorkTaskPollTime = 10;// 监测任务轮询时间，可配置
    /*public TaskMonitorThread(PoolManage pool) {
        System.out.println("正在创建任务监测线程...");
        this.threadPool = pool;
    }*/

    public TaskMonitor() {
        System.out.println("正在创建任务监测线程...");
    }

    @Override
    public void run() {

        while (true) {

            try {
                WorkTask task = TaskManager.getTask();
                if (task == null) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    Worker t = ThreadPoolManager.getInstance().getIdleThread();// 获取空闲线程
                    System.out.println("Worker.toString()=============================>?"+t.toString());
                    if (t == null) {
                        break;
                    }
                    t.setWorkTask(task);// 设置线程任务
                    System.out.println("task.toString()=============================>?"+task.toString());
                    t.setIsRunning(true);//激活空闲线程
                    System.out.println("Worker.toString()=============================>?"+t.getIsrunning());
                    //try {
                    //    Thread.sleep(GetWorkTaskPollTime);
                    //}
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

package com.melody.threadpool.core;

/**
 * 工作线程（WorkThread）: 线程池中线程
 * @author zqhuangc
 */
public class Worker extends Thread {

    public boolean isrunning = false;

    private WorkTask currentTask; // 当前任务

    private Object threadTag;// 线程标识

    // private int state;// 线程状态

    public Worker(Object key) {
        System.out.println("正在创建工作线程...线程编号" + key.toString());
        this.threadTag = key;
        // this.state=STATE_CREATE;
    }

    /**
     * 获取线程标识key
     * @return 线程标识
     */
    public Object getThreadTag() {
        return threadTag;
    }

    public synchronized void setWorkTask(WorkTask task) {
        this.currentTask = task;
    }

    public synchronized void setIsRunning(boolean flag) {
        this.isrunning = flag;
        if (flag) {
            this.notify();
        }
    }



    public boolean getIsrunning() {
        return isrunning;
    }

    public synchronized void run() {
        System.out.println("工作线程：" +  this.getThreadTag()  + "初始化成功");
        while (true) {
            if (!isrunning) {
                try {
                    System.out.println("工作线程：" + this.getThreadTag() + " 任务完成回归线程池");
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("线程被阻挡");
                    e.printStackTrace();
                }
            } else {
                //try {
                currentTask.runTask();
                setIsRunning(false);
                System.out.println("工作线程：" +this.getThreadTag()  + " 开始工作");
                //this.sleep(3000);
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}

                //this.notify();
                //break;
            }
        }
    }
}

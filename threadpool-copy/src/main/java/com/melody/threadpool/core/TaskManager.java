package com.melody.threadpool.core;



import java.util.LinkedList;

/**
 * 任务管理器:管理任务队列
 * @author zqhuangc
 */
public class TaskManager {

    public static LinkedList<WorkTask> workqueue =new LinkedList<>();// 缓冲队列

    /**
     * 向工作队列中加入一个任务，由工作线程去执行该任务
     * @param task 工作任务
     */
    public synchronized static void addTask(WorkTask worktask) {
        if (worktask != null && workqueue.size() < 15) {
            workqueue.add(worktask);
        }
    }


    /**
     * 从工作队列中取出一个任务
     *
     * @return 待执行任务
     * @throws InterruptedException
     */
    public synchronized static WorkTask getTask() throws InterruptedException {
        if (workqueue.size() > 0) {
            return workqueue.removeFirst();
        }
        return null;
    }
}

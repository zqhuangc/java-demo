package com.melody.threadpool.core;

/**
 * 任务接口：实现它来定义自己具体的工作任务
 * @author zqhuangc
 */
public interface WorkTask {

    /**
     * 执行工作任务
     */
    void runTask();

    /**
     * 取消工作任务
     */
    void cancelTask();

    int getProgress();
}

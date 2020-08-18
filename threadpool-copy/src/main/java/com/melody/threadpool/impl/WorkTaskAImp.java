package com.melody.threadpool.impl;

import com.melody.threadpool.core.WorkTask;

/**
 *
 * 任务类 A
 * 正常执行的工作任务
 * @author zqhuangc
 */

public class WorkTaskAImp implements WorkTask {

    protected String param;

    public WorkTaskAImp(){}

    public WorkTaskAImp(String param){
        this.param = param;
    }

    @Override
    public void runTask() {
        // Log.v("=============>Task1", this.param);
        System.out.println("=============>WorkTaskAImp "+ this.param);
    }

    @Override
    public void cancelTask() {

    }

    @Override
    public int getProgress() {
        return 0;
    }

}
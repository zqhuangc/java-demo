package com.melody.threadpool.impl;

import com.melody.threadpool.core.WorkTask;

/**
 * 任务类: 正常执行的工作任务
 * @author zqhuangc
 */
public class WorkTaskImp implements WorkTask {

    protected String param;

    public WorkTaskImp(){
    }

    public WorkTaskImp(String param){
        this.param=param;
    }

    @Override
    public void runTask() {
        System.out.println("=============>WorkTaskImp:"+this.param);
    }

    @Override
    public void cancelTask() {

    }

    @Override
    public int getProgress() {
        return 0;
    }

}

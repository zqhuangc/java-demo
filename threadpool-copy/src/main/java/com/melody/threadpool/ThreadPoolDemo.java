package com.melody.threadpool;

import com.melody.threadpool.core.TaskManager;
import com.melody.threadpool.core.ThreadPoolManager;
import com.melody.threadpool.impl.WorkTaskAImp;
import com.melody.threadpool.impl.WorkTaskImp;

/**
 * 测试
 * @author zqhuangc
 */
public class ThreadPoolDemo {

    public static void main(String[] args) {
        ThreadPoolManager instance = ThreadPoolManager.getInstance();
        instance.init(1);
        TaskManager.addTask(new WorkTaskImp());
        TaskManager.addTask(new WorkTaskAImp("sss"));

    }
}

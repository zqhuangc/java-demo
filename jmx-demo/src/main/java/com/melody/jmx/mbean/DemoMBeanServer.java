package com.melody.jmx.mbean;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * TODO
 * @author zqhuangc
 */
public class DemoMBeanServer {
    public static void main(String[] args) throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.melody.jmx.mbean:type=Test");
        mBeanServer.registerMBean(new Demo(), objectName);
        System.out.println("----------- started");
        Thread.sleep(Long.MAX_VALUE);
    }
}

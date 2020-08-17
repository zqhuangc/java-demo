package com.melody.jmx.dynamic;

import javax.management.DynamicMBean;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.lang.management.ManagementFactory;

/**
 * TODO
 * @author zqhuangc
 */
public class DynamicMBeanServer {

    public static void main(String[] args) throws Exception {

        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // domain:com.melody.jmx.dynamic
        //
        ObjectName objectName = new ObjectName("com.melody.jmx.dynamic:type=Data");

        DataMBean dataMBean = new DataMBean();
        DynamicMBean dynamicMBean = new StandardMBean(dataMBean, Data.class);
        mBeanServer.registerMBean(dynamicMBean, objectName);

        System.out.println("----------- started");
        Thread.sleep(Long.MAX_VALUE);
    }
}

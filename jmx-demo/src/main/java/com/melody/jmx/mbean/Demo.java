package com.melody.jmx.mbean;  

/**
 *  TODO
 * @author zqhuangc
 */
public class Demo implements DemoMBean{

    @Override
    public String test() {
        return "just test";
    }
}

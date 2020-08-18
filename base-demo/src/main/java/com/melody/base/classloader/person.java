package com.melody.base.classloader;

/**
 * @author zqhuangc
 */
public class person  {

    public person() {
        System.out.println("person is loaded by " + this.getClass().getClassLoader());
        new User();
    }

}

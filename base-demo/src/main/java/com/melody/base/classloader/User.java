package com.melody.base.classloader;

/**
 * @author zqhuangc
 */
public class User {

    public User() {
        System.out.println("User is loaded by " + this.getClass().getClassLoader());
    }

    public void say(){
        System.out.println("this is the busness");
    }
}

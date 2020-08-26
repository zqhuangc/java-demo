package com.melody.java.nio.selector;


import java.nio.channels.Selector;


/**
 * SelectorD demo
 * @author zqhuangc
 * @see Selector
 */
public class SelectorDemo {

    private final static Selector SELECTOR = null;

    public static Selector createSelector() throws Exception {
        // 实际调用：SelectorProvider.provider().openSelector();
        if(SELECTOR == null){
            return Selector.open();
        }
        return SELECTOR;
    }

    public static void main(String[] args) {
        try {
            Selector selector = SelectorDemo.createSelector();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

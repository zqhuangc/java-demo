package com.melody.java.nio.selector;


import java.nio.channels.Selector;


/**
 * @Description: TODO
 * @author: melody_wongzq
 * @since: 2020/6/16
 * @see
 */
public class SelectorManagement {

    private final static Selector SELECTOR = null;

    public static Selector createSelector() throws Exception {
        // 实际调用：SelectorProvider.provider().openSelector();
        if(SELECTOR == null){
            return Selector.open();
        }
        return SELECTOR;
    }

}

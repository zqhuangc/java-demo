package com.melody.base.newfeature.java9;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * @author zqhuangc
 */
public class VarHandleDemo {
	
	public static void main(String[] args) {

	}
	
	private static void testVarHandle() throws Exception{
		VarHandle varHandle = MethodHandles.lookup().findVarHandle(Integer.class, "", Long.class);
		varHandle.compareAndSet("");
	}
	
	private static void testUnsafe() {
		// TODO Auto-generated method stub
		//Unsafe.getUnsafe();//有安全问题
	}
}

package com.melody.base.newfeature.java9;

import java.util.stream.Stream;

/**
 * java 9
 * @author zqhuangc
 */
public class StackFrameDemo {
	
	public static void main(String[] args) {
		// Java 9 前
		echoStackTraceElement();	
		
		// Java 9
		echoStackWalker();
		
	}
	
	
	private static void echoStackWalker() {
		System.out.println("echoStackWalker():");
		StackWalker.getInstance().forEach(System.out::println);
	}
	
	private static void echoStackTraceElement() {
		System.out.println("echoStackTraceElement():");
		Stream.of(Thread.currentThread().getStackTrace()).forEach(System.out::println);;
	}
}

package com.melody.base.newfeature.java9;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @author zqhuangc
 */
public class ProcessHandleDemo {
	
	public static void main(String[] args) {
		echoAllProcess();
		echoCurrentProcessOnExit();
		echoOnExit();
	}
	
	private static void echoCurrentProcessOnExit() {
		// TODO Auto-generated method stub
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				//no gurantee
				long pid = ProcessHandle.current().pid();// Java 9
				// TODO Auto-generated method stub
				RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
				String value = runtimeMXBean.getName(); // value = pid@xxx
				String pidString = value.substring(0,value.indexOf("@"));
				System.out.printf("Current Process[pid:%d，pidString:%s] will on exicted",pid,pidString);
			}
			
		});
	}
	
	private static void echoAllProcess() {
		ProcessHandle.allProcesses().forEach(System.out::println);;
	}
	
	private static void echoOnExit() {
		ProcessHandle.current().onExit().thenAccept(System.out::println);
	}
}

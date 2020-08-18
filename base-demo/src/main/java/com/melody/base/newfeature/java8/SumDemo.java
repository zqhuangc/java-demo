package com.melody.base.newfeature.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

/**
 * @author zqhuangc
 */
public class SumDemo {
	
	//Java5 面向过程方式
	public static int sumInJava5(int size) {

		List<Integer> values = range(1, size);
		// 变量参数（Java 5）
	    int sum = 0;
	    Iterable<Integer> integers = values;// 泛型（Java 5）
		for (Integer value : integers) { // 迭代语句 Iterable 作为参数（Java 5）
			sum += value; // Unboxing Integer -> int （Java 5）  这里的计算过程不是面向对象的
		}
		return sum;
	}
	
	//Java5 面向对象方式
	public static int sumInJava5(int size,Sum sum) {

		List<Integer> values = range(0, size);
		// 变量参数（Java 5）
	    int result = 0;
	    Iterable<Integer> integers = values;// 泛型（Java 5）
		for (Integer value : integers) { // 迭代语句 Iterable 作为参数（Java 5）
			result = sum.calc(result, value); // Unboxing Integer -> int （Java 5） 
		}
		return result;
	}
	
	public static void main(String[] args) {
		//int sum = sumInJava5(10);
		//int sum = sumInJava5(10, SumDemo::calcurate);
		//int sum = sumInJava8(9);
		
		int sum = sumInJava8(10, Integer::sum);
		
		System.out.println(sum);
	}
	
	// Java 8 面向函数方式
	// 通过面向对象的方式（Function 是接口）
	// 语法补充，你可以执行代码或者方法引用
	public static int sumInJava8(int size,BinaryOperator<Integer> function) {

		//List<Integer> values = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		List<Integer> values = range(0, size);
		int sum = 0;
		// values = [1,2,3,4,5,6,7,8,9,10]
		// Stream 流式
		// values = [(1,2),3,4,5,6,7,8,9,10]
		// => sum(1,2),3,4,5,6,7,8,9,10
		// => sum(3,3),4,5,6,7,8,9,10
		// Stream API
		// 方法引用
		return values.stream().reduce(sum, function);
	}
	
	public static int sumInJava8(int size) {
		// TODO Auto-generated method stub
		List<Integer> values = range(0, size);
		int result = 0;
		return values.stream().reduce(result, Integer::sum);
	}
	
	public static int calcurate(int a,int b) {
		return a + b;
	}
	
	public static List<Integer> range(int since,int count) {
		List<Integer> values = new ArrayList<Integer>(count);
		for (int i = since; i < since + count; i++) {
			values.add(i);
		}
		return values;
	}
	
	
	public interface Sum{
		/**
		 * 计算方法
		 * @param a 左值
		 * @param b 右值
		 * @return 计算值
		 */
		int calc(int a, int b);
	}
	
	private static class SumImp implements Sum{

		@Override
		public int calc(int a, int b) {

			return a + b;
		}
		
	}	
}

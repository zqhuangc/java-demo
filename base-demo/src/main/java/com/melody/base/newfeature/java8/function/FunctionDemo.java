package com.melody.base.newfeature.java8.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * 函数设计 Demo
 * @author zqhuangc
 */
public class FunctionDemo {
	
	public static void main(String[] args) {
		//demo(ArrayList::new);
		//demo(FunctionDemo::oneToTen); // new ArrayList();
		demoFlatMap();// 1,2,3,4,5
		
	}
	
	private static void demoFlatMap() {
		//.flatMapToInt(value -> { })     // 降维
		Arrays.asList("1,2","3,4,5")
		        .stream()
				// 一维变二维
		        .map(value -> value.split(","))
				// 二维降到一维
				.flatMap(Arrays::stream)
				//.flatMap(array -> Arrays.stream(array))
		        .forEach(System.out::println);
		
		System.out.println(Arrays.asList("1,2","3,4,5")
				.stream()
				// 一维变二维
				.map(value -> value.split(","))
				// 二维降到一维
				.flatMap(array -> Arrays.stream(array))
				.map(Integer::valueOf)
				.reduce(0, Integer::sum)
		);
	}
	
	private static List<Integer> oneToTen() {
		return range(1, 10);
	}
	
	private static void demo(Supplier<List<Integer>> supplier) {
		// 1-10 累加
		//List<Integer> values = range(1, 10);
		List<Integer> values = supplier.get();
		System.out.println(values.stream().reduce(0,Integer::sum));
		// 1 - 10 打印
		// Consumer -> 只接收数据，不返回（消费数据）
		//values.stream().forEach(System.out::println);
		//values.stream().forEach(value ->{System.out.println(value);});
		
		// 1 - 10 挑选偶数
		// Predicate -> 判断
		// Function -> 转换 （Integer -> Integer * 2）
		// Supplier -> 没有接受数据，只有返回（提供数据）,引用方法和构造器
		
		//values.stream().filter(value -> value % 2 == 0).forEach(System.out::println);		
		
		values.stream()
		          .filter(value -> value % 2 == 0)  // Predicate：过滤偶数
		          .map(value -> value * 2)          // Function：将偶数乘以 2（convert）
		          
		          .forEach(System.out::print);      // Consumer
	}
	
	public static List<Integer> range(int since,int count) {
		List<Integer> values = new ArrayList<Integer>(count);
		for (int i = since; i < since + count; i++) {
			values.add(i);
		}
		return values;
	}
}

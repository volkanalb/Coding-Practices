package com.vvv.stack02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		List<KeyValue<Boolean, Runnable>> condition1Map = new ArrayList<KeyValue<Boolean, Runnable>>();
		condition1Map.add(new KeyValue(Boolean.TRUE, new ARunnable(0)));
		condition1Map.add(new KeyValue(Boolean.TRUE, new BRunnable()));
//		condition1Map.add(new KeyValue(Boolean.FALSE, new ARunnable(0)));
//		condition1Map.add(new KeyValue(Boolean.FALSE, new CRunnable()));
		
		condition1Map.stream().filter(k->k.key.equals(condition1())).forEach(k->k.value.run());;
		

		List<KeyValue<Boolean, Runnable>> condition2Map = new ArrayList<KeyValue<Boolean, Runnable>>();
		condition2Map.add(new KeyValue(Boolean.TRUE, new ARunnable(0)));
		condition2Map.add(new KeyValue(Boolean.TRUE, new CRunnable()));
//		condition2Map.add(new KeyValue(Boolean.FALSE, new ARunnable(0)));
//		condition2Map.add(new KeyValue(Boolean.FALSE, new CRunnable()));
		

		condition2Map.stream().filter(k->k.key.equals(condition2())).forEach(k->k.value.run());;
		
		
	}

	private static Boolean condition1() {
		return Boolean.FALSE;
	}

	private static Boolean condition2() {
		return Boolean.TRUE;
	}

	private static class KeyValue<T, U> {
		private T key;
		private U value;

		public KeyValue(T key, U value) {
			this.key = key;
			this.value = value;
		}
	}

	private static class ARunnable implements Runnable {
		Integer aParameter;

		public ARunnable(Integer aParameter) {
			this.aParameter = aParameter;
		}

		@Override
		public void run() {
			doSomethingA(this.aParameter);
		}

		private static void doSomethingA(Integer innerParameter) {
			System.out.println("Doing A with " + innerParameter);
		}

	}

	private static class BRunnable implements Runnable {

		@Override
		public void run() {
			doSomethingB();
		}

		private static void doSomethingB() {
			System.out.println("Doing B");
		}

	}

	private static class CRunnable implements Runnable {

		private static void doSomethingC() {
			System.out.println("Doing C");
		}

		@Override
		public void run() {
			doSomethingC();
		}

	}
}

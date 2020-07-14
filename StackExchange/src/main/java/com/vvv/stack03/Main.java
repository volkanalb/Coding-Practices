package com.vvv.stack03;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		int[] numbers = { 1, 1, 2, 1, 5 };
		System.out.println(F5(numbers));
	}

	public static boolean F5(int[] numbers) {
		return !Arrays.stream(numbers).anyMatch(n -> n < 0 || n > 4);
	}

	public static boolean F5temp(int[] numbers) {

		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] < 0 || numbers[i] >= 4)
				return false;
		}
		return true;
	}
}

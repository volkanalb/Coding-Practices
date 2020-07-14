package vvv.kata;

import java.util.ArrayList;
import java.util.List;

public class RemovedNumbers {

	public static List<long[]> removNb(long n) {

		List<long[]> finalList = new ArrayList<long[]>();

		long totalSum = n * (n + 1) / 2;

		for (int i = 1; i <= n; i++) {
			double x = ((double) totalSum - (double) i) / ((double) i + 1d);
			if (x % 1 == 0 && x <= n) {
				long[] tata = {i,(long)x};
				finalList.add(tata);
			}
		}
		return finalList;
	}
}

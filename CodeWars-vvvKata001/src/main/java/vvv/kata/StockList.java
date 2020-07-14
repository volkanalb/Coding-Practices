package vvv.kata;

import java.util.*;
import java.util.stream.*;
import java.util.Map;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.joining;

public class StockList {
	// 1st parameter is the stocklist (L in example),
	// 2nd parameter is list of categories (M in example)
	public static String stockSummary(String[] lstOfArt, String[] lstOf1stLetter) {
		// your code here
		StringBuilder stockStringBuilder = new StringBuilder();
		if (lstOfArt.length > 0 && lstOf1stLetter.length > 0) {
			for (int i = 0; i < lstOf1stLetter.length; i++) {
				final String firstLetter = lstOf1stLetter[i];
				Stream<String> lstOfArtStream = Arrays.stream(lstOfArt);
				Stream<String> lstOfArtStreamFiltered = lstOfArtStream
						.filter(s -> s.substring(0, 1).equals(firstLetter));
				List<String> lstOfArtFilteredStockAmounts = lstOfArtStreamFiltered.collect(Collectors.toList());
				lstOfArtFilteredStockAmounts.replaceAll(s -> s = s.split(" ")[1]);
				List<Integer> lstOfArtFilteredInt = lstOfArtFilteredStockAmounts.stream().map(Integer::valueOf)
						.collect(Collectors.toList());
				int sum = lstOfArtFilteredInt.stream().mapToInt(j -> j).sum();
				stockStringBuilder.append("(" + firstLetter + " : " + sum + ")");
				if (lstOf1stLetter.length > (i + 1)) {
					stockStringBuilder.append(" - ");
				}
			}
		}
		return stockStringBuilder.toString();
	}

	private static int stockCount(final String s) {
		return Integer.valueOf(s.split(" ")[1]);
	}

	public static String stockSummaryBest(final String[] stock, final String[] categories) {
		if (stock.length == 0 || categories.length == 0)
			return "";
		final Map<String, Integer> counts = stream(stock)
				.collect(groupingBy(s -> s.substring(0, 1), summingInt(StockList::stockCount)));
		return stream(categories).map(s -> "(" + s + " : " + counts.getOrDefault(s, 0) + ")").collect(joining(" - "));
	}
}

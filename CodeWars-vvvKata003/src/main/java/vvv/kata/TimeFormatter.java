package vvv.kata;

import java.util.stream.IntStream;

public class TimeFormatter {

	public static String formatDuration(int seconds) {
		String returnValue = "";
		if (seconds <= 0) {
			returnValue = "now";
		} else {

			int second = seconds % 60;
			int minute = (seconds / 60) % 60;
			int hour = (seconds / (60 * 60)) % 24;
			int day = (seconds / (60 * 60 * 24)) % 365;
			int year = seconds / (60 * 60 * 24 * 365);

			returnValue = getHumanReadableFormat(year, "year");
			returnValue += getAndOrComma(returnValue, second, minute, hour, day);

			returnValue += getHumanReadableFormat(day, "day");
			returnValue += getAndOrComma(returnValue, second, minute, hour);

			returnValue += getHumanReadableFormat(hour, "hour");
			returnValue += getAndOrComma(returnValue, second, minute);

			returnValue += getHumanReadableFormat(minute, "minute");
			returnValue += getAndOrComma(returnValue, second);

			returnValue += getHumanReadableFormat(second, "second");

			System.out.println(returnValue);
		}
		return returnValue;
	}

	private static String getHumanReadableFormat(int value, String durationName) {
		return (value > 0 ? (value + " " + durationName + getPlural(value)) : "");
	}

	private static String getPlural(int value) {
		return (value > 1 ? "s" : "");
	}

	private static String getAndOrComma(String currentString, int... nextDurations) {
		if (currentString.isEmpty())
			return "";

		long count = IntStream.of(nextDurations).filter(i -> i > 0).count();
		if (count == 0L)
			return "";

		if (count == 1L)
			return " and ";

		return ", ";
	}

}

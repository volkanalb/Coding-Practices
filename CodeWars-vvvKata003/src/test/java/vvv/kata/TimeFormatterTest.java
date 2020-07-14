package vvv.kata;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeFormatterTest {

	@Test
	public void testNow() {
		assertEquals("now", TimeFormatter.formatDuration(0));
	}

	@Test
	public void test1Second() {
		assertEquals("1 second", TimeFormatter.formatDuration(1));
	}
	
	@Test
	public void testMultiSeconds() {
		assertEquals("45 seconds", TimeFormatter.formatDuration(45));
		assertEquals("1 minute and 1 second", TimeFormatter.formatDuration(61));
		assertEquals("1 minute", TimeFormatter.formatDuration(60));
        assertEquals("1 minute and 2 seconds", TimeFormatter.formatDuration(62));
	}

}

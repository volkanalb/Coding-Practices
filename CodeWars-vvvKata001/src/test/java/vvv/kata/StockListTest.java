package vvv.kata;

import static org.junit.Assert.*;

import org.junit.Test;

import vvv.kata.StockList;

public class StockListTest {

	@Test
	public void test1() { // Ex 1: (A : 200) - (B : 1140)
		String art[] = new String[] { "ABAR 200", "CDXE 500", "BKWR 250", "BTSQ 890", "DRTY 600" };
		String cd[] = new String[] { "A", "B" };
		assertEquals("(A : 200) - (B : 1140)", StockList.stockSummary(art, cd));
	}

	@Test
	public void test2() { // Ex 2: (A : 0) - (B : 1290) - (C : 515) - (D : 600)
		String art[] = new String[] { "BBAR 150", "CDXE 515", "BKWR 250", "BTSQ 890", "DRTY 600" };
		String cd[] = new String[] { "A", "B", "C", "D" };
		assertEquals("(A : 0) - (B : 1290) - (C : 515) - (D : 600)", StockList.stockSummary(art, cd));
	}

	@Test
	public void test3() { // Ex 3: (A : 0) - (B : 114) - (C : 70) - (W : 0)
		String art[] = new String[] { "CBART 20", "CDXEF 50", "BKWRK 25", "BTSQZ 89", "DRTYM 60" };
		String cd[] = new String[] { "A", "B", "C", "W" };
		assertEquals("(A : 0) - (B : 114) - (C : 70) - (W : 0)", StockList.stockSummary(art, cd));
	}

	@Test
	public void test4() { // Ex 4:(B : 364) - (R : 225) - (D : 60) - (X : 0)
		String art[] = new String[] { "ROXANNE 102", "RHODODE 123", "BKWRKAA 125", "BTSQZFG 239", "DRTYMKH 060" };
		String cd[] = new String[] { "B", "R", "D", "X" };
		assertEquals("(B : 364) - (R : 225) - (D : 60) - (X : 0)", StockList.stockSummary(art, cd));
	}

	@Test
	public void test5() { // Ex 4:(B : 364) - (R : 225) - (D : 60) - (X : 0)
		String art[] = new String[] {};
		String cd[] = new String[] { "B", "R", "D", "X" };
		assertEquals("", StockList.stockSummary(art, cd));
	}

	@Test
	public void test6() { // Ex 4:(B : 364) - (R : 225) - (D : 60) - (X : 0)
		String art[] = new String[] { "ROXANNE 102", "RHODODE 123", "BKWRKAA 125", "BTSQZFG 239", "DRTYMKH 060" };
		String cd[] = new String[] {};
		assertEquals("", StockList.stockSummary(art, cd));
	}
}

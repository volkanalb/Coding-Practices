package vvv.kata;

public class MainApp {

	public static void main(String[] args) {
		System.out.println("Hello to Kata 001!!");
		String art[] = new String[] { "ABAR 200", "CDXE 500", "BKWR 250", "BTSQ 890", "DRTY 600" };
		String cd[] = new String[] { "A", "B" };
		System.out.println(StockList.stockSummary(art, cd));
	}

}

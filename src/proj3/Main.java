package proj3;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		CityPopAtLeast40PercentOperator cityPop = new CityPopAtLeast40PercentOperator(true);
		
		cityPop.open();
		cityPop.getNext();
		cityPop.close();
	}
}
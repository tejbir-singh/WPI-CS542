package proj3;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		CityCountryPopulationComparisonOperator cityPop = new CityCountryPopulationComparisonOperator(false);
		
		cityPop.open();
		cityPop.getNext();
		cityPop.close();
	}
}
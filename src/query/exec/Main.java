package query.exec;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		CityCountryPopulationComparisonOperator cityPop = new CityCountryPopulationComparisonOperator(false);
		
		cityPop.open();
		//cityPop.getNext();
		cityPop.getNextBySortedCountry();
		cityPop.close();
	}
}
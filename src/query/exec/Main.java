package query.exec;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws IOException {
		ArrayList<String> results;
		int flag = 0;
		CityCountryPopulationComparisonOperator cityPop = new CityCountryPopulationComparisonOperator();
		long startTime = System.currentTimeMillis();
		cityPop.open();
		cityPop.getNext();
		//Alternative method: cityPop.getNextAlternative();
		results = cityPop.close();

		// output the total runtime
		System.out.println("Time: " + (System.currentTimeMillis()-startTime) + " ms.");
		
		// for testing
		String[] expected = {"\"Città del Vaticano\"","\"Victoria\"","\"Nassau\"","\"Adamstown\"","\"Gibraltar\"",
				"\"Dalap-Uliga-Darrit\"","\"Djibouti\"","\"Bantam\"","\"Longyearbyen\"","\"El-Aaiún\"","\"Avarua\"",
				"\"George Town\"","\"Stanley\"","\"Saint-Pierre\"","\"Singapore\"","\"Koror\"","\"Macao\"","\"Doha\""
		};
		
		for (String exp : expected) {
			exp = new String(exp.toString().getBytes("UTF-8"), "UTF-8");
			flag = 1;
			for (String res : results) {
				res = new String(res.toString().getBytes("UTF-8"), "UTF-8");
				if (res.toString().equals(exp.toString())) {
					flag = 0;
					System.out.println(res);
					break;
				}
			}
			if (flag == 1) {
				System.out.println("ERROR: Unexpected result.");
				return;
			}
		}
		System.out.println("SUCCESS: Returned expected results.");
	}
}
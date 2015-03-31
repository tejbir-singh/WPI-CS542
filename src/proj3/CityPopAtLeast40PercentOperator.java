package proj3;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;


public class CityPopAtLeast40PercentOperator {
	Relation country, city;
	Relation results = new Relation("results.store");
	
	// read the .csv files into the Relation objects
	public void open() throws IOException {
		country = new Relation("country.store");
		city = new Relation("city.store");
		//String tuple;
		/*
		BufferedReader br = new BufferedReader(new FileReader("City3.csv"));
		
		while ((tuple = br.readLine()) != null) {
			city.put(tuple.getBytes("UTF-8").hashCode(), tuple.getBytes("UTF-8"));
		}
		br.close();*/
		/*
		BufferedReader br = new BufferedReader(new FileReader("country.csv"));
		while ((tuple = br.readLine()) != null) {
			country.put(tuple.getBytes("UTF-8").hashCode(), tuple.getBytes("UTF-8"));
		}
		br.close();*/
	}
	
	public void getNext() throws UnsupportedEncodingException {
		// iterate through Country and City
		Enumeration<byte[]> countryEnum = country.getValuesEnum();
		while (countryEnum.hasMoreElements()) {
			String[] countryValues = getTupleValues(countryEnum.nextElement());
			Enumeration<byte[]> cityEnum = city.getValuesEnum();
			
			while (cityEnum.hasMoreElements()) {
				String[] cityValues = getTupleValues(cityEnum.nextElement());
				
				/*	check whether or not they match based on the data provided,
					then check if city pop. > 40% country pop. */
				if (countryValues[0].equals(cityValues[2])) {
					Double ci = Double.parseDouble(cityValues[4].substring(1, cityValues[4].length()-1));
					Double co = Double.parseDouble(countryValues[6].substring(1, countryValues[6].length()-1));
					if (ci/co > .4) {
						// add it to the result set
						results.put(cityValues[1].getBytes("UTF-8").hashCode(), cityValues[1].getBytes("UTF-8"));
					}	
				}
			}
		}
	}
	
	// print the results
	public void close() throws UnsupportedEncodingException {
		Enumeration<byte[]> resultsEnum = results.getValuesEnum();
		System.out.println("RESULTS");
		while (resultsEnum.hasMoreElements()) {
			System.out.println(new String(resultsEnum.nextElement(), "UTF-8"));
		}
	}
	
	// convert a given byte array into a string array
	public String[] getTupleValues(byte[] tuple) throws UnsupportedEncodingException {
		String str = new String(tuple, "UTF-8");
		return str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // ignores commas inside quotation marks
	}
}

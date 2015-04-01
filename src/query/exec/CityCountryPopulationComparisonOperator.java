package query.exec;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;

public class CityCountryPopulationComparisonOperator {
	Relation country, city;
	Relation results = new Relation("results.store", true);
	private boolean initialize;
	
	public CityCountryPopulationComparisonOperator(boolean initialize) {
		this.initialize = initialize; 
	}
	
	
	// read the .csv files into the Relation objects
	public void open() throws IOException {
		country = new Relation("country.store", true);
		city = new Relation("city.store", true);
		
		if (initialize) {
			initializeRelations();
		}
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
		// delete the saved results table; we don't need it anymore
		Path p = Paths.get("results.store");
		try {
			Files.delete(p);
		} catch (IOException e) {
			// do nothing
		}
	}
	
	// convert a given byte array into a string array
	public String[] getTupleValues(byte[] tuple) throws UnsupportedEncodingException {
		String str = new String(tuple, "UTF-8");
		return str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // ignores commas inside quotation marks
	}

	private void initializeRelations() throws UnsupportedEncodingException,
	FileNotFoundException, IOException {
		String tuple;
		// read city.csv and country.csv using UTF-8 character encoding
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("City3.csv"), "UTF8"));
		while ((tuple = br.readLine()) != null) {
			city.put(tuple.getBytes("UTF-8").hashCode(), tuple.getBytes("UTF-8"));
		}
		br.close();

		br = new BufferedReader(new InputStreamReader(new FileInputStream("country.csv"), "UTF8"));
		while ((tuple = br.readLine()) != null) {
			country.put(tuple.getBytes("UTF-8").hashCode(), tuple.getBytes("UTF-8"));
		}
		br.close();
	}
}
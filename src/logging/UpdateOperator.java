package logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class UpdateOperator {
	Relation country, city;
	Relation results = new Relation("results.store", false);

	// read the .csv files into the Relation objects
	public void open() throws IOException {
		country = new Relation("country.store", true);
		city = new Relation("city.store", true);
		initializeRelations();
	}

	public void getNext() throws UnsupportedEncodingException {
		// iterate through Country and City, increasing each population by 2%
		String newPopulation, oldValue = "";
		byte[] modifiedValues;
		for (Integer i : country.getRidList()) {
			String[] countryValues = getTupleValues(country.get(i));
			oldValue = Double.valueOf(countryValues[6].substring(1, countryValues[6].length() - 1)).toString();
			newPopulation = updatePopulation(
					Double.parseDouble(countryValues[6].substring(1, countryValues[6].length() - 1))).toString();
			countryValues[6] = "\"" + newPopulation + "\"";
			modifiedValues = unsplit(countryValues);
			// log the modification
			country.getLog().add(new LogElement("T", "" + i, oldValue, countryValues[6]));
			country.remove(i);
			country.put(i, modifiedValues);
		}
		
		// modify city population
		for (Integer i : city.getRidList()) {
			String[] cityValues = getTupleValues(city.get(i));
			oldValue = Double.valueOf(cityValues[4].substring(1, cityValues[4].length() - 1)).toString();
			newPopulation = updatePopulation(
					Double.parseDouble(cityValues[4].substring(1, cityValues[4].length() - 1))).toString();
			cityValues[4] = "\"" + newPopulation + "\"";
			modifiedValues = unsplit(cityValues);
			// Log the modification
			city.getLog().add(new LogElement("T", "" + i, oldValue, cityValues[4]));
			city.remove(i);
			city.put(i, modifiedValues);
		}
	}

	public void close() throws UnsupportedEncodingException {
		// TODO: Commit
		city.getLog().add(new LogElement("COMMIT", "", "", ""));
		city.saveContents();
		country.getLog().add(new LogElement("COMMIT", "", "", ""));
		country.saveContents();
		System.out.println("Successfully increased all populations by 2%.");
	}

	// Convert a given byte array into a string array
	public String[] getTupleValues(byte[] tuple) throws UnsupportedEncodingException {
		String str = new String(tuple, "UTF-8");
		return str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // ignores commas inside quotation marks
	}

	/**
	 * Reconnect all array elements as a comma-delimited string and convert back
	 * to byte array.
	 * 
	 * @param array
	 *            to reconnect and convert to byte array
	 * @return byte array consisting of the parameter's information
	 * @throws UnsupportedEncodingException
	 */
	private byte[] unsplit(String[] splitArray)
			throws UnsupportedEncodingException {
		String retString = "";
		for (String str : splitArray) {
			retString = retString + "," + str;
		}
		return retString.getBytes("UTF-8");
	}

	// If the storage files do not exist, run this to generate the tables from
	// the .csv files
	private void initializeRelations() throws IOException {
		String tuple;
		// read city.csv and country.csv using UTF-8 character encoding
		File file = new File("country.store");
		if (file.exists()) {
			return;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("city.csv"), "UTF8"));
		while ((tuple = br.readLine()) != null) {
			city.put(tuple.getBytes("UTF-8").hashCode(),
					tuple.getBytes("UTF-8"));
		}
		br.close();

		br = new BufferedReader(new InputStreamReader(new FileInputStream(
				"country.csv"), "UTF8"));
		while ((tuple = br.readLine()) != null) {
			country.put(tuple.getBytes("UTF-8").hashCode(),
					tuple.getBytes("UTF-8"));
		}
		br.close();
	}

	/**
	 * Small helper function to increase the given double by 2%.
	 * @param population to manipulate
	 * @return updated population
	 */
	private Double updatePopulation(Double pop) {
		return pop.doubleValue() * 1.02;
	}
}

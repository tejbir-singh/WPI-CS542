package logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import logging.Relation;

public class Main {
	private static final int CityPopulationIdx = 4;
	private static final int CountryPopulationIdx = 6;
	static Relation country, countryOld, city, cityOld;
	
	
	public static void main(String[] args) throws IOException {
		initializeRelations();
		
		long startTime = System.currentTimeMillis();
		UpdateOperator cityPop = new UpdateOperator(city, 4);
		UpdateOperator countryPop = new UpdateOperator(country, 6);
		
		cityPop.open();
		cityPop.getNext();
		cityPop.close();
		System.out.println("City: Successfully increased all populations by 2%");
		
		countryPop.open();
		countryPop.getNext();
		countryPop.close();
		System.out.println("Country: Successfully increased all populations by 2%");
		
		// Apply the logs to countryOld and cityOld
		ApplyLogsToStore.updateDataStore("city.store.log", cityOld, CityPopulationIdx);
		ApplyLogsToStore.updateDataStore("country.store.log", countryOld, CountryPopulationIdx);
		
		// Verify that the stores are now equivalent
		for (int k : country.getKeysArray()) {
			if (!Arrays.equals(countryOld.get(k), country.get(k))) {
				System.out.println("Error: Relations do not match");
				return;
			}
		}
		
		for (int k : city.getKeysArray()) {
			if (!Arrays.equals(cityOld.get(k), city.get(k))) {
				System.out.println("Error: Relations do not match");
				return;
			}
		}
		
		// output the total runtime
		System.out.println("Logs successfully generated and applied to secondary relation.");
		System.out.println("Time: " + (System.currentTimeMillis()-startTime) + " ms.");
	}
	
	// If the storage files do not exist, run this to generate the tables from
	// the .csv files
	private static void initializeRelations() throws IOException {
		String tuple;
		// first start fresh; delete files if they exist
		removeOldFiles();
		
		country = new Relation("country.store", true);
		countryOld = new Relation("country.old.store", true);
		city = new Relation("city.store", true);
		cityOld = new Relation("city.old.store", true);
		
		// read city.csv and country.csv using UTF-8 character encoding
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("city.csv"), "UTF8"));
		while ((tuple = br.readLine()) != null) {
			byte[] byteArr = tuple.getBytes("UTF-8");
			int key = byteArr.hashCode();
			city.put(key, byteArr);
			cityOld.put(key, byteArr);
		}
		br.close();

		br = new BufferedReader(new InputStreamReader(new FileInputStream(
				"country.csv"), "UTF8"));
		while ((tuple = br.readLine()) != null) {
			byte[] byteArr = tuple.getBytes("UTF-8");
			int key = byteArr.hashCode();
			country.put(key, byteArr);
			countryOld.put(key, byteArr);
		}
		br.close();
		
		// Write the contents to .store files
		country.saveContents();
		countryOld.saveContents();
		city.saveContents();
		cityOld.saveContents();
	}
	
	// Small helper function to remove old files if they exist.
	private static void removeOldFiles() {
		try {
			File f = new File("city.store");
			if (f.exists()){ f.delete(); }
			
			f = new File("country.store");
			if (f.exists()){ f.delete(); }
			
			f = new File("city.old.store");
			if (f.exists()){ f.delete(); }
			
			f = new File("country.old.store");
			if (f.exists()){ f.delete(); }
			
			f = new File("city.store.log");
			if (f.exists()){ f.delete(); }
			
			f = new File("country.store.log");
			if (f.exists()){ f.delete(); }
		}catch(Exception e){
			// if any error occurs
			e.printStackTrace();
		}
	}
}
package logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import logging.Relation;

public class Main {
	static Relation country = new Relation("country.store", true);
	static Relation countryOld = new Relation("country.old.store", true);
	static Relation city = new Relation("city.store", true);
	static Relation cityOld = new Relation("city.old.store", true);
	
	public static void main(String[] args) throws IOException {
		initializeRelations();
		country.saveContents();
		countryOld.saveContents();
		city.saveContents();
		cityOld.saveContents();
		
		long startTime = System.currentTimeMillis();
		UpdateOperator cityPop = new UpdateOperator(city, 4);
		UpdateOperator countryPop = new UpdateOperator(country, 6);
		
		cityPop.open();
		cityPop.getNext();
		cityPop.close();
		
		countryPop.open();
		countryPop.getNext();
		countryPop.close();

		city.outputLog();
		country.outputLog();
		// output the total runtime
		System.out.println("Time: " + (System.currentTimeMillis()-startTime) + " ms.");
	}
	
	// If the storage files do not exist, run this to generate the tables from
	// the .csv files
	private static void initializeRelations() throws IOException {
		String tuple;
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
	}
}
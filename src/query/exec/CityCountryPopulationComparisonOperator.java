package query.exec;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;


public class CityCountryPopulationComparisonOperator{
	 
	public class CountryPop implements Comparable{
        String couPop[] = new String[2];
        public CountryPop(String x1, String x2){
        	couPop[0]=x1;
        	couPop[1]=x2;
        }

		@Override
		public int compareTo(Object o) {
			// TODO Auto-generated method stub
			CountryPop obj=(CountryPop) o;
			if(obj.couPop[0].compareTo(couPop[0])>0) return -1;
				else if(obj.couPop[0].compareTo(couPop[0])<0) return 1;
				else return 0;
		}	
	}
	 
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
		Enumeration<byte[]> cityEnum = city.getValuesEnum();
		while (cityEnum.hasMoreElements()) {
			String[] cityValues = getTupleValues(cityEnum.nextElement());
			
			Enumeration<byte[]> countryEnum = country.getValuesEnum();
			while (countryEnum.hasMoreElements()) {
				String[] countryValues = getTupleValues(countryEnum.nextElement());
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
	
	public void getNextBySortedCountry() throws UnsupportedEncodingException {
		int flag;
		ArrayList<CountryPop> sortedCountry = sortCountry();
		Enumeration<byte[]> cityEnum = city.getValuesEnum();
		while (cityEnum.hasMoreElements()) {
			String[] cityValues = getTupleValues(cityEnum.nextElement());
			
			flag = binarySearch(sortedCountry, 0, sortedCountry.size()-1, cityValues[2].substring(1, cityValues[2].length()-1));
			if (flag != -1){
				Double ci = Double.parseDouble(cityValues[4].substring(1, cityValues[4].length()-1));
				Double co = Double.parseDouble(sortedCountry.get(flag).couPop[1]);
				if (ci/co > .4) {
					// add it to the result set
					results.put(cityValues[1].getBytes("UTF-8").hashCode(), cityValues[1].getBytes("UTF-8"));
				}	
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<CountryPop> sortCountry() throws UnsupportedEncodingException {
		ArrayList<CountryPop> sortedCountry = new ArrayList<CountryPop>();
		Enumeration<byte[]> countryEnum = country.getValuesEnum();
		while (countryEnum.hasMoreElements()) {
			String[] countryValues = getTupleValues(countryEnum.nextElement());
			CountryPop o1 = new CountryPop(countryValues[0].substring(1, countryValues[0].length()-1),countryValues[6].substring(1, countryValues[6].length()-1));
			sortedCountry.add(o1);
		}
		Collections.sort(sortedCountry);
		
		return sortedCountry;
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
	
	private static int binarySearch(ArrayList<CountryPop> cp, int lowerbound, int upperbound, String key){
	    int position = ( lowerbound + upperbound) / 2;
	
	    while((cp.get(position).couPop[0].compareTo(key) != 0) && (lowerbound <= upperbound)){
	         if (cp.get(position).couPop[0].compareTo(key)>0){
	              upperbound = position - 1;  
	         }                                                            
	         else{                                                       
	              lowerbound = position + 1; 
	         }
	         position = (lowerbound + upperbound) / 2;
	     }
	     
	     if (lowerbound <= upperbound){
	           return position;
	     }
	     else{
	          return -1;
	     }
	}

}
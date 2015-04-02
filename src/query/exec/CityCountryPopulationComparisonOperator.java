package query.exec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;


public class CityCountryPopulationComparisonOperator {
	Relation country, city;
	Relation results = new Relation("results.store", false);
	
	// read the .csv files into the Relation objects
	public void open() throws IOException {
		country = new Relation("country.store", true);
		city = new Relation("city.store", true);
		
		initializeRelations();
	}
	
	public void getNextAlternative() throws UnsupportedEncodingException {
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
					Double cityPop = Double.parseDouble(cityValues[4].substring(1, cityValues[4].length()-1));
					Double countryPop = Double.parseDouble(countryValues[6].substring(1, countryValues[6].length()-1));
					if (cityPop/countryPop > .4) {
						// add it to the result set
						results.put(cityValues[1].getBytes("UTF-8").hashCode(), cityValues[1].getBytes("UTF-8"));
					}
					break;
				}
			}
		}
	}
	
	public void getNext() throws UnsupportedEncodingException {
		int flag;
		ArrayList<CountryPop> sortedCountry = sortCountry();
		Enumeration<byte[]> cityEnum = city.getValuesEnum();
		while (cityEnum.hasMoreElements()) {
			String[] cityValues = getTupleValues(cityEnum.nextElement());
			
			// find the matching country using binary search
			flag = binarySearch(sortedCountry, 0, sortedCountry.size()-1, cityValues[2].substring(1, cityValues[2].length()-1));
			if (flag != -1) {
				/*	check whether or not they match based on the data provided,
					then check if city pop. > 40% country pop. */
				Double cityPop = Double.parseDouble(cityValues[4].substring(1, cityValues[4].length()-1));
				Double countryPop = Double.parseDouble(sortedCountry.get(flag).couPop[1]);
				if (cityPop/countryPop > .4) {
					// add it to the result set
					results.put(cityValues[1].getBytes("UTF-8").hashCode(), cityValues[1].getBytes("UTF-8"));
				}	
			}
			
		}
	}
	
	/**
	 * @return Sorted ArrayList of the country table
	 * @throws UnsupportedEncodingException
	 */
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
	
	// output the results
	public ArrayList<String> close() throws UnsupportedEncodingException {
		ArrayList<String> resultList = new ArrayList<String>();
		Enumeration<byte[]> resultsEnum = results.getValuesEnum();
		System.out.println("RESULTS");
		while (resultsEnum.hasMoreElements()) {
			resultList.add(new String(resultsEnum.nextElement(), "UTF-8"));
		}
		return resultList;
	}
	
	// convert a given byte array into a string array
	public String[] getTupleValues(byte[] tuple) throws UnsupportedEncodingException {
		String str = new String(tuple, "UTF-8");
		return str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // ignores commas inside quotation marks
	}

	// If the storage files do not exist, run this to generate the tables from the .csv files
	private void initializeRelations() throws IOException {
		String tuple;
		// read city.csv and country.csv using UTF-8 character encoding
		File file = new File("country.store");
		if (file.exists()) {
			return;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("city.csv"), "UTF8"));
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
	
	// Standard binary search operation
	private static int binarySearch(ArrayList<CountryPop> cp, int lowerBound, int upperBound, String key) {
	    int position = (lowerBound + upperBound) / 2;
	
	    while((cp.get(position).couPop[0].compareTo(key) != 0) && (lowerBound <= upperBound)){
	         if (cp.get(position).couPop[0].compareTo(key)>0){
	              upperBound = position - 1;  
	         }                                                            
	         else{                                                       
	              lowerBound = position + 1; 
	         }
	         position = (lowerBound + upperBound) / 2;
	     }
	     
	     if (lowerBound <= upperBound){
	           return position;
	     }
	     else{
	          return -1;
	     }
	}
	
	public class CountryPop implements Comparable {
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
}

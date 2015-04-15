package logging;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ApplyLogsToOldStores {

	private static final int CityPopulationIdx = 4;
	private static final int CountryPopulationIdx = 6;
	
	public static void main(String[] args) throws IOException {
		updateDataStore("city.store.log", "city.old.store", CityPopulationIdx );
		updateDataStore("country.store.log", "country.old.store", CountryPopulationIdx);
	}

	/*
	 *  This method will take two files: log file and data store and apply the 
	 *  log file to update the data store
	 */
	private static void updateDataStore(String csvLogFile, String dstore, int populationIdx) throws FileNotFoundException, IOException {
		BufferedReader br = null;
		String[] tupleTokenized;
		String line = "";
		String csvSplitBy = ",";
		String hashValue, oldPopulationVal, newPopulationVal, tuple;
		int hashVal;
		byte[] oldTuple, updatedTuple;

		Relation oldDataStore = new Relation(dstore, true);
		try {
			br = new BufferedReader(new FileReader(csvLogFile));
				while ( (line = br.readLine()) != null) {
					String[] tokens = line.split(csvSplitBy);
					if (!tokens[0].equals("COMMIT"))
					{
						hashValue = tokens[1];
						oldPopulationVal = tokens[3];
						newPopulationVal = tokens[2];
						hashVal = Integer.parseInt(hashValue);

						oldTuple = oldDataStore.get(hashVal); 
						tupleTokenized = UpdateOperator.getTupleValues(oldTuple);
						// update the value of Tuple with new population value from log
						tupleTokenized[populationIdx] = newPopulationVal;
						oldDataStore.remove(hashVal);
						updatedTuple = UpdateOperator.unsplit(tupleTokenized);
						oldDataStore.put(hashVal, updatedTuple);
						System.out.println(tupleTokenized);
					}
				}
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
} /* end of class definition */


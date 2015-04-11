package logging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class UpdateOperator {
	Relation r;
	int populationIndex; 	// specifies the index within the tuple that 
							// contains the population data

	public UpdateOperator(Relation r, int populationIndex) {
		this.r = r;
		this.populationIndex = populationIndex;
	}
	
	public void open() throws IOException {
		// TODO: What do we do here?
	}

	public void getNext() throws UnsupportedEncodingException {
		// iterate through the relation, increasing each population by 2%
		byte[] modifiedValues;
		
		// modify city population
		for (Integer i : r.getKeysArray()) {
			String[] values = getTupleValues(r.get(i));
			long oldPop = Math.round(Double.valueOf(
					values[populationIndex].substring(1, values[populationIndex].length() - 1)));
			values[populationIndex] = "\"" + updatePopulation(oldPop) + "\"";
			modifiedValues = unsplit(values);
			// Log the modification
			r.getLog().add(new LogElement("T", "" + i, "" + oldPop, values[populationIndex]));
			r.remove(i);
			r.put(i, modifiedValues);
		}
	}

	public void close() throws UnsupportedEncodingException {
		r.getLog().add(new LogElement("COMMIT", "", "", ""));
		r.saveContents();
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
		String retString = null;
		for (String str : splitArray) {
			if (retString == null) {
				retString = str;
			}
			else {
				retString = retString + "," + str;
			}
		}
		return retString.getBytes("UTF-8");
	}

	/**
	 * Small helper function to increase the given double by 2%.
	 * @param population to manipulate
	 * @return updated population
	 */
	private long updatePopulation(long pop) {
		return Math.round(pop * 1.02);
	}
}

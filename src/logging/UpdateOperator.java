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
		// All loading is done in the main function.
	}

	public void getNext() throws UnsupportedEncodingException {
		// iterate through the relation, increasing each population by 2%
		byte[] modifiedValues;
		
		// modify city population
		for (Integer i : r.getKeysArray()) {
			String[] values = LoggingUtil.getTupleValues(r.get(i));
			long oldPop = Math.round(Double.valueOf(
					values[populationIndex].substring(1, values[populationIndex].length() - 1)));
			values[populationIndex] = "" + updatePopulation(oldPop);
			modifiedValues = LoggingUtil.unsplit(values);
			// Log the modification
			r.getLog().add(new LogElement("T", "" + i, values[populationIndex], "" + oldPop));
			r.remove(i);
			r.put(i, modifiedValues);
		}
	}

	public void close() throws UnsupportedEncodingException {
		r.getLog().add(new LogElement("COMMIT", "", "", ""));
		r.saveContents();
		r.outputLog();
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

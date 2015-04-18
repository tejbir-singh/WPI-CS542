package logging;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ApplyLogsToStore {
	/**
	 *  This method will take the log file and apply it to the data store. 
	 *  @param logFile log file location
	 *  @param r Relation to update
	 *  @param populationIdx index of the population in the tuple
	 */
	public static void updateDataStore(String logFile, Relation r, int populationIdx) throws FileNotFoundException, IOException {
		BufferedReader br = null;
		String line = "";
		int hashKey;
		byte[] updatedTuple;
		String[] tupleTokenized;
		byte[] oldTuple;

		try {
			br = new BufferedReader(new FileReader(logFile));
			while ( (line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				if (!tokens[0].equals("COMMIT")) {
					hashKey = Integer.parseInt(tokens[1]);
					oldTuple = r.get(hashKey); 
					tupleTokenized = LoggingUtil.getTupleValues(oldTuple);

					// update the value of Tuple with new population value from log
					tupleTokenized[populationIdx] = tokens[2];
					r.remove(hashKey);
					updatedTuple = LoggingUtil.unsplit(tupleTokenized);
					r.put(hashKey, updatedTuple);
				}
			}
		}
		catch(IOException e) {
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
}


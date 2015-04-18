package logging;

import java.io.UnsupportedEncodingException;

/**
 *	This class contains helper functions for the logging operations.
 */
public class LoggingUtil {
	/**
	 * Convert a given byte array into a string array
	 * @param tuple byte array containing the original tuple  
	 * @return formatted String array
	 * @throws UnsupportedEncodingException
	 */
	protected static String[] getTupleValues(byte[] tuple) throws UnsupportedEncodingException {
		String str = new String(tuple, "UTF-8");
		return str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // ignores commas inside quotation marks
	}

	/**
	 * Reconnect all array elements as a comma-delimited string and convert back
	 * to byte array.
	 * 
	 * @param array to reconnect and convert to byte array
	 * @return byte array consisting of the parameter's information
	 * @throws UnsupportedEncodingException
	 */
	protected static byte[] unsplit(String[] splitArray)
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
}

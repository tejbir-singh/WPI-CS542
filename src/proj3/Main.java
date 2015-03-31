package proj3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) throws IOException {
		CityPopAtLeast40PercentOperator cityPop = new CityPopAtLeast40PercentOperator();
		
		cityPop.open();
		cityPop.getNext();
		cityPop.close();
		// delete the saved results table
		Path p = Paths.get("results.store");
		try {
			Files.delete(p);
		} catch (IOException e) {
			// do nothing
		}
	}
}

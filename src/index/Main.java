package index;

import java.util.ArrayList;

public class Main {
	static Directory directory;
	
	
	public static void main(String[] args) {
		directory = new Directory();
		// for this test, we do not want to use persistent storage.
		initialize();
		
		directory.put("1", "4");   //"4".hashCode = 52
		directory.put("2", "4");
		directory.put("3", "4");
		directory.put("4", "4");
		directory.put("5", "8");   //"8".hashCode = 56
		directory.put("6", "8");
		directory.put("7", "8"); 
		directory.put("8", "8");
		directory.put("9", "11");  //"11".hashCode = 1568
		directory.put("10", "5");  //"5".hashCode = 53
		directory.put("11", "6");  //"6".hashCode = 54
		
		directory.put("8", "xinjiehao");
		
		ArrayList<String> results = directory.get("4");
		results = directory.get("8");
		results = directory.get("11");
		results = directory.get("6");
		directory.remove("11");
		results = directory.get("6");	
		
		directory.remove("1");
		results = directory.get("4");
		directory.remove("2");
		results = directory.get("4");
		directory.remove("5");
		results = directory.get("8");
	}
	
	private static void initialize() {
		directory.setDirectory(new ArrayList<Bucket>());
		directory.setGlobalDepth(2);
		for (int i = 0; i < 4; i++) {
			directory.getDirectory().add(new Bucket(Directory.initialLocalDepth));		
		}
	}
}

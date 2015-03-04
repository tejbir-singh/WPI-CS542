package index;

import java.util.ArrayList;

public class Main {
	static Directory directory;
	
	
	public static void main(String[] args) {
		directory = Directory.getInstance();
	
		directory.put("1", "4");
		directory.put("2", "4");
		directory.put("3", "4");
		directory.put("4", "4");
		directory.put("5", "8");
		directory.put("6", "8");
		directory.put("7", "8");
		directory.put("8", "8");
		directory.put("9", "11");
		directory.put("10", "5");
		directory.put("11", "6");
		
		
		
		
		ArrayList<String> results = directory.get("4");
		results = directory.get("8");
		results = directory.get("11");
		results = directory.get("6");
		directory.remove("11");
		results = directory.get("6");
		
	}
}

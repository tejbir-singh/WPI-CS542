package index;

import java.util.ArrayList;

public class Directory {
	private static Directory instance;
	private static int globalDepth = 2;
	private ArrayList<Bucket> directory;
	
	private Directory() {
		directory = new ArrayList<Bucket>();
		// initialize with initialGlobalDepth Buckets
		for (int i = 0; i < Math.pow(2, globalDepth); i++) {
			directory.add(new Bucket());
		}
	}
	
	// Singleton implementation
	public static Directory getInstance() {
		if (instance == null) {
			instance = new Directory();
		}
		return instance;
	}
	
	public void put(String rid, String attribValue) {
		// find the bucket
		// decide if it can fit
		// if not expand
		int location = attribValue.hashCode() % directory.size();
		Bucket b = directory.get(location);
		if (b.getContents()[b.getContents().length-1] != null) {
			// decide if split or expand
			if (b.getLocalDepth() == globalDepth) {
				expand(b);
			}
			else {
				splitBucket(b);
			}
			location = rid.hashCode() % directory.size();
			b = directory.get(location);
			b.addToBucket(rid);
		}
		else {				// the element will fit in the bucket
			b.addToBucket(rid);
		}
	}
	
	private void expand(Bucket b) {
		int overflowedIndex = 0;
		globalDepth++;
		
		for (int i = 0; i < directory.size(); i++) {
			if (directory.get(i) == b) {
				overflowedIndex = i;
			}
		}
		
		for (int i = 0; i < Math.pow(2, globalDepth-1); i++) {
			if (i == overflowedIndex + Math.pow(2, globalDepth-1)) {
				Bucket b2 = new Bucket();
				directory.add(b2);
				redistribute(b);
			}
			// if it's the directory which overflowed, add a new
			// otherwise, add a reference to an old bucket
			else {
				directory.add(directory.get(i));
			}
		}
		
		
		// call splitBucket
	}
	
	private void splitBucket(Bucket b) {
		
	}
	
	private void redistribute(Bucket b1) {
		for (String rid : b1.getContents()) {
			//put(rid);
		}
	}
}

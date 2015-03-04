package index;

import java.util.ArrayList;

public class Directory {
	private static Directory instance;
	private static int globalDepth = 2;
	private static int initialLocalDepth = 2;
	private ArrayList<Bucket> directory;
	
	private Directory() {
		directory = new ArrayList<Bucket>();
		// initialize with initialGlobalDepth Buckets
		for (int i = 0; i < Math.pow(2, globalDepth); i++) {
			directory.add(new Bucket(initialLocalDepth));
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
	
	public void get(String attribValue) {
		
	}
	
	private void expand(Bucket bucket) {
		int overflowedIndex = directory.indexOf(bucket);
		globalDepth++;
		bucket.incrementLocalDepth();
		
		for (int i = 0; i < Math.pow(2, globalDepth-1); i++) {
			// if it's the location which overflowed, add a new bucket
			// otherwise, add a reference to an old bucket

			if (i == overflowedIndex + Math.pow(2, globalDepth-1)) {
				Bucket b2 = new Bucket(bucket.getLocalDepth());
				directory.add(b2);
				redistribute(bucket);
			}
			else {
				directory.add(directory.get(i));
			}
		}
	}
	
	private void splitBucket(Bucket bucket) {
		bucket.incrementLocalDepth();
		// create a bucket at overflowedIndex + Math.pow(2, globalDepth-1)
		Double newBucketIndex = directory.indexOf(bucket) + Math.pow(2, globalDepth-1);
		directory.set(newBucketIndex.intValue(), new Bucket(bucket.getLocalDepth()));
		
		// redistribute items from the original bucket
		redistribute(bucket);
	}
	
	private void redistribute(Bucket b1) {
		String[] contents = b1.getContents();
		b1.setContents(new String[Bucket.bucketSize]);
		for (String rid : contents) {
			// TODO: how do we get back the data_value that was originally used to hash?
			// put(rid, ???);
		}
	}
}

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
				expand(b, rid, attribValue);
			}
			else {
				splitBucket(b, rid, attribValue);
			}
		}
		else {			// the element will fit in the bucket
			b.addToBucket(rid, attribValue);
		}		
	}
	
	public ArrayList<String> get(String attribValue) {
		ArrayList<String> results = new ArrayList<String>();
		int location = attribValue.hashCode() % directory.size();
		Bucket b = directory.get(location);
		System.out.println("\n\nglobalDepths = " + globalDepth);
		System.out.println("localDepths = " + b.getLocalDepth());
		System.out.println("location = " + location);
		// add the rid's which belong to results
		if (b.getContents() != null){
			for (IndexElement ie : b.getContents()) {
				if (ie != null && ie.attribValue == attribValue) {
					results.add(ie.rid);
				}
			}
			return results;
		}
		else {
			return null;
		}
		
		
	}
	
	public void remove(String rid) {
		// brute force deletion?
		for (Bucket b : directory) {
			for (IndexElement ie : b.getContents()) {
				if (ie.rid == rid) {
					b.removeFromBucket(rid);
					return;
				}
			}
		}
	}
	
	private void expand(Bucket bucket, String rid, String attribValue) {
		int overflowedIndex = directory.indexOf(bucket);
		globalDepth++;
		bucket.incrementLocalDepth();
		
		for (int i = directory.size(); i < Math.pow(2, globalDepth); i++) {
			// if it's the location which overflowed, add a new bucket
			// otherwise, add a reference to an old bucket

			if (i == overflowedIndex + Math.pow(2, globalDepth-1)) {
				Bucket b2 = new Bucket(bucket.getLocalDepth());
				directory.add(b2);
			}
			
			else {
				directory.add(directory.get(i - directory.size() + 1));
			}
		}
		redistribute(bucket, rid, attribValue);
	}
	
	private void splitBucket(Bucket bucket, String rid, String attribValue) {
		bucket.incrementLocalDepth();
		// create a bucket at overflowedIndex + Math.pow(2, globalDepth-1)
		Double newBucketIndex = directory.indexOf(bucket) + Math.pow(2, globalDepth-1);
		directory.set(newBucketIndex.intValue(), new Bucket(bucket.getLocalDepth()));
		
		// redistribute items from the original bucket
		redistribute(bucket, rid, attribValue);
	}
	
	private void redistribute(Bucket b1, String rid, String attribValue) {
		IndexElement[] contents = b1.getContents();
		b1.setContents(new IndexElement[Bucket.bucketSize]);
		for (IndexElement e : contents) {
			put(e.rid, e.attribValue);
		}
		put(rid, attribValue);
	}
}

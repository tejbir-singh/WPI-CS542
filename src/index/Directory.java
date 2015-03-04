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
			put(rid, attribValue);
		}
		else {			// the element will fit in the bucket
			b.addToBucket(rid, attribValue);
		}		
	}
	
	public ArrayList<String> get(String attribValue) {
		ArrayList<String> results = new ArrayList<String>();
		int location = attribValue.hashCode() % directory.size();
		Bucket b = directory.get(location);
		
		System.out.println("\n\nThe current globalDepths = " + globalDepth);
		System.out.println("Get attribute value = '" + attribValue + 
				"' in Bucket " + directory.indexOf(b) + " with localDepths = " + b.getLocalDepth());
		
		// add the rid's which belong to results
		if (b.getContents() != null){
			for (IndexElement ie : b.getContents()) {
				if (ie != null && ie.attribValue == attribValue) {
					results.add(ie.rid);
				}
			}
			
			if (results.isEmpty()){
				System.out.println("Oops..'" + attribValue + "' does not exist!");
			}
			else{
				System.out.print("rids: ");
				for (String rids: results){
					System.out.print("'" + rids + "'  ");
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
				if (ie != null && ie.rid == rid) {
					b.removeFromBucket(rid);
					System.out.println("\n\nRemove rid = '" + rid + "' from Bucket " + directory.indexOf(b) + " successfully!");
					return;
				}
			}
		}
	}
	
	private void expand(Bucket bucket) {
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
				directory.add(directory.get((int) (i - Math.pow(2, globalDepth-1))));
			}
		}
		redistribute(bucket);
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
		IndexElement[] contents = b1.getContents();
		b1.setContents(new IndexElement[Bucket.bucketSize]);
		for (IndexElement e : contents) {
			put(e.rid, e.attribValue);
		}
	}
}

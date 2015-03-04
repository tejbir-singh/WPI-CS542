package index;

import java.util.ArrayList;

/**
 *	Structure which manages and operates on a list of Buckets.
 */
public class Directory {
	private static Directory instance;
	private static int globalDepth = 2;
	private static int initialLocalDepth = 2;
	private ArrayList<Bucket> directory;
	
	/**
	 * Constructor.
	 */
	private Directory() {
		directory = new ArrayList<Bucket>();
		// initialize with initialGlobalDepth Buckets
		for (int i = 0; i < Math.pow(2, globalDepth); i++) {
			directory.add(new Bucket(initialLocalDepth));
		}
	}
	
	/** Singleton implementation. */
	public static Directory getInstance() {
		if (instance == null) {
			instance = new Directory();
		}
		return instance;
	}
	
	/**
	 * Put an entry into the index.
	 * The attribute value is hashed to determine which bucket to add the element to.
	 * If necessary, add a new Bucket or expand the directory.
	 * @param rid rid to index
	 * @param attribValue to index
	 */
	public void put(String rid, String attribValue) {
		// find the bucket
		// decide if it can fit
		// if not expand
		int location = Math.abs(attribValue.hashCode()) % directory.size();
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
			System.out.println("\nInsert ('" + rid + "', '" + attribValue + "') into Bucket "
					+ directory.indexOf(b) + " successfully!");
			System.out.println("Current globalDepth = " + globalDepth + ", Bucket"
					+ directory.indexOf(b) + "'s localDepth = " + b.getLocalDepth()); 
		}		
	}
	
	/**
	 * Retrieve the elements which match the given attribute value.
	 * @param attribValue attribute value to search for
	 * @return An ArrayList of elements which match the search criteria.
	 */
	public ArrayList<String> get(String attribValue) {
		ArrayList<String> results = new ArrayList<String>();
		int location = Math.abs(attribValue.hashCode()) % directory.size();
		Bucket b = directory.get(location);
		
		System.out.println("\n\nThe current globalDepth = " + globalDepth);
		System.out.println("Get attribute value = '" + attribValue + 
				"' in Bucket " + directory.indexOf(b) + " with localDepth = " + b.getLocalDepth());
		
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
	
	/**
	 * Remove the entry from the index.
	 * @param rid rid to remove from the index
	 */
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
	
	/**
	 * Expand the size of the directory.
	 * In doing so, we add a new Bucket where we need it and add a 
	 * reference to pre-existing Buckets where we do not.
	 * @param bucket Bucket which overflowed and caused the expansion
	 */
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
	
	/**
	 * Split the given bucket and redistribute its contents.
	 * @param bucket Bucket to split
	 */
	private void splitBucket(Bucket bucket) {
		bucket.incrementLocalDepth();
		// create a bucket at overflowedIndex + Math.pow(2, globalDepth-1)
		Double newBucketIndex = directory.indexOf(bucket) + Math.pow(2, globalDepth-1);
		directory.set(newBucketIndex.intValue(), new Bucket(bucket.getLocalDepth()));
		
		// redistribute items from the original bucket
		redistribute(bucket);
	}
	
	/**
	 * Redistribute the contents of the given Bucket.
	 * @param b1 Bucket to operate on
	 */
	private void redistribute(Bucket b1) {
		IndexElement[] contents = b1.getContents();
		b1.setContents(new IndexElement[Bucket.bucketSize]);
		for (IndexElement e : contents) {
			put(e.rid, e.attribValue);
		}
	}
}

package index;

/**
 * Class used to store indexed data.
 */
public class Bucket {
	public static final int bucketSize = 4;		// defines the bucket size used to create every bucket
	private IndexElement[] contents = new IndexElement[bucketSize];	// array containing IndexElements
	private int localDepth;						// local depth of this Bucket
	
	/** Constructor */
	public Bucket(int localDepth) {
		this.localDepth = localDepth;
	}
	
	/** 
	 * Add an element to this Bucket.
	 */
	public void addToBucket(String rid, String attribValue) {
		IndexElement ie = new IndexElement(rid, attribValue);
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] == null) {		// empty slot in the bucket
				contents[i] = ie;
				return;
			}
		}
	}
	
	/** 
	 * Remove a bucket from this Bucket.
	 */
	public void removeFromBucket(String rid) {
		for (int i = 0; i < contents.length; i++) {
			if (contents[i].rid == rid) {		// found the rid we are removing
				contents[i] = null;
				return;
			}
		}
	}

	// Getters and setters
	public IndexElement[] getContents() {
		return contents;
	}

	public void setContents(IndexElement[] contents) {
		this.contents = contents;
	}
	
	public int getLocalDepth() {
		return this.localDepth;
	}
	
	public void incrementLocalDepth() {
		this.localDepth++;
	}
}


/**
 *	Small data structure used to keep track of index entries.
 */
class IndexElement {
	String rid;
	String attribValue;
	
	public IndexElement(String rid, String attribValue) {
		this.rid = rid;
		this.attribValue = attribValue;
	}
}



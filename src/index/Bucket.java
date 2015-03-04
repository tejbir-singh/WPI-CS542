package index;

public class Bucket {
	public static final int bucketSize = 4;
	private IndexElement[] contents = new IndexElement[bucketSize];
	private int localDepth;
	
	public Bucket(int localDepth) {
		this.localDepth = localDepth;
	}
	
	public void addToBucket(String rid, String attribValue) {
		IndexElement ie = new IndexElement(rid, attribValue);
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] == null) {		// empty slot in the bucket
				contents[i] = ie;
				return;
			}
		}
	}
	
	public void removeFromBucket(String rid) {
		for (int i = 0; i < contents.length; i++) {
			if (contents[i].rid == rid) {		// found the rid we are removing
				contents[i] = null;
				return;
			}
		}
	}

	/**
	 * @return the contents
	 */
	public IndexElement[] getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
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

class IndexElement {
	String rid;
	String attribValue;
	
	public IndexElement(String rid, String attribValue) {
		this.rid = rid;
		this.attribValue = attribValue;
	}
}



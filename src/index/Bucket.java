package index;

public class Bucket {
	public static final int bucketSize = 4;
	private String[] contents = new String[bucketSize];
	private int localDepth;
	
	public Bucket(int localDepth) {
		this.localDepth = localDepth;
	}
	
	public void addToBucket(String rid) {
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] == null) {		// empty slot in the bucket
				contents[i] = rid;
				return;
			}
		}
	}
	
	public void removeFromBucket(String rid) {
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] == rid) {		// found the rid we are removing
				contents[i] = null;
				return;
			}
		}
	}

	/**
	 * @return the contents
	 */
	public String[] getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
	public void setContents(String[] contents) {
		this.contents = contents;
	}
	
	public int getLocalDepth() {
		return this.localDepth;
	}
	
	public void incrementLocalDepth() {
		this.localDepth++;
	}
}


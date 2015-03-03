package index;

public class Bucket {
	private static final int maxBucketSize = 4;
	private static final int initialLocalDepth = 2;
	
	private String[] contents = new String[maxBucketSize];
	private int localDepth = initialLocalDepth;
	
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
}


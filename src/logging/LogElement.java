package logging;

public class LogElement {
	String transaction, element, newValue, oldValue;

	/**
	 * Used to identify and keep track of each change made to the database.
	 * @param transaction transaction which made the change
	 * @param element element which was modified
	 * @param newValue value which the element was set to
	 * @param oldValue value which the element previously contained
	 */
	public LogElement(String transaction, String element, String newValue, String oldValue) {
		this.transaction = transaction;
		this.element = element;
		this.newValue = newValue;
		this.oldValue = oldValue;
	}
	
	@Override
	public String toString() {
		return "" + this.transaction + "," + this.element + "," + this.newValue + "," + this.oldValue;
	}
}
package logging;

public class LogElement {
	String transaction, element, newValue, oldValue;

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
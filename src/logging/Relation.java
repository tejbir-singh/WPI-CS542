package logging;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class Relation implements Serializable {
	private static final long serialVersionUID = -8534916903031691902L;
	private Hashtable<Integer, byte[]> data;
	private String fileLocation;
	private ArrayList<Integer> ridList = new ArrayList<Integer>();
	private final boolean persistent;
	private ArrayList<LogElement> log = new ArrayList<LogElement>();
	
	public Relation(String location, boolean persistent) {
		// first check if there is data to load
		this.fileLocation = location;
		this.persistent = persistent;
		try {
			ObjectInputStream objIn = new ObjectInputStream (new FileInputStream(fileLocation));
			this.data = (Hashtable<Integer, byte[]>) objIn.readObject();
			this.ridList = (ArrayList<Integer>) objIn.readObject();
			objIn.close();
		} catch (IOException | ClassNotFoundException e) {
			this.data = new Hashtable<Integer, byte[]>();
		}
	}
	
	public void put(int key, byte[] value) {
		// ensure that only one thread can perform the action at any given time
		synchronized(this) {
			data.put(key, value);
		}
	}
	
	public byte[] get(int key) {
		return data.get(key);
	}
	
	public void remove(int key) {
		// ensure that only one thread can perform the action at any given time
		synchronized(this) {
			data.remove(key);
		}
	}
	
	// TODO: Change to save contents only on COMMIT
	public void saveContents() {
		// Write object with ObjectOutputStream
		if (!this.persistent) {
			return;
		}
		ObjectOutputStream objOut;
		try {
			objOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
			// Write object out to disk
			objOut.writeObject(this.data);
			objOut.writeObject(this.ridList);
			objOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void clearData() { this.data = new Hashtable<Integer, byte[]>(); }
	public Enumeration<byte[]> getValuesEnum() {  return data.elements(); }
	public ArrayList<Integer> getRidList() {  return this.ridList; }
	public ArrayList<LogElement> getLog() { return this.log; }
}

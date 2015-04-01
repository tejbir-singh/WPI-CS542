package proj3;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

public class Relation implements Serializable {
	private static final long serialVersionUID = -8534916903031691902L;
	private Hashtable<Integer, byte[]> data;
	private String fileLocation;
	public final int testKey = 587276264;
	private final boolean persistent;
	
	public Relation(String location, boolean persistent) {
		// first check if there is data to load
		this.fileLocation = location;
		this.persistent = persistent;
		try {
			ObjectInputStream objIn = new ObjectInputStream (new FileInputStream(fileLocation));
			Object obj = objIn.readObject();
			data = (Hashtable<Integer, byte[]>) obj;
			objIn.close();
			
		} catch (IOException | ClassNotFoundException e) {
			this.data = new Hashtable<Integer, byte[]>();
			saveContents();
		}
	}
	
	public void put(int key, byte[] value) {
		// ensure that only one thread can perform the action at any given time
		synchronized(this) {
			data.put(key, value);
		}
		saveContents();
	}
	
	public byte[] get(int key) {
		return data.get(key);
	}
	
	public void remove(int key) {
		// ensure that only one thread can perform the action at any given time
		synchronized(this) {
			data.remove(key);
		}
		saveContents();
	}
	
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
			objOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void clearData() { this.data = new Hashtable<Integer, byte[]>(); }
	public Enumeration<byte[]> getValuesEnum() {  return data.elements(); };
}

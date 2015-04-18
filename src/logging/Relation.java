package logging;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class Relation implements Serializable {
	private static final long serialVersionUID = -8534916903031691902L;
	private Hashtable<Integer, byte[]> data;
	private String fileLocation;
	private final boolean persistent;
	private ArrayList<LogElement> log = new ArrayList<LogElement>();
	
	public Relation(String location, boolean persistent) {
		// first check if there is data to load
		this.fileLocation = location;
		this.persistent = persistent;
		try {
			ObjectInputStream objIn = new ObjectInputStream (new FileInputStream(fileLocation));
			this.data = (Hashtable<Integer, byte[]>) objIn.readObject();
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
	public ArrayList<Integer> getKeysArray() {  return new ArrayList<Integer> (data.keySet()); }
	public ArrayList<byte[]> getValuesArray() {  return new ArrayList<byte[]> (data.values()); }
	public ArrayList<LogElement> getLog() { return this.log; }
	
	/**
	 *  Output the log to a .log file.
	 */
	public void outputLog() {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileLocation + ".log", true)));
			for (LogElement le : log) {
				out.println(le.toString());
			}
			out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}

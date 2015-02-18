package store;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;

public class Store implements Serializable {
	private static final long serialVersionUID = -8534916903031691902L;
	private static Store instance;
	private Hashtable<Integer, byte[]> data;
	private final String fileLocation = "store.store";
	
	private Store() {
		// first check if there is data to load
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
	
	// Singleton implementation
	public static Store getInstance() {
		if (instance == null) {
			instance = new Store();
		}
		return instance;
	}
	
	public void put(int key, byte[] value) {
		// ensure that only one thread can perform the action at any given time
		synchronized(this) {
			byte[] t = "test multithreading".getBytes();
			
			// TODO fix
			// used to assert synchronization in tests
			if (value.equals(t)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
}

package store;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Main {
	static Store store;
	private final static String fileLocation = "store.store";
	
	public static void main(String[] args) {
		Path p = Paths.get(fileLocation);
		try {
			Files.delete(p);
		} catch (IOException e) {
			// do nothing
		}
		store = Store.getInstance();
		
		//putSimple(1);
		//putSimple();
		//putLarge();
		multiPut();
		//putAndGet();
		
		System.out.println("complete");
	}
	
	// put at specific location
	public static void putSimple(int key) {
		byte[] value = new byte[40];
		new Random().nextBytes(value);
		store.put(key, value);
	}
	
	public static void putTestArray(int key) {
		byte[] value = "test multithreading".getBytes();
		store.put(new Random().nextInt(), value);
	}
	
	// test put large (1 GB)
	// NOTE: may need to allocate extra memory to the program in order to perform this operation
	// could alternatively use a 1 GB file located on the hard drive
	public static void putLarge() {
		// byte[] value = new byte[1073741824];
		// new Random().nextBytes(value);
		try {
			ObjectInputStream objIn = new ObjectInputStream (new FileInputStream("large.test"));
			Object obj = objIn.readObject();
			objIn.close();
			store.put(new Random().nextInt(), (byte[]) obj);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// test concurrent use
	public static void multiPut() {
		Thread t1 = new Thread() { public void run() { putTestArray(new Random().nextInt()); } };
		Thread t2 = new Thread() { public void run() { putSimple(new Random().nextInt()); } };
		
		t1.start();
		//try {
			//Thread.sleep(1000);
		//} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		t2.start();
	}
	
	public static void getSimple() {
		store.get(1);
	}
	
	// could write a put and get for testing?
	public static void putAndGet() {
		Thread t1 = new Thread() { public void run() { putTestArray(new Random().nextInt()); } };
		Thread t2 = new Thread() { public void run() { getSimple(); } };
		
		t1.start();
		t2.start();
	}
}

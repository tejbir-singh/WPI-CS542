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
		store.clearData();
		putLarge();
		try {
			store.clearData();
			multiPut();
			store.clearData();
			putAndGet();
			store.clearData();
			putAndGetSuccessfully();
			store.clearData();
			removeAndGetSuccessfully();
			store.clearData();
			removeAndGet();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("-----Completed-----");
	}
	
	// put at specific location
	public static void putSimple(int key) {
		byte[] value = new byte[40];
		new Random().nextBytes(value);
		store.put(key, value);
	}
	
	public static void putTestArray(int key) {
		byte[] value = "test multithreading".getBytes();
		store.put(key, value);
	}
	
	// test put large (1 GB). This may take a while.
	// NOTE: This requires a file named "large.test" in the source directory to run
	public static void putLarge() {
		System.out.println("--- putLarge() ---");
		try {
			ObjectInputStream objIn = new ObjectInputStream (new FileInputStream("large.test"));
			Object obj = objIn.readObject();
			objIn.close();
			store.put(new Random().nextInt(), (byte[]) obj);
			System.out.println("putLarge() completed successfully");
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("file large.test not found");
		}
	}
	
	public static void getSimple(int key) {
		byte[] s = store.get(key);
		if (s == null) {
			System.out.println(Thread.currentThread().getName() + " found no entries at key " + key + " during get()");
		}
		else {
			System.out.println(Thread.currentThread().getName() + " successfully got " + s.toString() + " from key " + key);
		}
	}
	
	public static void removeSimple(int key) {
		store.remove(key);
	}
	
	// Test concurrent put()
	// t1 must wait until t0 completes even though it takes longer
	public static void multiPut() throws InterruptedException {
		System.out.println("-----    multiPut()   -----");
		Thread t0 = new Thread() { 
			public void run() { 
				this.setName("t0");		
				putTestArray(1); 
				System.out.println(this.getName() + " completed");
			}
		};

		Thread t1 = new Thread() { 
			public void run() { 
				this.setName("t1");
				putSimple(1); 
				System.out.println(this.getName() + " completed");
			} 
		};

		t0.start();
		t0.join();
		t1.start();
		t1.join();
	}
	
	// This test is used to show that get() is not blocked by an outstanding put()
	public static void putAndGet() throws InterruptedException {
		System.out.println("-----    putAndGet()   -----");
		// put
		Thread t0 = new Thread() {
			public void run() {
				this.setName("t0");
				putTestArray(1);
			}	
		};
		
		// get
		Thread t1 = new Thread() {	
			public void run() {
				this.setName("t1");
				getSimple(1);	
			} 	
		};

		t0.start();
		t1.start();
		t1.join();
	}	
	
	// This test is the same as putAndGet() except that it shows the circumstance where 
	// the put() terminates before the get()
	public static void putAndGetSuccessfully() throws InterruptedException {
		System.out.println("-----    putAndGetSuccessfully()   -----");
		// put
		Thread t0 = new Thread() {
			public void run() {
				this.setName("t0");
				putSimple(2);
			}
		};

		// get
		Thread t1 = new Thread() {	
			public void run() {
				this.setName("t1");
				getSimple(2);	
			} 	
		};

		t0.start();
		t0.join();
		t1.start();
		t1.join();
	}
	
	// get() does not need to wait for remove() to complete, therefore it
	// will successfully get() before remove() completes
	public static void removeAndGetSuccessfully() throws InterruptedException {
		System.out.println("-----    removeAndGetSuccessfully   -----");
		putTestArray(store.testKey);	// initialize with test data
		
		// remove
		Thread t0 = new Thread() {
			public void run() {
				this.setName("t0");
				removeSimple(store.testKey);
			}	
		};

		// get
		Thread t1 = new Thread() {	
			public void run() {
				this.setName("t1");
				getSimple(store.testKey);
			} 	
		};
		
		t0.start();
		t1.start();
		t1.join();
	}
	
	// In the event that a remove() operation does complete just before a get(),
	// the get() will not find anything.
	public static void removeAndGet() throws InterruptedException {
		System.out.println("-----    removeAndGet   -----");
		putTestArray(3);	// initialize with test data

		// remove
		Thread t0 = new Thread() {
			public void run() {
				this.setName("t0");
				removeSimple(3);
			}	
		};

		// get
		Thread t1 = new Thread() {	
			public void run() {
				this.setName("t1");
				getSimple(3);
			} 	
		};

		t0.start();
		t0.join();
		t1.start();
		t1.join();
	}
}

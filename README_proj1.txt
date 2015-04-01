Design
	The underlying data storage method for Store (Store.java) is a hash table. We use the the standard hash table methodology to access the values store, while imposing our own limitations and implementing a means for persistent storage. We used synchronized blocks as our locking mechanism.
	
	The database is backed up to persistent storage each time a value is added or removed. The file is stored in the source directory with the name "store.store". Upon loading, a Store object will first check to see if there is a valid save file to load; otherwise it will start anew.
	
	To validate our multithreaded tests, we added some checks to the get() and put() methods in Store.

Assumptions
-	it is safe to allow users to perform reads (get()) on the database while another thread performs a write or modify operation
-	it is not safe for write and modify operations to operate simultaneously
-	each file that is added will be no larger than 1 GB
-	the database will not grow larger than the size of allocated physical memory

Testing
	In order to access the database, a testing class (Main.java) instantiates a Store object and operates on it. The main function calls a series of methods which perform different tests on the Store. The test methods perform various actions to evaluate the system, including invoking multiple threads to test various scenarios that may occur naturally. Each of the test methods are detailed in code comments.
	
	Because of the use of multithreading, we found it easier to test manually rather than rather than using a test runner such as JUnit. To run the tests, simply run the main function from Main.java. The output displays some of the test results.
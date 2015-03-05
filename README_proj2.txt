Implementation
We implemented our index as an extendible hash index. The index consists of a directory and a number of buckets. 

A bucket contains a fixed-size array of IndexElements, which store the <rid> and <data_value> elements of data as in Alternative 2. It also keeps track of its local depth.

The directory contains an ArrayList of buckets. We used an ArrayList to represent this because it must be able to grow as the directory expands. 

When a client calls put() the directory hashes the specified data_value and uses the result to place it in the correct bucket. When this happens, there are three possible outcomes:
	1. The bucket has space: simply add the new IndexElement to the bucket.
	2. The bucket is full and the local depth is less than global depth: create a new bucket and rehash all of the values from the old bucket.
	3. The bucket is full and the local depth is equal to the global depth: expand the directory, create a new bucket and rehash all of the values from the old bucket. The local depth of the overflowed bucket  and global depth are incremented.

When the client calls get() the directory hashes the given data_value to find the bucket which contains the matching IndexElements. It then compares the exact data_value to each element in the bucket and returns it if it matches the IndexElement's data_value.

Because it functions on the rid alone, the remove() function must go through each bucket until it finds the matching rid to delete.



Assumptions
-	There can be a maximum of BucketSize elements in the dataset with the same data_value. This is because extensible hashing does not allow for overflow buckets.

-	There will be only one client accessing the index at a time.
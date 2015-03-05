Implementation
==============
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
===========
-	There can be a maximum of BucketSize elements in the dataset with the same data_value. This is because extensible hashing does not allow for overflow buckets.

-	There will be only one client accessing the index at a time.

- 	The capacity of the bucket is assumed to contain at most 4 elements

Testing
=======

Our test code for the indexing mechanism is implemented in the main method of the Main class. The test cases we ran exercise the interface by building an index off of a sample 'rid' and 'data_value" attribute by consecutive calls to 'put' method. We subsequently test the functionality of 'get' and 'remove' methods.

The sample test run of our test cases is shown below for reference:


Insert ('1', '4') into Bucket 0 successfully!
Current globalDepth = 2, Bucket0's localDepth = 2

Insert ('2', '4') into Bucket 0 successfully!
Current globalDepth = 2, Bucket0's localDepth = 2

Insert ('3', '4') into Bucket 0 successfully!
Current globalDepth = 2, Bucket0's localDepth = 2

Insert ('4', '4') into Bucket 0 successfully!
Current globalDepth = 2, Bucket0's localDepth = 2
--- EXPAND ---

Insert ('1', '4') into Bucket 4 successfully!
Current globalDepth = 3, Bucket4's localDepth = 3

Insert ('2', '4') into Bucket 4 successfully!
Current globalDepth = 3, Bucket4's localDepth = 3

Insert ('3', '4') into Bucket 4 successfully!
Current globalDepth = 3, Bucket4's localDepth = 3

Insert ('4', '4') into Bucket 4 successfully!
Current globalDepth = 3, Bucket4's localDepth = 3
--- END OF EXPAND ---

Insert ('5', '8') into Bucket 0 successfully!
Current globalDepth = 3, Bucket0's localDepth = 3

Insert ('6', '8') into Bucket 0 successfully!
Current globalDepth = 3, Bucket0's localDepth = 3

Insert ('7', '8') into Bucket 0 successfully!
Current globalDepth = 3, Bucket0's localDepth = 3

Insert ('8', '8') into Bucket 0 successfully!
Current globalDepth = 3, Bucket0's localDepth = 3
--- EXPAND ---

Insert ('5', '8') into Bucket 8 successfully!
Current globalDepth = 4, Bucket8's localDepth = 4

Insert ('6', '8') into Bucket 8 successfully!
Current globalDepth = 4, Bucket8's localDepth = 4

Insert ('7', '8') into Bucket 8 successfully!
Current globalDepth = 4, Bucket8's localDepth = 4

Insert ('8', '8') into Bucket 8 successfully!
Current globalDepth = 4, Bucket8's localDepth = 4
--- END OF EXPAND ---

Insert ('9', '11') into Bucket 0 successfully!
Current globalDepth = 4, Bucket0's localDepth = 4

Insert ('10', '5') into Bucket 1 successfully!
Current globalDepth = 4, Bucket1's localDepth = 2

Insert ('11', '6') into Bucket 2 successfully!
Current globalDepth = 4, Bucket2's localDepth = 2

Insert ('8', 'xinjiehao') into Bucket 3 successfully!
Current globalDepth = 4, Bucket3's localDepth = 2


The current globalDepth = 4
Get attribute value = '4' in Bucket 4 with localDepth = 3
rids: '1'  '2'  '3'  '4'  

The current globalDepth = 4
Get attribute value = '8' in Bucket 8 with localDepth = 4
rids: '5'  '6'  '7'  '8'  

The current globalDepth = 4
Get attribute value = '11' in Bucket 0 with localDepth = 4
rids: '9'  

The current globalDepth = 4
Get attribute value = '6' in Bucket 2 with localDepth = 2
rids: '11'  

Remove rid = '11' from Bucket 2 successfully!


The current globalDepth = 4
Get attribute value = '6' in Bucket 2 with localDepth = 2
Oops..'6' does not exist!


Remove rid = '1' from Bucket 4 successfully!


The current globalDepth = 4
Get attribute value = '4' in Bucket 4 with localDepth = 3
rids: '2'  '3'  '4'  

Remove rid = '2' from Bucket 4 successfully!


The current globalDepth = 4
Get attribute value = '4' in Bucket 4 with localDepth = 3
rids: '3'  '4'  

Remove rid = '5' from Bucket 8 successfully!


The current globalDepth = 4
Get attribute value = '8' in Bucket 8 with localDepth = 4
rids: '6'  '7'  '8'  
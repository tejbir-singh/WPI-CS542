Design

The Relation class is a modified version of Store from project 1. We added the ability to enumerate the data, allowing us to easily iterate through it rather than searching the hash table for each value. We also added a boolean to the constructor to specify whether or not the relation needs persistent storage.

Because we were given a specific query to run, we implemented the whole query as a single operator. Open() simply loads the databases into memory. If it is the first run then we generate the Relation objects from their respective .csv files. If the code had already been run once then we can use the previously generated storage objects, allowing the program to run much faster (~500ms from ~150 seconds).

GetNext() is where most of the work happens. First, the country table is sorted on its Code attribute. This allows us to perform a binary search on the country table for the matching country code in the city table. Once we find it, we compare the country's total population to the city's population. If the city's population is over 40% of the total, then it is added to the results table.

GetNextAlternative() is our original implementation which outputs the correct results, but is slower than GetNext() (~500ms from ~6600ms). This method iterates lazily through each table and searches for the matching tuple rather than using binary search.

Close() outputs the results to the console.

Assumptions
- Because we were given a specific query to execute, having a single operator is sufficient.
- Because the data set is small, we do not need to read it block by block from the disk into memory.

Testing
We had a fairly straightforward testing procedure, as we were only asked to account for one query: running each of our open(), getNext() and close() procedures and verifying the results against the expected results. We obtained the results from running the query on the same dataset in MySQL.

In order to run our code, simply run the application from query.exec.Main.java.
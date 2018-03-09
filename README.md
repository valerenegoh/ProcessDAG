# SUTD ISTD Term 5 Programming Assignment 1
1 /*Programming Assignment 1
2 * Author : Valerene Goh Ze Yi
3 * ID: 1002457
4 * Date : 09/03/2018 */

## Purpose of this program
This program traverses a directed acyclic graph (DAG) of user programs from an input text file in parallel, while fulfilling both contol and data dependencies between each other. Multithreading is implemented for the processes that are labelled runnable for that particular iteration. The diagram shows a visual representaion of the DAG traversal for a corresponding graph file.



## How to compile this program
This program works much better in Linux than Windows. It is suggested that you use a Virtual Machine such as Ubuntu to run it if you are not a Linux user.

### For Linux shell:
1) Change directory (using the command `cd`) to the directory location containing all your files.
2) Compile the java file using the following command:
  `$ javac ProcessManagement.java`
  Run the java file using the following command, whereby `[arg]` is either one of: `graph-file`/`graph-file1`/`graph-file2`/`graph-file3`
  `$ java ProcessManagement [arg]`
  
### For IDE:
1) Download the zip folder and extract its contents.
2) In `ProcessManagement.java`, ensure that user.dir is the directory location containing all your files.
3) Under Edit Configurations, input either one of: `graph-file`/`graph-file1`/`graph-file2`/`graph-file3`.
  Alternatively, you can directly edit the file path for `instructionSet` to the file path of your chosen graph file.
3) Run `ProcessManagement.java` to start the program.

## What exactly the program does
ProcessManagement.java passes the graph file into ParseFile.java.

The program ProcessManagement reads it line by line and parses the information contained in it (colon delimited). Each node of the graph is represented by a line with the following format:
`program name with arguments:list of children ID's:input file:output file`

The program first checks if all nodes have been executed and if a cycle exists in the graph (all process nodes are not runnable while there still exist unexecuted process nodes), subject to race condition. This feature is part of deadlock prevention.

If none if this is fulfilled (i.e. not all have been executed and there isn't a cycle), we iterate through every process node in the DAG and set it to runnable if it is runnable. Then, for the runnable nodes, multithreading executes them concurrently for each iteration of the while loop. The order in which the process nodes are executed will be printed in the terminal.

The program terminates when all nodes have been executed successfully or when the program finds an error (e.g. a cycle causing deadlock).

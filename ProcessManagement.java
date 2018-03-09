import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessManagement {

    //set the working directory
    public static File currentDirectory = new File(System.getProperty("user.dir"));
    //set the instructions file
    private static File instructionSet;

    public static Object lock=new Object();

    public static void main(String[] args) throws InterruptedException {

//        for(String fileNames : currentDirectory.list()) System.out.println(fileNames);
    	
    	instructionSet = new File(args[0]);

        //parse the instruction file and construct a data structure, stored inside ProcessGraph class
        ParseFile.generateGraph(new File(currentDirectory + "/"+ instructionSet));

        // Print the graph information
        ProcessGraph.printGraph();
        
        boolean allComplete = false;
        boolean cycleExists = false;

        while(!allComplete && !cycleExists){
        	
        	// TODO: check if all the nodes are executed
        	allComplete = true;            
            for (ProcessGraphNode node : ProcessGraph.nodes) {
            	if(!node.isExecuted()){
            		allComplete = false;
            	}
            }
            if(allComplete){
            	break;      //escape while loop
            }
	
            // TODO: Using index of ProcessGraph, loop through each ProcessGraphNode, to check whether it is ready to run
            synchronized (lock) {	            
            	// TODO: mark all the runnable nodes
	            for (ProcessGraphNode node : ProcessGraph.nodes) {	                
	            	if(!node.isExecuted()){
	            		allComplete = false;
	            		if(node.allParentsExecuted() && !node.isRunning()){		                    
		                    node.setRunnable();
	            		}
	                }
	            }
            
		        //TODO: Check for cycles
		        boolean allNotRunnable = true, allExecuted = true;
		
		        for(ProcessGraphNode node : ProcessGraph.nodes){
		            if (node.isRunnable()){
		                allNotRunnable = false;
				 }
		            if (!node.isExecuted()){
		                allExecuted = false;
				 }
		        }
		        if(allNotRunnable && !allExecuted){
		        	System.out.println("There exists a cycle!");
		        	cycleExists = true;
		        }
            }
      
            if(cycleExists){
                break;      //escape while loop
            }
            
            // TODO: run the node if it is runnable
            List<ProcessThread> threadsList = new ArrayList<>();
            //convert all eligible process nodes into process threads
            for(ProcessGraphNode node: ProcessGraph.nodes){
                //control dependency         
                if(node.allParentsExecuted() && node.isRunnable() && !node.isExecuted() && !node.isRunning()){   //double check
                    ProcessThread threadn = new ProcessThread(node);                  
                    threadsList.add(threadn);
                }
            }
            for(ProcessThread thread: threadsList){
            	System.out.println("Process " + thread.processNode.getNodeId() + " has started execution. Please be patient while it executes...");
            	thread.processNode.setRunning(true);
                thread.start();
            }            
            for(ProcessThread thread: threadsList){
                thread.join();
                System.out.println("Process " + thread.processNode.getNodeId()+ " has finished execution");
            }
        }

        System.out.println("All process finished successfully");
    }
}

class ProcessThread extends Thread{
    ProcessGraphNode processNode;
    File inputFile, outputFile;
    ProcessBuilder pb;

    public ProcessThread(ProcessGraphNode processNode) {
        this.processNode = processNode;
    }

    @Override
    public void run() {
        //execute process node
        String command = processNode.getCommand();
        pb = new ProcessBuilder(command.split("\\s+"));
        pb.directory(ProcessManagement.currentDirectory);

        //handle input/output files
        if(!processNode.getInputFile().exists() && !processNode.getInputFile().getName().equals("stdin")){
            System.out.println("input file " + processNode.getInputFile().getName() + " doesn't exist!");
        } else {
            inputFile = processNode.getInputFile();
            if(!inputFile.getName().equals("stdin")){
                pb.redirectInput(inputFile);
            }
        }
        if(!processNode.getOutputFile().exists() && !processNode.getOutputFile().getName().equals("stdout")){
            System.out.println("input file " + processNode.getOutputFile().getName() + " doesn't exist!");
        } else{
            outputFile = processNode.getOutputFile();
            if(!outputFile.getName().equals("stdout")){
                pb.redirectOutput(outputFile);
            }
        }

//        pb.command("bash", "-c", command);
        try {
            Process p = pb.start();
//            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            for(String line; (line = br.readLine()) != null;){
//                System.out.println(line);
//            }
//            br.close();

            p.waitFor();
            synchronized (ProcessManagement.lock){
            	processNode.setExecuted();
            	processNode.setNotRunnable();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}

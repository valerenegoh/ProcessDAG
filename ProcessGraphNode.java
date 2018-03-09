import java.io.File;
import java.util.ArrayList;


/**
 * Programming Assignment 1
 * Done by:
 * Koh Kai Wei 1001471
 * Chan Wei Ren 1001459
 *
 * CHANGES : Added a running boolean to tell us if the process is running.
 * METHODS :
 *  isRunning - Returns a boolean representing if the node's command is running as a process
 *  setRunning - sets running to true or false.
 *
 **/

public class ProcessGraphNode {

    //point to all the parents
    private ArrayList<ProcessGraphNode> parents=new ArrayList<>();
    //point to all the children
    private ArrayList<ProcessGraphNode> children=new ArrayList<>();
    //properties of ProcessGraphNode
    private int nodeId;
    private File inputFile;
    private File outputFile;
    private String command;
    private boolean runnable;
    private boolean executed;
    private boolean running; // This field is to notify us if the node is running


    public ProcessGraphNode(int nodeId ) {
        this.nodeId = nodeId;
        this.runnable=false;
        this.running=false;
        this.executed=false;
    }



    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setRunnable() {
        this.runnable = true;
    }

    public void setNotRunnable() {this.runnable = false;}

    public void setExecuted() {
        this.executed = true;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isRunnable() {
        return runnable;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void addChild(ProcessGraphNode child){
        if (!children.contains(child)){
            children.add(child);
        }
    }

    public void addParent(ProcessGraphNode parent){
        if (!parents.contains(parent)){
            parents.add(parent);
        }
    }
    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public String getCommand() {
        return command;
    }

    public ArrayList<ProcessGraphNode> getParents() {
        return parents;
    }

    public ArrayList<ProcessGraphNode> getChildren() {
        return children;
    }

    public int getNodeId() {
        return nodeId;
    }

    public synchronized boolean allParentsExecuted(){
        boolean ans=true;
        for (ProcessGraphNode child : this.getChildren()) {
            if (child.isExecuted()) {
                return false;
            }
        }
        for (ProcessGraphNode parent:this.getParents()) {
            if (!parent.isExecuted())
                ans=false;
        }
        return ans;
    }
}

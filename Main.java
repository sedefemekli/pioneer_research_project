import java.util.*;
import java.io.*;


class Main{

  //Returns an ArrayList of graph names in the given directory
  public static ArrayList<String> getGraphNames(String dir, String type){

    //Change the format of the directory to avoid errors
    if(!dir.substring(dir.length()-1).equals("/")) {
  	  dir += "/";
  	}
  	if(!dir.substring(0,1).equals("/")) {
  	  dir = "/" + dir;
  	}

    ArrayList<String> graphNames = new ArrayList<String>(); //ArrayList of Strings to return

    //Define the directory and get all files
    File directory = new File(dir);
    File[] directoryFiles = directory.listFiles();

    //Traverse through all of the files in the directory
    for(File file: directoryFiles){
      //If the file is a file
      if(file.isFile()){
        String name = file.getName();
        //If the extension is .txt and the file is visible (doesn't start with .), add the graph's name to the ArrayList
        if(name.substring(name.length() - 4, name.length()).equals(".txt") && !name.substring(0,1).equals(".") && name.substring(0,1).equals(type)){
          graphNames.add(name);
        }
      }
    }
    return graphNames;
  }

  //Returns the graph of the given File
  public static Graph getGraph(File file){
    //Initialize the graph to return
    Graph g = new Graph(1);

    try {
      //Scan the file and read the lines
      Scanner sc = new Scanner(file);
      ArrayList<String> lines = new ArrayList<String>();
      while(sc.hasNextLine()) {
        lines.add(sc.nextLine());
      }
      sc.close();

      //Get the file name and obtain the number of vertices from it
      String fileName = file.getName();
      String strNumVertices = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
      strNumVertices = strNumVertices.substring(strNumVertices.indexOf(".") + 1, strNumVertices.length());
      strNumVertices = strNumVertices.substring(0,strNumVertices.indexOf("."));
      int numVertices = Integer.parseInt(strNumVertices);

      //Initialize the graph with the found number of vertices
      g = new Graph(numVertices);

      //Traverse through the lines and get the values from the file to add them to the graph
      for(int j = 0; j < numVertices; j++){
        String[] strValues = lines.get(j+1).split(" ");
        for(int i = 0; i < numVertices; i++){
          if(Integer.parseInt(strValues[i]) == 1){
            g.addEdge(i,j);
          }
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    //Return the graph
    return g;
  }

  public static Graph createGraph(){
    Graph g = new Graph(5);
    g.addEdge(0,1);
    g.addEdge(0,2);
    g.addEdge(0,4);
    g.addEdge(1,0);
    g.addEdge(2,0);
    g.addEdge(2,3);
    g.addEdge(2,4);
    g.addEdge(3,2);
    g.addEdge(3,4);
    g.addEdge(4,0);
    g.addEdge(4,2);
    g.addEdge(4,3);
    return g;
  }


  public static void main(String[] args) {
    Graph g;           //Graph that will be tested

    //Get the directory of the graphs from the user
    // Scanner sc = new Scanner(System.in);
    // System.out.println("Enter the directory of the folder containing the graphs");
    // String dir = sc.nextLine();
    String dir = "/Users/sedef/Desktop/LPP/Graphs/";

    Greedy greedy;    //Declare the Greedy object
    WFCS wfcs;          //Declare the Wfc object
    WFCL wfcl;          //Declare the Wfc object
    long start, end;  //Start and end times for the execution time



    String csvDir = "/Users/sedef/Desktop/LPP/Tests/csv.txt";
    File csv = new File(csvDir);
    String csvData = "";
    try{
      csv.createNewFile();
    }
    catch (Exception e) {
      e.getStackTrace();
    }

    csvData = "Graph Name,Nodes,Edges,Greedy(with) Runtime,Greedy(with) Path Size,Greedy(with) Ratio,Greedy(without) Runtime,Greedy(without) Path Size,Greedy(without) Ratio, WFCS Runtime,WFCS Path Size,WFCS Ratio, WFCL Runtime,WFCL Path Size,WFCL Ratio\n";
    int numSamples = 0;
    while(numSamples < 40){
      numSamples++;
      String testsDir = "/Users/sedef/Desktop/LPP/Tests/"+numSamples+".txt";
      File tests = new File(testsDir);
      String testsData = "";
      try{
        tests.createNewFile();
      }
      catch (Exception e) {
        e.getStackTrace();
      }
      ArrayList<String> directedGraphNames = getGraphNames(dir + numSamples + "/", "d");     //Get the names of the directed graphs
      ArrayList<String> undirectedGraphNames = getGraphNames(dir + numSamples + "/", "u");     //Get the names of the undirected graphs

      //Traverse through the directed graph names
      for(String name: directedGraphNames){

        //Print out the graph informations
        System.out.println("Graph " + name.substring(0,name.length()-4));
        testsData = testsData + "Graph " + name.substring(0,name.length()-4) + "\n";
        csvData = csvData + name.substring(0,name.length()-4) + ",";
        File f = new File(dir + numSamples + "/" + name);

        //Greedy With execution
        g = getGraph(f);
        csvData = csvData + g.numVertices + "," + g.numEdges + ",";
        greedy = new Greedy();
        start = System.nanoTime();
        ArrayList<Integer> greedyWPath = greedy.runWithRemoving(g);
        end = System.nanoTime();
        System.out.println("Greedy(with) Execution: " + (end - start) + " nanoseconds\tPath Size: " + greedyWPath.size() + "\tTime/size: " + ((float)(end - start)/greedyWPath.size()));
        System.out.println("\t"+greedyWPath);
        testsData = testsData + "Greedy(with) Execution: " + (end - start) + " nanoseconds\tPath Size: " + greedyWPath.size() + "\tTime/size: " + ((float)(end - start)/greedyWPath.size());
        testsData = testsData + "\t"+ greedyWPath + "\n";
        csvData = csvData + (end - start) + "," + greedyWPath.size() + "," + ((float)(end - start)/greedyWPath.size()) + ",";

        //Greedy Without execution
        g = getGraph(f);
        greedy = new Greedy();
        start = System.nanoTime();
        ArrayList<Integer> greedyPath = greedy.runWithoutRemoving(g);
        end = System.nanoTime();
        System.out.println("Greedy(without) Execution: " + (end - start) + " nanoseconds\tPath Size: " + greedyPath.size() + "\tTime/size: " + ((float)(end - start)/greedyPath.size()));
        System.out.println("\t"+greedyPath);
        testsData = testsData + "Greedy(without) Execution: " + (end - start) + " nanoseconds\tPath Size: " + greedyPath.size() + "\tTime/size: " + ((float)(end - start)/greedyPath.size());
        testsData = testsData + "\t"+ greedyPath + "\n";
        csvData = csvData + (end - start) + "," + greedyPath.size() + "," + ((float)(end - start)/greedyPath.size()) + ",";

        //WFCS Execution
        g = getGraph(f);
        wfcs = new WFCS();
        start = System.nanoTime();
        ArrayList<Integer> wfcsPath = wfcs.run(g);
        end = System.nanoTime();
        System.out.println("WFCS Execution: " + (end - start) + " nanoseconds\tPath Size: " + wfcsPath.size() + "\tTime/size: " + ((float)(end - start)/wfcsPath.size()));
        System.out.println("\t"+wfcsPath);
        testsData = testsData + "WFCS Execution: " + (end - start) + " nanoseconds\tPath Size: " + wfcsPath.size() + "\tTime/size: " + ((float)(end - start)/wfcsPath.size());
        testsData = testsData + "\t"+ wfcsPath + "\n";
        csvData = csvData + (end - start) + "," + wfcsPath.size() + "," + ((float)(end - start)/wfcsPath.size()) + ",";

        //WFCL Execution
        g = getGraph(f);
        wfcl = new WFCL();
        start = System.nanoTime();
        ArrayList<Integer> wfclPath = wfcl.run(g);
        end = System.nanoTime();
        System.out.println("WFCL Execution: " + (end - start) + " nanoseconds\tPath Size: " + wfclPath.size() + "\tTime/size: " + ((float)(end - start)/wfclPath.size()));
        System.out.println("\t"+wfclPath);
        testsData = testsData + "WFCL Execution: " + (end - start) + " nanoseconds\tPath Size: " + wfclPath.size() + "\tTime/size: " + ((float)(end - start)/wfclPath.size());
        testsData = testsData + "\t"+ wfclPath + "\n";
        csvData = csvData + (end - start) + "," + wfclPath.size() + "," + ((float)(end - start)/wfclPath.size()) + "\n";

        System.out.println("-------------------------------------------------------------------------------");
        testsData = testsData + "-------------------------------------------------------------------------------" + "\n";
      }
      //Traverse through the undirected graph names
      for(String name: undirectedGraphNames){

        //Print out the graph informations
        System.out.println("Graph " + name.substring(0,name.length()-4));
        testsData = testsData + "Graph " + name.substring(0,name.length()-4) + "\n";
        csvData = csvData + name.substring(0,name.length()-4) + ",";
        File f = new File(dir + numSamples + "/" + name);

        //Greedy With execution
        g = getGraph(f);
        csvData = csvData + g.numVertices + "," + g.numEdges + ",";
        greedy = new Greedy();
        start = System.nanoTime();
        ArrayList<Integer> greedyWPath = greedy.runWithRemoving(g);
        end = System.nanoTime();
        System.out.println("Greedy(with) Execution: " + (end - start) + " nanoseconds\tPath Size: " + greedyWPath.size() + "\tTime/size: " + ((float)(end - start)/greedyWPath.size()));
        System.out.println("\t"+greedyWPath);
        testsData = testsData + "Greedy(with) Execution: " + (end - start) + " nanoseconds\tPath Size: " + greedyWPath.size() + "\tTime/size: " + ((float)(end - start)/greedyWPath.size());
        testsData = testsData + "\t"+ greedyWPath + "\n";
        csvData = csvData + (end - start) + "," + greedyWPath.size() + "," + ((float)(end - start)/greedyWPath.size()) + ",";

        //Greedy Without execution
        g = getGraph(f);
        greedy = new Greedy();
        start = System.nanoTime();
        ArrayList<Integer> greedyPath = greedy.runWithoutRemoving(g);
        end = System.nanoTime();
        System.out.println("Greedy(without) Execution: " + (end - start) + " nanoseconds\tPath Size: " + greedyPath.size() + "\tTime/size: " + ((float)(end - start)/greedyPath.size()));
        System.out.println("\t"+greedyPath);
        testsData = testsData + "Greedy(without) Execution: " + (end - start) + " nanoseconds\tPath Size: " + greedyPath.size() + "\tTime/size: " + ((float)(end - start)/greedyPath.size());
        testsData = testsData + "\t"+ greedyPath + "\n";
        csvData = csvData + (end - start) + "," + greedyPath.size() + "," + ((float)(end - start)/greedyPath.size()) + ",";

        //WFCS Execution
        g = getGraph(f);
        wfcs = new WFCS();
        start = System.nanoTime();
        ArrayList<Integer> wfcsPath = wfcs.run(g);
        end = System.nanoTime();
        System.out.println("WFCS Execution: " + (end - start) + " nanoseconds\tPath Size: " + wfcsPath.size() + "\tTime/size: " + ((float)(end - start)/wfcsPath.size()));
        System.out.println("\t"+wfcsPath);
        testsData = testsData + "WFCS Execution: " + (end - start) + " nanoseconds\tPath Size: " + wfcsPath.size() + "\tTime/size: " + ((float)(end - start)/wfcsPath.size());
        testsData = testsData + "\t"+ wfcsPath + "\n";
        csvData = csvData + (end - start) + "," + wfcsPath.size() + "," + ((float)(end - start)/wfcsPath.size()) + ",";

        //WFCL Execution
        g = getGraph(f);
        wfcl = new WFCL();
        start = System.nanoTime();
        ArrayList<Integer> wfclPath = wfcl.run(g);
        end = System.nanoTime();
        System.out.println("WFCL Execution: " + (end - start) + " nanoseconds\tPath Size: " + wfclPath.size() + "\tTime/size: " + ((float)(end - start)/wfclPath.size()));
        System.out.println("\t"+wfclPath);
        testsData = testsData + "WFCL Execution: " + (end - start) + " nanoseconds\tPath Size: " + wfclPath.size() + "\tTime/size: " + ((float)(end - start)/wfclPath.size());
        testsData = testsData + "\t"+ wfclPath + "\n";
        csvData = csvData + (end - start) + "," + wfclPath.size() + "," + ((float)(end - start)/wfclPath.size()) + "\n";

        System.out.println("-------------------------------------------------------------------------------");
        testsData = testsData + "-------------------------------------------------------------------------------" + "\n";
      }

      try {
        FileWriter output = new FileWriter(testsDir);
        output.write(testsData);
        output.close();
      } catch(Exception e) {
        e.getStackTrace();
      }
    }





    try {
      FileWriter output = new FileWriter(csvDir);
      output.write(csvData);
      output.close();
    } catch(Exception e) {
      e.getStackTrace();
    }
  }
}

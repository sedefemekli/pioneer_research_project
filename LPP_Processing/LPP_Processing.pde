import java.util.*;
import java.io.*;

class Graph{

  int initialSize;      //Number of vertices initially
  int numVertices;      //Number of vertices currently
  int numEdges;         //Number of edges currently
  int[][] grid;         //Grid that contains the edges between nodes

  //Graph constructor
  Graph(int initialSize){
    this.initialSize = initialSize;
    this.numVertices = initialSize;
    this.numEdges = 0;
    grid = new int[initialSize][initialSize];
    //Initially, there aren't any edges
    for(int[] arr: grid){
      Arrays.fill(arr,0);
    }
  }

  Graph(Graph graph){
    for(int i = 0; i < graph.initialSize; i++){
      for(int j = 0; j < graph.initialSize; j++){
        this.grid[i][j] = graph.grid[i][j];
      }
    }
  }

  //Shows if a vertex exists/isn't removed
  boolean vertexExists(int vertex){
    int initialIn = grid[0][vertex];
    int initialOut = grid[vertex][0];
    int zerosIn = 0;
    int zerosOut = 0;
    for(int i = 0; i < initialSize; i++){
      if(grid[vertex][i] != initialOut || grid[i][vertex] != initialIn){
        return true;
      }else if(grid[vertex][i] == 0){
        zerosOut++;
      }else if(grid[i][vertex] == 0){
        zerosIn++;
      }
      initialOut = grid[vertex][i];
      initialIn = grid[i][vertex];
    }
    if(zerosOut == numVertices && zerosIn == numVertices) return false;
    return true;
  }

  //Adds an edge relationship from vertex1 to vertex2
  void addEdge(int vertex1, int vertex2){
    if(!edgeExists(vertex1, vertex2)){
      grid[vertex1][vertex2] = 1;
      numEdges++;
    }
  }

  //Shows if an edge exists from vertex1 to vertex2
  boolean edgeExists(int vertex1, int vertex2){
    return (grid[vertex1][vertex2] == 1);
  }

  //Marks the vertex's edges as zero (deleted/no edge)
  void removeVertex(int vertex){
    numEdges -= (indegreeOf(vertex) + outdegreeOf(vertex));
    numVertices--;
    for(int i = 0; i < initialSize; i++){
      grid[i][vertex] = 0;
      grid[vertex][i] = 0;
    }
  }

  //Marks the vertex's edges as two (visited)
  void markVisitedVertex(int vertex){
    numEdges -= (indegreeOf(vertex) + outdegreeOf(vertex));
    numVertices--;
    for(int i = 0; i < initialSize; i++){
      grid[i][vertex] = 2;
      grid[vertex][i] = 2;
    }
  }

  //Marks the edge from vertex1 to vertex2 as two (visited)
  void markVisitedEdge(int vertex1, int vertex2){
    grid[vertex1][vertex2] = 2;
  }

  //Returns if the incoming edges and/or the outgoing edges are all visited
  boolean isSaturated(int vertex){
    for(int i = 0; i < initialSize; i++){
      if(grid[vertex][i] == 1 || grid[i][vertex] == 1 ){
        return false;
      }
    }
    return true;
  }

  //Returns if there is an unvisited edge
  boolean unvisitedEdgeExists(){
    for(int i = 0; i < initialSize; i++){
      for(int j = 0; j < initialSize; j++){
        if(grid[i][j] == 1 || grid[j][i] == 1){
          return true;
        }
      }
    }
    return false;
  }

  //Returns an unvisited vertex
  int unvisitedVertex(){
    for(int i = 0; i < initialSize; i++){
      int zeros = 0;
      for(int j = 0; j < initialSize; j++){
        if(grid[i][j] == 0){
          zeros++;
        }
      }
      if(zeros == initialSize){
        return i;
      }
    }
    return -1;
  }

  //Returns the indegree of the vertex, returns -1 if the vertex doesn't exist
  int indegreeOf(int vertex){
    int degree = 0;
    if(!vertexExists(vertex)){
      return -1;
    }
    for(int i = 0; i < initialSize; i++){
      if(grid[i][vertex] == 1){
        degree++;
      }
    }
    return degree;
  }

  //Returns the outdegree of the vertex, returns -1 if the vertex doesn't exist
  int outdegreeOf(int vertex){
    int degree = 0;
    if(!vertexExists(vertex)){
      return -1;
    }
    for(int i = 0; i < initialSize; i++){
      if(grid[vertex][i] == 1){
        degree++;
      }
    }
    return degree;
  }

  //Returns the vertex with the highest outdegree in the graph, if all are 0, returns -1
  int highestOutdegreeVertex(){
    int highestDegree = 0;
    int highestIndex = -1;
    for(int i = 0; i < initialSize; i++){
      int currentOutdegree = outdegreeOf(i);
      if(currentOutdegree > highestDegree){
        highestDegree = currentOutdegree;
        highestIndex = i;
      }
    }
    return highestIndex;
  }

  //Returns the vertex with the highest outdegree in the graph, if all are 0, returns -1
  int highestOutdegreeNeighbor(int vertex){
    int highestDegree = 0;
    int highestIndex = -1;
    for(int i = 0; i < initialSize; i++){
      int currentOutdegree = outdegreeOf(i);
      if(edgeExists(vertex,i) && currentOutdegree >= highestDegree){
        highestDegree = currentOutdegree;
        highestIndex = i;
      }
    }
    return highestIndex;
  }

  //Returns an ArrayList of the vertex's outgoing neighbors
  ArrayList<Integer> returnOutgoingNeighbors(int vertex){
    ArrayList<Integer> neighbors = new ArrayList<Integer>();
    for(int i = 0; i < initialSize; i++){
      if(edgeExists(vertex,i)){
        neighbors.add(i);
      }
    }
    return neighbors;
  }

  //Returns an ArrayList of the vertex's incoming neighbors
  ArrayList<Integer> returnIncomingNeighbors(int vertex){
    ArrayList<Integer> neighbors = new ArrayList<Integer>();
    for(int i = 0; i < initialSize; i++){
      if(edgeExists(i,vertex)){
        neighbors.add(i);
      }
    }
    return neighbors;
  }


  //Prints the graph
  void printGraph(){
    System.out.println("\nGraph (V: "+numVertices+",E: "+numEdges+")");
    for(int i = 0; i < initialSize; i++){
      for(int j = 0; j < initialSize; j++){
        System.out.print(grid[i][j] + " ");
      }
      System.out.println();
    }
  }
}

class Greedy{

  Graph g;                  //Graph used in the algorithm
  float d;                  //Value denoting the density of the graph
  ArrayList<Integer> path;  //Resulting path


  public Greedy(){}

  //Remove vertices with outdegree less than d
  void removeVertices(){
    //Find the value d defined as number of edges divided by number of vertices
    float d = (float)g.numEdges/(float)g.numVertices;

    for(int i = 0; i < g.numVertices; i++){
      int deg = g.outdegreeOf(i);
      if(deg != -1 &&  deg < d){
        g.removeVertex(i);
      }
    }
  }

  //Create the path
  void createPath(){
    //Create a Random object
    Random rand = new Random();
    //Initialize the path
    path = new ArrayList<Integer>();
    //Current vertex
    int current;
    //Initially start with a random node
    int next = rand.nextInt(g.initialSize);

    //Until all vertices are removed:
    while(next != -1){
      current = next;
      //Add the vertex to the path
      path.add(current);
      //Find the neighbor with the highest degree
      // next = g.highestOutdegreeNeighbor(current);
      if(g.outdegreeOf(current) == 0){
        return;
      }
      ArrayList<Integer> neighbors = g.returnOutgoingNeighbors(current);


      next = neighbors.get(rand.nextInt(g.outdegreeOf(current)));

      if(next == -1){
        return;
      }
      //Remove the current vertex
      g.markVisitedVertex(current);
    }
  }

  //Main function used for running with removing vertices
  ArrayList<Integer> runWithRemoving(Graph givenGraph){
    //Create the graph
    g = givenGraph;

    //Remove vertices with less than outdegree d
    removeVertices();

    //Create the path
    createPath();

    return path;

  }

  //Main function used for running without removing vertices
  ArrayList<Integer> runWithoutRemoving(Graph givenGraph){
    //Create the graph
    g = givenGraph;

    //Create the path
    createPath();

    return path;

  }
}

class WFCS{

  Graph g;
  ArrayList<ArrayList<Integer>> index = new ArrayList<ArrayList<Integer>>();
  ArrayList<Integer> path = new ArrayList<Integer>();

  public WFCS(){}

  //Create the index of possible patterns each
  public void createIndex(){
    //Traverse through all nodes and add neighbors to the index
    for(int i = 0; i < g.initialSize; i++){
      ArrayList<Integer> al = new ArrayList<Integer>();
      for(int j = 0; j < g.initialSize; j++){
        if(g.grid[i][j] == 1){
          al.add(j);
        }
      }
      index.add(al);
    }
  }

  //Recursively find a path from the index
  public void DFS(int vertex, boolean visited[]){
    //Mark the vertex as visited
    visited[vertex] = true;
    //Add the vertex to the path
    path.add(vertex);

    //Get the neighbors of the vertex
    ArrayList<Integer> neighbors = index.get(vertex);
    int min = g.initialSize;
    int val = -1;
    //Find the next neighbor to go to
    for(int neighbor: neighbors){
      int size = index.get(neighbor).size();
      if(size < min){
        val = neighbor;
        min = size;
      }
      val = neighbor;

      if(val == -1){
        return;
      }
      //If the neighbor isn't visited, repeats the function on that vertex
      if(!visited[val]){
        DFS(val, visited);
      }
    }

  }

  //Choose vertex to start
  public int vertexSelection(){
    //Initialize the array to see the visited vertices
    boolean visited[] = new boolean[g.initialSize];

    int vertex = -1;
    int min = g.initialSize;

    //Chooses the vertex and returns it
    for(int i = 0; i < g.initialSize; i++){
      int size = index.get(i).size();
      if(g.outdegreeOf(i) == 1){
        vertex = i;
        return vertex;
      }

      else if(size <= min && size != 0){
        vertex = i;
        min = size;
      }
    }

    return vertex;
  }

  //Returns the resulting path
  public ArrayList<Integer> run(Graph givenGraph){
    //Initialize the Graph g
    g = givenGraph;

    //Create the index
    createIndex();

    //Choose a vertex to start
    int vertex = vertexSelection();
    if(vertex == -1){
      path.clear();
      return path;
    }

    boolean visited[] = new boolean[g.initialSize];
    //Recursively create the path
    DFS(vertex,visited);

    return path;

  }

}

class WFCL{

  Graph g;    //Graph that the path is created in

  ArrayList<ArrayList<Integer>> patterns = new ArrayList<ArrayList<Integer>>();              //ArrayList that contains all possible patterns in the graph
  ArrayList<ArrayList<Integer>> index = new ArrayList<ArrayList<Integer>>();                 //ArrayList that contains all possible patterns that a vertex appears in
  ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();                 //ArrayList that contains possible paths that can be followed after eliminating
  Map<Integer,ArrayList<Integer>> pathIndex = new HashMap<Integer,ArrayList<Integer>>();     //Map that shows the possible vertices that follow a certain vertex
  ArrayList<Integer> path = new ArrayList<Integer>();                                        //Final path to return
  int[] freq;         //Array that contains the frequencies of the nodes

  public WFCL(){}

  //Create patterns from the graph
  public int createPatterns(){
    //Initialize variables and structures
    freq = new int[g.numVertices];
    Arrays.fill(freq,0);
    for(int i = 0; i < g.numVertices; i++){
      ArrayList<Integer> ar = new ArrayList<Integer>();
      index.add(ar);
    }
    int patternsSize = 0;

    //For every vertex, create a pattern by adding the current vertex in the middle
    for(int i = 0; i < g.initialSize; i++){
      ArrayList<Integer> outgoingNeighbors = g.returnOutgoingNeighbors(i);
      ArrayList<Integer> incomingNeighbors = g.returnIncomingNeighbors(i);
      for(int first: incomingNeighbors){
        for(int last: outgoingNeighbors){
          //If the first vertex and the last vertex aren't the same
          if(first != last){
            ArrayList<Integer> pattern = new ArrayList<Integer>(Arrays.asList(first,i,last));
            patterns.add(pattern);

            //Add the pattern to the index
            patternsSize = patterns.size()-1;
            ArrayList<Integer> af = index.get(first);
            af.add(patternsSize);
            index.set(first,af);
            ArrayList<Integer> ai = index.get(i);
            ai.add(patternsSize);
            index.set(i,ai);
            ArrayList<Integer> al = index.get(last);
            al.add(patternsSize);
            index.set(last,al);

            //Increase the vertices' frequencies
            freq[first]++;
            freq[i]++;
            freq[last]++;
          }
        }
      }
    }
    return patternsSize+1;
  }

  //Find the vertex with the lowest frequency
  public int lowestFrequency(){
    int lowestIndex = -1;
    int min = 99999999;
    for(int i = 0; i < g.numVertices; i++){
      if(min >= freq[i] && freq[i] != 0){
        lowestIndex = i;
        min = freq[i];
      }
    }
    return lowestIndex;
  }

  //Find the pattern with the lowest entropy (a pattern containing the lowest frequency vertex)
  public int lowestEntropyPattern(int lowestFrequencyNode){
    Random rand = new Random();

    int outdegree = g.outdegreeOf(lowestFrequencyNode) + g.indegreeOf(lowestFrequencyNode);
    if(outdegree <= 0){
      return -1;
    }

    if(outdegree == 1){
      for(int i: index.get(lowestFrequencyNode)){
        if(patterns.get(i).get(0) == lowestFrequencyNode){
          return i;
        }
      }
    }
    int val = rand.nextInt(index.get(lowestFrequencyNode).size());
    int chosenPatternIndex = index.get(lowestFrequencyNode).get(val);
    return chosenPatternIndex;
  }

  //Creates an index of the possible patterns to be followed
  public void createPathIndex(){
    for(ArrayList<Integer> arr: paths){
      if(!pathIndex.containsKey(arr.get(0))){
        ArrayList<Integer> al = new ArrayList<Integer>();
        al.add(arr.get(1));
        pathIndex.put(arr.get(0), al);
      }else{
        ArrayList<Integer> al = pathIndex.get(arr.get(0));
        if(al.indexOf(arr.get(1)) == -1){
          al.add(arr.get(1));
          pathIndex.replace(arr.get(0), al);
        }
      }
      if(!pathIndex.containsKey(arr.get(1))){
        ArrayList<Integer> al = new ArrayList<Integer>();
        al.add(arr.get(2));
        pathIndex.put(arr.get(1), al);
      }else{
        ArrayList<Integer> al = pathIndex.get(arr.get(1));
        if(al.indexOf(arr.get(2)) == -1){
          al.add(arr.get(2));
          pathIndex.replace(arr.get(1), al);
        }
      }
    }
  }

  //Recursively creates a path
  public void DFS(int vertex, boolean visited[]){
    //Mark the vertex as visited
    visited[vertex] = true;
    //Add the vertex to the path
    path.add(vertex);

    //Get the neighbors of the vertex
    if(!pathIndex.containsKey(vertex)){
      return;
    }
    ArrayList<Integer> neighbors = pathIndex.get(vertex);
    int min = g.initialSize;
    int val = -1;
    //Find the next neighbor to go to
    for(int neighbor: neighbors){
      if(!pathIndex.containsKey(neighbor)){
        return;
      }
      int size = pathIndex.get(neighbor).size();
      if(size < min){
        val = neighbor;
        min = size;
      }
      // val = neighbor;
      if(val == -1){
        return;
      }
      //If the neighbor isn't visited, repeats the function on that vertex
      if(!visited[val]){
        DFS(val, visited);
      }
    }
  }

  //Choose vertex to start
  public int vertexSelection(){
    //Initialize the array to see the visited vertices
    boolean visited[] = new boolean[g.initialSize];

    int vertex = -1;
    int min = g.initialSize;

    //Chooses the vertex and returns it
    for(int val: pathIndex.keySet()){
      int size = pathIndex.get(val).size();
      if(g.outdegreeOf(val) == 1){
        vertex = val;
        return vertex;
      }


      else if(size <= min && size != 0){
        vertex = val;
        min = size;
      }
    }

    return vertex;
  }

  //Returns the resulting path
  public ArrayList<Integer> run(Graph givenGraph){
    //Initialize the Graph g
    g = givenGraph;

    //Find the patterns from the graph
    int patternsSize = createPatterns();

    //While there are patterns to try and an unvisited edge exists
    while(patternsSize != 0 && g.unvisitedEdgeExists()){
      //Find the lowest frequency vertex
      int lowestFrequencyVertex = lowestFrequency();

      if(lowestFrequencyVertex == -1){
        break;
      }

      //Choose a pattern containing the lowest frequency vertex
      int chosenPatternIndex = lowestEntropyPattern(lowestFrequencyVertex);
      ArrayList<Integer> chosenPattern = patterns.get(chosenPatternIndex);

      //Mark the edges of the pattern as visited
      g.markVisitedEdge(chosenPattern.get(0),chosenPattern.get(1));
      g.markVisitedEdge(chosenPattern.get(1),chosenPattern.get(2));

      //Add the pattern to the paths
      paths.add(chosenPattern);

      //Update the values according to the chosen pattern
      for(int i = 0; i < g.initialSize; i++){
        int temp = index.get(i).indexOf(chosenPatternIndex);
        if(temp != -1){
          index.get(i).remove(temp);
          freq[i]--;
        }
      }
      patternsSize--;

      for(int i: chosenPattern){
        if(g.isSaturated(i)){
          ArrayList<Integer> saturatedNodePatterns = new ArrayList<Integer>(index.get(i));
          for(int patternIndex: saturatedNodePatterns){
            for(int v = 0; v < g.initialSize; v++){
              int temp = index.get(v).indexOf(patternIndex);
              if(temp != -1){
                index.get(v).remove(temp);
                freq[v]--;
              }
            }
            patternsSize--;
          }
        }
      }
    }

    //Create an index of paths
    createPathIndex();

    //Select a vertex to start the path
    int vertex = vertexSelection();
    if(vertex == -1){
      path.clear();
      return path;
    }
    boolean visited[] = new boolean[g.initialSize];
    //Recursively create the path
    DFS(vertex,visited);

    return path;

  }


}




















class Main{

  //Returns an ArrayList of graph names in the given directory
  public   ArrayList<String> getGraphNames(String dir, String type){

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
  public   Graph getGraph(File file){
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


  public void main(String[] args) {
    Graph g;           //Graph that will be tested

    //Set the directory of the graphs
    String dir = "/Users/sedef/Desktop/LPP/Graphs/";

    Greedy greedy;    //Declare the Greedy object
    WFCS wfcs;          //Declare the Wfc object
    WFCL wfcl;          //Declare the Wfc object
    long start, end;  //Start and end times for the execution time


    //Set the directory of the csv file to be create
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
      //Set the directory of the tests
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

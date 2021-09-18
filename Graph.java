import java.util.*;


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

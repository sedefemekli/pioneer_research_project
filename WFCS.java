import java.util.*;

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

import java.util.*;

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

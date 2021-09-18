import java.util.*;

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

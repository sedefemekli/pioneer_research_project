import java.util.*;
import java.io.*;
import java.nio.*;

class GraphGenerator{

  public GraphGenerator(){}

  public Graph createUndirectedGraph(int numVert){
    int numVertices = numVert;
    Graph g = new Graph(numVertices);

    Random random = new Random();

    int val = random.nextInt((numVertices * ((numVertices - 1) / 2)) - numVertices/2) + numVertices/2;

    int numEdges = 0;
    int prev = -1;
    int v1 = random.nextInt(numVertices);
    int v2 = -1;
    while(numEdges < val){
      int unvisitedVertex = g.unvisitedVertex();
      if(unvisitedVertex != -1){
        v2 = unvisitedVertex;
        while(v1 == v2 || v2 == prev){
          v2 = random.nextInt(numVertices);
        }
      }else{
        v2 = random.nextInt(numVertices);
        while(v1 == v2 || v2 == prev){
          v2 = random.nextInt(numVertices);
        }
      }
      g.addEdge(v1,v2);
      g.addEdge(v2,v1);
        prev = v1;
        v1 = v2;
        numEdges++;
    }
    g.numEdges /= 2;

    return g;
  }

  public Graph createDirectedGraph(int numVert){
    int numVertices = numVert;
    Graph g = new Graph(numVertices);

    Random random = new Random();

    int val = 2*random.nextInt((numVertices * ((numVertices - 1) / 2)) - numVertices/2) + numVertices/2;

    int numEdges = 0;
    int prev = -1;
    int v1 = random.nextInt(numVertices);
    int v2 = -1;
    while(numEdges < val){
      int unvisitedVertex = g.unvisitedVertex();
      if(unvisitedVertex != -1){
        v2 = unvisitedVertex;
        while(v1 == v2 || v2 == prev){
          v2 = random.nextInt(numVertices);
        }
      }else{
        v2 = random.nextInt(numVertices);
        while(v1 == v2 || v2 == prev){
          v2 = random.nextInt(numVertices);
        }
      }
      g.addEdge(v1,v2);
      prev = v1;
      v1 = v2;
      numEdges++;
    }

    return g;
  }

  // public Graph createDirectedGraph(int numVert){
  //   int numVertices = numVert;
  //   Graph g = new Graph(numVertices);
  //
  //   Random random = new Random();
  //   int val = random.nextInt((numVertices * ((numVertices - 1) / 2)) - numVertices/2) + numVertices/2;
  //
  //   int numEdges = 0;
  //   int prev = -1;
  //
  //   int v1 = random.nextInt(numVertices);
  //   while(numEdges < val){
  //     int v2 = random.nextInt(numVertices);
  //     while(v1 == v2 || v2 == prev){
  //       v2 = random.nextInt(numVertices);
  //     }
  //     g.addEdge(v1,v2);
  //     prev = v1;
  //     v1 = v2;
  //     numEdges++;
  //   }
  //   return g;
  // }

  public Graph getGraph(File file){
    Graph g = new Graph(1);

    try {
      Scanner sc = new Scanner(file);
      ArrayList<String> lines = new ArrayList<String>();
      while(sc.hasNextLine()) {
        lines.add(sc.nextLine());
      }
      sc.close();

      String fileName = file.getName();
      String strNumVertices = fileName;
      strNumVertices = strNumVertices.substring(strNumVertices.indexOf(".") + 1, strNumVertices.length());
      strNumVertices = strNumVertices.substring(0,strNumVertices.indexOf("."));
      int numVertices = Integer.parseInt(strNumVertices);

      g = new Graph(numVertices);
      for(int j = 0; j < numVertices; j++){
        String[] strValues = lines.get(j+1).split(" ");
        for(int i = 0; i < numVertices; i++){
          if(Integer.parseInt(strValues[i]) == 1){
            if(fileName.indexOf("u") != -1){
              g.addEdge(i,j);
            }else{
              g.addEdge(i,j);
              g.addEdge(j,i);
            }

          }
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    return g;

  }

  public Map<String,Graph> getGraphs(String dir){
    Map<String,Graph> map = new HashMap<String,Graph>();
    if(!dir.substring(dir.length()-1).equals("/")) {
  	  dir += "/";
  	}
  	if(!dir.substring(0,1).equals("/")) {
  	  dir = "/" + dir;
  	}
    File directory = new File(dir);
    File[] directoryFiles = directory.listFiles();
    for(File file: directoryFiles){
      if(file.isFile()){
        String name = file.getName();
        if(name.substring(name.length() - 4, name.length()).equals(".txt")){
          Graph g = getGraph(file);
          map.put(name,g);
        }
      }
    }
    return map;
  }
}

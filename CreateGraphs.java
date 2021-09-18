import java.util.*;
import java.io.*;

class CreateGraphs{

  public static Graph generateGraph(int numVert, boolean directed){
    Graph g;
    GraphGenerator gg = new GraphGenerator();
    if(directed){
      g = gg.createDirectedGraph(numVert);
    }
    else{
      g = gg.createUndirectedGraph(numVert);
    }
    return g;
  }

  public static void main(String[] args) {
    int numSamples = 0;
    int maxSamples = 40;
    Random rand = new Random();
    while(numSamples < maxSamples){
      numSamples++;
      System.out.println("Num samples: " + numSamples);
      int numFiles = 0;
      int maxFiles = 100;
      int maxVertices = 50;
      // Random rand = new Random();
      for(int i = 0; i < maxFiles/2; i++){
        System.out.println("UndirectedGraph");
        Graph g = generateGraph(rand.nextInt(maxVertices - 3) + 3,false);
        numFiles++;

        String fileName = "/Users/sedef/Desktop/LPP/Graphs/" + numSamples + "/u." + i +"." + g.numVertices + "." + g.numEdges + ".txt";
        System.out.println("Filename: " + fileName);

        File file = new File(fileName);

        try{
          file.createNewFile();
        }
        catch (Exception e) {
          e.getStackTrace();
        }


        String data = "";
        data = "Graph (V: "+ g.numVertices+",E: "+ g.numEdges+")\n";
        for(int k = 0; k < g.initialSize; k++){
          for(int j = 0; j < g.initialSize; j++){
            data = data + g.grid[k][j] + " ";
          }
          data += "\n";
        }

        try {
          FileWriter output = new FileWriter(fileName);
          output.write(data);
          output.close();
        } catch(Exception e) {
          e.getStackTrace();
        }
      }
      for(int i = 0; i < maxFiles/2; i++){
        System.out.println("DirectedGraph");
        Graph g = generateGraph(rand.nextInt(maxVertices - 3) + 3,true);
        numFiles++;

        String fileName = "/Users/sedef/Desktop/LPP/Graphs/" + numSamples + "/d." + (i+50) +"." + g.numVertices + "." + g.numEdges + ".txt";
        System.out.println("Filename: " + fileName);

        File file = new File(fileName);

        try{
          file.createNewFile();
        }
        catch (Exception e) {
          e.getStackTrace();
        }


        String data = "";
        data = "Graph (V: "+ g.numVertices+",E: "+ g.numEdges+")\n";
        for(int k = 0; k < g.initialSize; k++){
          for(int j = 0; j < g.initialSize; j++){
            data = data + g.grid[k][j] + " ";
          }
          data += "\n";
        }

        try {
          FileWriter output = new FileWriter(fileName);
          output.write(data);
          output.close();
        } catch(Exception e) {
          e.getStackTrace();
        }
      }

    }




    // int numFiles = 0;
    // int maxFiles = 100;
    // int maxVertices = 50;
    // Random rand = new Random();
    // for(int i = 0; i < maxFiles/2; i++){
    //   System.out.println("UndirectedGraph");
    //   Graph g = generateGraph(rand.nextInt(maxVertices - 3) + 3,false);
    //   numFiles++;
    //
    //   String fileName = "/Users/sedef/Desktop/LPP/Graphs/" + i + "/u." + g.numVertices + "." + g.numEdges + ".txt";
    //   System.out.println("Filename: " + fileName);
    //
    //   File file = new File(fileName);
    //
    //   try{
    //     file.createNewFile();
    //   }
    //   catch (Exception e) {
    //     e.getStackTrace();
    //   }
    //
    //
    //   String data = "";
    //   data = "Graph (V: "+ g.numVertices+",E: "+ g.numEdges+")\n";
    //   for(int k = 0; k < g.initialSize; k++){
    //     for(int j = 0; j < g.initialSize; j++){
    //       data = data + g.grid[k][j] + " ";
    //     }
    //     data += "\n";
    //   }
    //
    //   try {
    //     FileWriter output = new FileWriter(fileName);
    //     output.write(data);
    //     output.close();
    //   } catch(Exception e) {
    //     e.getStackTrace();
    //   }
    // }
    // for(int i = 50; i < maxFiles; i++){
    //   System.out.println("DirectedGraph");
    //   Graph g = generateGraph(rand.nextInt(maxVertices - 3) + 3,true);
    //   numFiles++;
    //
    //   String fileName = "/Users/sedef/Desktop/LPP/Graphs/" + i + "/d." + g.numVertices + "." + g.numEdges + ".txt";
    //   System.out.println("Filename: " + fileName);
    //
    //   File file = new File(fileName);
    //
    //   try{
    //     file.createNewFile();
    //   }
    //   catch (Exception e) {
    //     e.getStackTrace();
    //   }
    //
    //
    //   String data = "";
    //   data = "Graph (V: "+ g.numVertices+",E: "+ g.numEdges+")\n";
    //   for(int k = 0; k < g.initialSize; k++){
    //     for(int j = 0; j < g.initialSize; j++){
    //       data = data + g.grid[k][j] + " ";
    //     }
    //     data += "\n";
    //   }
    //
    //   try {
    //     FileWriter output = new FileWriter(fileName);
    //     output.write(data);
    //     output.close();
    //   } catch(Exception e) {
    //     e.getStackTrace();
    //   }
    // }

  }
}

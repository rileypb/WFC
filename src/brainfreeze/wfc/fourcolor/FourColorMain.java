package brainfreeze.wfc.fourcolor;

import java.awt.Color;
import java.util.Map;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedGraph;

import brainfreeze.wfc.Edge;
import brainfreeze.wfc.WaveFunctionCollapse;

public class FourColorMain
{
  public static int SEED = 7;

  public static final float edgeTolerance = 0.01f;
  public static final int swatchWidth = 8;
  public static final int swatchHeight = 8;

  public static void main(String[] args)
  {
    Graph<String, Edge> graph = new DefaultUndirectedGraph<>(Edge.class);
    graph.addVertex("Virginia");
    graph.addVertex("West Virginia");
    graph.addVertex("North Carolina");
    graph.addVertex("Kentucky");
    graph.addVertex("Tennessee");
    graph.addVertex("South Carolina");
    graph.addVertex("Georgia");
    graph.addVertex("Ohio");
    graph.addVertex("Maryland");
    graph.addVertex("DC");

    graph.addEdge("Virginia", "West Virginia");
    graph.addEdge("Virginia", "Maryland");
    graph.addEdge("Virginia", "DC");
    graph.addEdge("Virginia", "Kentucky");
    graph.addEdge("Virginia", "Tennessee");
    graph.addEdge("Virginia", "North Carolina");

    graph.addEdge("West Virginia", "Ohio");
    graph.addEdge("West Virginia", "Kentucky");
    graph.addEdge("West Virginia", "Maryland");

    graph.addEdge("North Carolina", "Tennessee");
    graph.addEdge("North Carolina", "South Carolina");
    graph.addEdge("North Carolina", "Georgia");

    graph.addEdge("Kentucky", "Ohio");
    graph.addEdge("Kentucky", "Tennessee");

    graph.addEdge("Tennessee", "Georgia");

    graph.addEdge("South Carolina", "Georgia");

    graph.addEdge("Maryland", "DC");

    FourColorRules<String> rules = new FourColorRules<>(Color.red, "red", Color.blue, "blue",
        Color.green, "green", Color.orange, "orange", graph);
    WaveFunctionCollapse<String, FourColorTile, Edge> wfc = new WaveFunctionCollapse<String, FourColorTile, Edge>(
        rules, graph, null, 0, 0, null);

    // Random rnd = new Random(SEED);
    Random rnd = new Random();
    Map<String, FourColorTile> result = wfc.run(rnd, 1);
    System.out.println(result);
  }

}

package brainfreeze.wfc.fourcolor;

import java.awt.Color;
import java.util.Collection;
import java.util.Set;

import org.jgrapht.Graph;

import brainfreeze.wfc.CandidateRules;
import brainfreeze.wfc.Edge;
import brainfreeze.wfc.Multimap;
import brainfreeze.wfc.TileMap;

public class FourColorRules<T> implements CandidateRules<T, FourColorTile>
{
  private Color c1;
  private String n1;
  private Color c2;
  private String n2;
  private Color c3;
  private String n3;
  private Color c4;
  private String n4;
  private Graph<T, Edge> graph;

  public FourColorRules(Color c1, String n1, Color c2, String n2, Color c3, String n3, Color c4,
      String n4, Graph<T, Edge> graph)
  {
    this.c1 = c1;
    this.n1 = n1;
    this.c2 = c2;
    this.n2 = n2;
    this.c3 = c3;
    this.n3 = n3;
    this.c4 = c4;
    this.n4 = n4;
    this.graph = graph;
  }

  @Override
  public void eliminate(Multimap<T, FourColorTile> candidates, T coord, FourColorTile tile, TileMap<T, FourColorTile> map)
  {
    candidates.get(coord).clear();
    candidates.get(coord).add(tile);
    Set<Edge> outgoing = graph.outgoingEdgesOf(coord);
    for (Edge e : outgoing) {
      T opposite = getOppositeVertex(e, coord);
      candidates.get(opposite).remove(tile);
    }
  }

  private T getOppositeVertex(Edge e, T coord)
  {
    if (graph.getEdgeTarget(e).equals(coord)) {
      return graph.getEdgeSource(e);
    }
    return graph.getEdgeTarget(e);
  }

  @Override
  public void setInitialCandidates(Multimap<T, FourColorTile> candidates, Collection<T> coords)
  {
    FourColorTile t1 = new FourColorTile(c1, n1);
    FourColorTile t2 = new FourColorTile(c2, n2);
    FourColorTile t3 = new FourColorTile(c3, n3);
    FourColorTile t4 = new FourColorTile(c4, n4);
    
    for (T coord : coords) {
      candidates.add(coord, t1);
      candidates.add(coord, t2);
      candidates.add(coord, t3);
      candidates.add(coord, t4);
    }
  }

}

package brainfreeze.wfc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JComponent;

import org.jgrapht.Graph;

import brainfreeze.wfc.pic.Swatch;

public class WaveFunctionCollapse<T, V extends Tile, E extends Edge, H extends WFCHandler>
{
  private CandidateRules<T, V> rules;
  private Graph<T, E> graph;
  private H handler;

  public WaveFunctionCollapse(CandidateRules<T, V> rules, Graph<T, E> graph, H handler)
  {
    this.rules = rules;
    this.graph = graph;
    this.handler = handler;
  }

  public Map<T, V> run(Random rnd)
  {
    Graphics2D g = null;
    TileMapWithCandidates<T, V> map = new TileMapWithCandidates<>(graph.vertexSet(), rules);

    List<T> lowestEntropyCells = map.findLowestEntropy();

    int lastCellU = -1;
    int lastCellV = -1;
    T lastCoord = null;

    while (lowestEntropyCells.size() > 0)
    {
      if (map.size() == graph.vertexSet().size())
      {
        break;
      }

      T coord = randomCell(lowestEntropyCells, rnd);
      List<V> candidates = map.getCandidates(coord);
      V randomCandidate = randomCandidate(candidates, rnd);
      if (randomCandidate == null)
      {
        System.out.println("no candidate");
      }
      map.set(coord, randomCandidate);
      lastCoord = coord;
      lowestEntropyCells = map.findLowestEntropy();
      
      handler.after(map, graph, coord, randomCandidate);
    }

    return map.asMap();
  }

  private V randomCandidate(List<V> candidates, Random rnd)
  {
    if (candidates.size() == 0)
    {
      return null;
    }
    return candidates.get(rnd.nextInt(candidates.size()));
  }

  private T randomCell(List<T> lowestEntropyCells, Random rnd)
  {
    return lowestEntropyCells.get(rnd.nextInt(lowestEntropyCells.size()));
  }
}

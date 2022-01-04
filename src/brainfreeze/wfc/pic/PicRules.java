package brainfreeze.wfc.pic;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import brainfreeze.wfc.CandidateRules;
import brainfreeze.wfc.Coord;
import brainfreeze.wfc.Multimap;
import brainfreeze.wfc.TileMap;

public class PicRules implements CandidateRules<Coord, Swatch>
{
  private Graph<Swatch, Object> candidateGraphHorizontal;
  private Graph<Swatch, Object> candidateGraphVertical;
  private Set<Swatch> swatches = new HashSet<Swatch>();

  private BufferedImage img;
  private Graph<Coord, DirectedEdge> relationshipGraph;
  private int tileWidth;
  private int tileHeight;

  public PicRules(BufferedImage img, int tileWidth, int tileHeight,
      Graph<Coord, DirectedEdge> graph)
  {
    this.tileWidth = tileWidth;
    this.img = img;
    this.tileHeight = tileHeight;
    this.relationshipGraph = graph;
    buildCandidateGraphs();
  }

  private void buildCandidateGraphs()
  {
    System.out.println("building candidate graph...");
    candidateGraphHorizontal = new DefaultDirectedGraph<>(Object.class);
    candidateGraphVertical = new DefaultDirectedGraph<>(Object.class);
    int width = img.getWidth(null);
    int height = img.getHeight(null);
    if (PicMain.ROTATIONS && tileWidth != tileHeight) {
      throw new IllegalArgumentException("Rotated images must have tileWidth == tileHeight");
    }
    if (width % tileWidth != 0 || height % tileHeight != 0)
    {
      throw new IllegalArgumentException(
          "Tile size must divide image width and height evenly: " + width + "x" + height);
    }
    int numX = width / tileWidth;
    int numY = height / tileHeight;

    BufferedImage r90 = null;
    BufferedImage r180 = null;
    BufferedImage r270 = null;
    
    int multiplier = 1;

    if (PicMain.ROTATIONS)
    {
      r90 = new BufferedImage(height, width, BufferedImage.TYPE_INT_BGR);
      Graphics2D g = r90.createGraphics();
      AffineTransform rotateInstance = AffineTransform.getRotateInstance(Math.PI/2);
      rotateInstance.translate(0, -height);
      g.drawImage(img, rotateInstance, null);
      r180 = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
      g = r180.createGraphics();
      rotateInstance = AffineTransform.getRotateInstance(Math.PI);
      rotateInstance.translate(-width, -height);
      g.drawImage(img, rotateInstance, null);
      r180.flush();
      r270 = new BufferedImage(height, width, BufferedImage.TYPE_INT_BGR);
      g = r270.createGraphics();
      rotateInstance = AffineTransform.getRotateInstance(3 * Math.PI / 2);
      rotateInstance.translate(-width, 0);
      g.drawImage(img, rotateInstance, null);
      r270.flush();
      multiplier *= 4;
    }

    int numTiles = multiplier * numX * numY;
    int numTotal = numTiles * numTiles;
    int numSoFar = 0;

    for (int u = 0; u < numX; u++)
    {
      for (int v = 0; v < numY; v++)
      {
        Swatch swatch = new Swatch(img, u * tileWidth, v * tileHeight, tileWidth, tileHeight);
        swatches.add(swatch);
        candidateGraphHorizontal.addVertex(swatch);
        candidateGraphVertical.addVertex(swatch);
        if (r90 != null) {
          swatch = new Swatch(r90, v * tileHeight, u * tileWidth, tileHeight, tileWidth);
          swatches.add(swatch);
          candidateGraphHorizontal.addVertex(swatch);
          candidateGraphVertical.addVertex(swatch);
        }
        if (r180 != null) {
          swatch = new Swatch(r180, u * tileWidth, v * tileHeight, tileWidth, tileHeight);
          swatches.add(swatch);
          candidateGraphHorizontal.addVertex(swatch);
          candidateGraphVertical.addVertex(swatch);
        }
        if (r270 != null) {
          swatch = new Swatch(r270, v * tileHeight, u*tileWidth, tileHeight, tileWidth);
          swatches.add(swatch);
          candidateGraphHorizontal.addVertex(swatch);
          candidateGraphVertical.addVertex(swatch);
        }
      }
    }
    System.out.println("analyzing tiles...");
    Map<Swatch, int[]> westEdges = new HashMap<Swatch, int[]>();
    Map<Swatch, int[]> northEdges = new HashMap<Swatch, int[]>();
    Map<Swatch, int[]> eastEdges = new HashMap<Swatch, int[]>();
    Map<Swatch, int[]> southEdges = new HashMap<Swatch, int[]>();
    for (Swatch swatch : swatches)
    {
      int[] eastFringe = swatch.getEastFringe();
      int[] southFringe = swatch.getSouthFringe();
      int[] westFringe = swatch.getWestFringe();
      int[] northFringe = swatch.getNorthFringe();
      for (Swatch swatch2 : swatches)
      {
        int[] westEdge = westEdges.get(swatch2);
        if (westEdge == null)
        {
          westEdge = swatch2.getWestEdge();
          westEdges.put(swatch2, westEdge);
        }
        if (Arrays.equals(eastFringe, westEdge))
        {
          candidateGraphHorizontal.addEdge(swatch, swatch2);
        }
        int[] northEdge = northEdges.get(swatch2);
        if (northEdge == null)
        {
          northEdge = swatch2.getNorthEdge();
          northEdges.put(swatch2, northEdge);
        }
        if (Arrays.equals(southFringe, northEdge))
        {
          candidateGraphVertical.addEdge(swatch, swatch2);
        }

        int[] eastEdge = eastEdges.get(swatch2);
        if (eastEdge == null)
        {
          eastEdge = swatch2.getEastEdge();
          eastEdges.put(swatch2, eastEdge);
        }
        if (Arrays.equals(westFringe, eastEdge))
        {
          candidateGraphHorizontal.addEdge(swatch2, swatch);
        }
        int[] southEdge = southEdges.get(swatch2);
        if (southEdge == null)
        {
          southEdge = swatch2.getSouthEdge();
          southEdges.put(swatch2, southEdge);
        }
        if (Arrays.equals(northFringe, southEdge))
        {
          candidateGraphVertical.addEdge(swatch2, swatch);
        }
      }
      if (candidateGraphHorizontal.outgoingEdgesOf(swatch).size() == 0)
      {
        System.out.println("no horizontal matches");
      }
      if (candidateGraphVertical.outgoingEdgesOf(swatch).size() == 0)
      {
        System.out.println("no vertical matches");
      }
      numSoFar += numTiles;
      System.out.println("so far " + numSoFar + "/" + numTotal);
    }
    System.out.println("candidate graph built.");
  }

  @Override
  public void eliminate(Multimap<Coord, Swatch> candidates, Coord coord, Swatch tile,
      TileMap<Coord, Swatch> map)
  {
    candidates.clear(coord);
    candidates.add(coord, tile);
    Set<DirectedEdge> incomingEdges = relationshipGraph.incomingEdgesOf(coord);
    for (DirectedEdge edge : incomingEdges)
    {
      Coord otherCoord = relationshipGraph.getEdgeSource(edge);
      if (map.get(otherCoord) != null)
      {
        continue;
      }
      // System.out.println(otherCoord + " -> candidates " + candidates.get(otherCoord));
      switch (edge.dir)
      {
        case "horizontal":
          Set<Object> incomingCandidates = candidateGraphHorizontal.incomingEdgesOf(tile);
          Set<Swatch> possibilities = new HashSet<Swatch>();
          for (Object e : incomingCandidates)
          {
            possibilities.add(candidateGraphHorizontal.getEdgeSource(e));
          }
          candidates.retainAll(otherCoord, possibilities);
          break;

        case "vertical":
          Set<Object> incomingCandidates2 = candidateGraphVertical.incomingEdgesOf(tile);
          Set<Swatch> possibilities2 = new HashSet<Swatch>();
          for (Object e : incomingCandidates2)
          {
            possibilities2.add(candidateGraphVertical.getEdgeSource(e));
          }
          candidates.retainAll(otherCoord, possibilities2);
          break;

        default:
          throw new RuntimeException("invalid direction: " + edge.dir);
      }
      if (candidates.get(otherCoord).size() == 0)
      {
        throw new Deadend();
      }
    }

    Set<DirectedEdge> outgoingEdges = relationshipGraph.outgoingEdgesOf(coord);
    for (DirectedEdge edge : outgoingEdges)
    {
      Coord otherCoord = relationshipGraph.getEdgeTarget(edge);
      if (map.get(otherCoord) != null)
      {
        continue;
      }
      switch (edge.dir)
      {
        case "horizontal":
          Set<Object> outgoingCandidates = candidateGraphHorizontal.outgoingEdgesOf(tile);
          Set<Swatch> possibilities = new HashSet<Swatch>();
          for (Object e : outgoingCandidates)
          {
            possibilities.add(candidateGraphHorizontal.getEdgeTarget(e));
          }
          // System.out.println("possibilities: " + possibilities);
          candidates.retainAll(otherCoord, possibilities);
          break;

        case "vertical":
          Set<Object> outgoingCandidates2 = candidateGraphVertical.outgoingEdgesOf(tile);
          Set<Swatch> possibilities2 = new HashSet<Swatch>();
          for (Object e : outgoingCandidates2)
          {
            possibilities2.add(candidateGraphVertical.getEdgeTarget(e));
          }
          // System.out.println("possibilities: " + possibilities2);
          candidates.retainAll(otherCoord, possibilities2);
          break;

        default:
          throw new RuntimeException("invalid direction: " + edge.dir);
      }
      if (candidates.get(otherCoord).size() == 0)
      {
        throw new Deadend();
      }
    }
  }

  @Override
  public void setInitialCandidates(Multimap<Coord, Swatch> candidates, Collection<Coord> coords)
  {
    for (Coord coord : coords)
    {
      for (Swatch swatch : candidateGraphHorizontal.vertexSet())
      {
        candidates.add(coord, swatch);
      }
    }
  }

}

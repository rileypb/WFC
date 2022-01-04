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

public class WaveFunctionCollapse<T, V extends Tile, E extends Edge>
{
  private CandidateRules<T, V> rules;
  private Graph<T, E> graph;
  private BufferedImage im;
  private JComponent component;
  private int tileWidth;
  private int tileHeight;

  public WaveFunctionCollapse(CandidateRules<T, V> rules, Graph<T, E> graph, BufferedImage im,
      int tileWidth, int tileHeight, JComponent component)
  {
    this.rules = rules;
    this.graph = graph;
    this.im = im;
    this.tileWidth = tileWidth;
    this.tileHeight = tileHeight;
    this.component = component;
  }

  public Map<T, V> run(Random rnd, int scale)
  {
    Graphics2D g = null;
    TileMapWithCandidates<T, V> map = new TileMapWithCandidates<>(graph.vertexSet(), rules);

    if (im != null)
    {
      g = (Graphics2D) im.getGraphics();
      g.scale(scale, scale);
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
      g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
      int numCan = Math.min(255, map.getCandidates(graph.vertexSet().iterator().next()).size());
      Color clr = new Color(255 - numCan, numCan, 0);
      g.setColor(clr);
      g.fillRect(0, 0, im.getWidth(), im.getHeight());
      component.repaint();
    }
    
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
      // System.out.println("Place " + coord + " -> " + randomCandidate);
      if (im != null)
      {
        if (lastCoord != null)
        {
          Swatch swatch = (Swatch) map.get(lastCoord);
          swatch.draw(g, lastCellU, lastCellV);
        }
        g.setColor(Color.MAGENTA);
        int u = tileWidth * ((Coord) coord).i;
        int v = tileHeight * ((Coord) coord).j;
        
        for (T t : graph.vertexSet()) {
          Coord c = (Coord) t;
          int u1 = tileWidth * ((Coord) c).i;
          int v1 = tileHeight * ((Coord) c).j;
          if (map.get(t) == null) {
            int numCan = Math.min(255, map.getCandidates(t).size());
            Color clr = new Color(255 - numCan, numCan, 0);
            g.setColor(clr);
            g.fillRect(u1, v1, tileWidth, tileHeight);
          }
        }

//        ((Swatch) randomCandidate).draw(g, u, v);
//        if (map.get(((T) new Coord(((Coord) coord).i - 1, ((Coord) coord).j, 0))) == null)
//        {
//          g.fillRect(u - tileWidth, v, tileWidth, tileHeight);
//        }
//        if (map.get(((T) new Coord(((Coord) coord).i + 1, ((Coord) coord).j, 0))) == null)
//        {
//          g.fillRect(u + tileWidth, v, tileWidth, tileHeight);
//        }
//        if (map.get(((T) new Coord(((Coord) coord).i, ((Coord) coord).j - 1, 0))) == null)
//        {
//          g.fillRect(u, v - tileHeight, tileWidth, tileHeight);
//        }
//        if (map.get(((T) new Coord(((Coord) coord).i, ((Coord) coord).j + 1, 0))) == null)
//        {
//          g.fillRect(u, v + tileHeight, tileWidth, tileHeight);
//        }

        Swatch rc = (Swatch) randomCandidate;
        rc.draw(g, u, v);
        g.setColor(Color.blue);
        g.drawRect(u + 1, v + 1, tileWidth - 2, tileHeight - 2);
        component.repaint();
         try
         {
         Thread.sleep(1);
         }
         catch (InterruptedException e)
         {
         // TODO Auto-generated catch block
         e.printStackTrace();
         }
        lastCellU = u;
        lastCellV = v;
      }
      map.set(coord, randomCandidate);
      lastCoord = coord;
      lowestEntropyCells = map.findLowestEntropy();
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

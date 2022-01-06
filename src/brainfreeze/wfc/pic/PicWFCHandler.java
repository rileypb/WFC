package brainfreeze.wfc.pic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import org.jgrapht.Graph;

import brainfreeze.wfc.Coord;
import brainfreeze.wfc.Edge;
import brainfreeze.wfc.Tile;
import brainfreeze.wfc.TileMapWithCandidates;
import brainfreeze.wfc.WFCHandler;

public class PicWFCHandler implements WFCHandler<Coord, Swatch, DirectedEdge>
{
  private BufferedImage im;
  private JComponent component;
  private int tileWidth;
  private int tileHeight;
  private int scale;
  private int lastCellU;
  private int lastCellV;
  private Graphics2D g;
  private Coord lastCoord;
   
  public PicWFCHandler(BufferedImage im, JComponent component, int tileWidth, int tileHeight, int scale)
  {
    super();
    this.im = im;
    this.component = component;
    this.tileWidth = tileWidth;
    this.tileHeight = tileHeight;
    this.scale = scale;
    
    g = (Graphics2D) im.getGraphics();
    g.scale(scale, scale);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    
    lastCellU = -1;
    lastCellV = -1;
    lastCoord = null;
  }
  
  @Override
  public void first(TileMapWithCandidates<Coord, Swatch> map, Graph<Coord, DirectedEdge> graph) {
    int numCan = Math.min(255, map.getCandidates(graph.vertexSet().iterator().next()).size());
    Color clr = new Color(255 - numCan, numCan, 0);
    g.setColor(clr);
    g.fillRect(0, 0, im.getWidth(), im.getHeight());
    component.repaint();
  }

  @Override
  public void after(TileMapWithCandidates<Coord, Swatch> map, Graph<Coord, DirectedEdge> graph,
      Coord coord, Swatch candidate)
  {
    if (lastCoord != null)
    {
      Swatch swatch = (Swatch) map.get(lastCoord);
      swatch.draw(g, lastCellU, lastCellV);
    }
    g.setColor(Color.MAGENTA);
    int u = tileWidth * ((Coord) coord).i;
    int v = tileHeight * ((Coord) coord).j;
    
    for (Coord c : graph.vertexSet()) {
      int u1 = tileWidth * ((Coord) c).i;
      int v1 = tileHeight * ((Coord) c).j;
      if (map.get(c) == null) {
        int numCan = Math.min(255, map.getCandidates(c).size());
        Color clr = new Color(255 - numCan, numCan, 0);
        g.setColor(clr);
        g.fillRect(u1, v1, tileWidth, tileHeight);
      }
    }
    candidate.draw(g, u, v);
    g.setColor(Color.blue);
    g.drawRect(u + 1, v + 1, tileWidth - 2, tileHeight - 2);
    component.repaint();
    lastCoord = coord;
    lastCellU = u;
    lastCellV = v;
  }

}



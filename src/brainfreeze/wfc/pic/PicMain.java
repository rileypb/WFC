package brainfreeze.wfc.pic;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import brainfreeze.wfc.Coord;
import brainfreeze.wfc.WaveFunctionCollapse;

public class PicMain
{
  public static final int OUTPUT_REPEAT = 4;
  public static final boolean WRAP_SOURCE = false;
  public static final boolean WRAP_TARGET = true;
  public static final boolean REVERSE_Y_COPY = true;
  public static final boolean ROTATIONS = true;
//  public static final boolean REFLECTIONS = true;
  public static boolean POSTERIZE = true;
  public static int POSTER_THRESHOLD = 64;
  static int SCALE = 4;
  static int SEED = 0;
  static final int TILE_WIDTH = 5;
  static final int TILE_HEIGHT = 5;
  static final int CELLS_ACROSS = 10;
  static final int CELLS_DOWN = 10;

  public static void main(String[] args) throws IOException
  {
    // System.setOut(new PrintStream(new OutputStream()
    // {
    //
    // @Override
    // public void write(int b) throws IOException
    // {
    // // TODO Auto-generated method stub
    //
    // }
    // }));
    BufferedImage img = ImageIO.read(new File("branches6.png"));
    new Thread(() -> {
      while (img.getWidth() == -1)
      {
      }
      go(img);
    }).run();
  }

  private static void go(BufferedImage img)
  {
    BufferedImage im = new BufferedImage(TILE_WIDTH * CELLS_ACROSS * SCALE * OUTPUT_REPEAT,
        TILE_HEIGHT * CELLS_DOWN * SCALE * OUTPUT_REPEAT, BufferedImage.TYPE_INT_RGB);
    JFrame frame = new JFrame("WFC");
    frame.setSize(TILE_WIDTH * CELLS_ACROSS * SCALE, TILE_HEIGHT * CELLS_DOWN * SCALE + 28);
    JPanel drawPanel = new JPanel()
    {
      @Override
      public void paint(Graphics g)
      {
        super.paint(g);
        g.drawImage(im, 0, 0, null);
      }
    };
    frame.setContentPane(drawPanel);
    frame.setVisible(true);
    Graph<Coord, DirectedEdge> graph = new DefaultDirectedGraph<Coord, DirectedEdge>(
        DirectedEdge.class);
    buildGraph(graph, CELLS_ACROSS, CELLS_DOWN, TILE_WIDTH, TILE_HEIGHT);
    PicRules rules = new PicRules(img, TILE_WIDTH, TILE_HEIGHT, graph);
    WaveFunctionCollapse<Coord, Swatch, DirectedEdge> wfc = new WaveFunctionCollapse<Coord, Swatch, DirectedEdge>(
        rules, graph, im, TILE_WIDTH, TILE_HEIGHT, drawPanel);

    SEED = new Random().nextInt();
    Random rnd = new Random(SEED);
    boolean done = false;
    Map<Coord, Swatch> result = null;
    while (!done)
    {
      try
      {
        result = wfc.run(rnd, SCALE);
        // if (result.size() == CELLS_ACROSS * CELLS_DOWN)
        // {
        // done = true;
        // } else {
        // System.out.println("wrong size");
        // }

        Toolkit.getDefaultToolkit().beep();
        System.out.println(result);

        BufferedImage outImg = new BufferedImage(SCALE * CELLS_ACROSS * TILE_WIDTH * OUTPUT_REPEAT,
            SCALE * CELLS_DOWN * TILE_HEIGHT * OUTPUT_REPEAT, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = (Graphics2D) outImg.getGraphics();
        g.scale(SCALE, SCALE);
        for (Coord coord : result.keySet())
        {
          int u = TILE_WIDTH * coord.i;
          int v = TILE_HEIGHT * coord.j;
          for (int i = 0; i < OUTPUT_REPEAT; i++) {
            for (int j = 0; j < OUTPUT_REPEAT; j++) {
              result.get(coord).draw(g, u + i * CELLS_ACROSS * TILE_WIDTH, v + j * CELLS_DOWN * TILE_HEIGHT);
            }
          }
          System.out.println(coord + " -> " + result.get(coord));
        }
        try
        {
          ImageIO.write(outImg, "png",
              new File("out" + System.currentTimeMillis() + "_" + SEED + ".png"));
        }
        catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        try
        {
          Thread.sleep(5000);
        }
        catch (InterruptedException e1)
        {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
      catch (Deadend e)
      {
        // System.out.println("deadend");
        try
        {
          Thread.sleep(1);
        }
        catch (InterruptedException e1)
        {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    }
  }

  private static void buildGraph(Graph<Coord, DirectedEdge> graph, int xTiles, int yTiles,
      int tileWidth, int tileHeight)
  {

    for (int u = 0; u < xTiles; u++)
    {
      for (int v = 0; v < yTiles; v++)
      {
        graph.addVertex(new Coord(u, v, 0));
      }
    }
    for (int u = 0; u < xTiles; u++)
    {
      for (int v = 0; v < yTiles; v++)
      {
        if (u < xTiles - 1)
        {
          graph.addEdge(new Coord(u, v, 0), new Coord(u + 1, v, 0), new DirectedEdge("horizontal"));
        }
        else if (WRAP_TARGET)
        {
          graph.addEdge(new Coord(u, v, 0), new Coord(0, v, 0), new DirectedEdge("horizontal"));
        }
        if (v < yTiles - 1)
        {
          graph.addEdge(new Coord(u, v, 0), new Coord(u, v + 1, 0), new DirectedEdge("vertical"));
        }
        else if (WRAP_TARGET)
        {
          graph.addEdge(new Coord(u, v, 0), new Coord(u, 0, 0), new DirectedEdge("vertical"));
        }
      }
    }
  }

}

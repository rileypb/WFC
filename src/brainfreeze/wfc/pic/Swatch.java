package brainfreeze.wfc.pic;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Arrays;

import brainfreeze.wfc.Tile;

public class Swatch implements Tile
{
  

  private BufferedImage img;
  private int x;
  private int y;
  private int width;
  private int height;

  public Swatch(BufferedImage img, int x, int y, int width, int height)
  {
    this.img = img;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public int[] getEastFringe()
  {
    Raster data = img.getData();
    int[] a = new int[height];
    if (x + width + 1 >= img.getWidth(null) && !PicMain.WRAP_SOURCE) {
      return null;
    }
    if (x + width + 1 >= img.getWidth(null))
    {
      for (int i = 0; i < height; i++)
      {
        a[i] = RGBtoInt(data, 0, y + i);
      }
    }
    else
    {
      for (int i = 0; i < height; i++)
      {
        a[i] = RGBtoInt(data, x + width, y + i);
      }
    }
    return a;
  }

  public int[] getWestFringe()
  {
    Raster data = img.getData();
    int[] a = new int[height];
    if (x - 1 < 0 && !PicMain.WRAP_SOURCE)
    {
      return null;
    }
    if (x - 1 < 0)
    {
      for (int i = 0; i < height; i++)
      {
        a[i] = RGBtoInt(data, img.getWidth() - 1, y + i);
      }
    }
    else
    {
      for (int i = 0; i < height; i++)
      {
        a[i] = RGBtoInt(data, x - 1, y + i);
      }
    }
    return a;
  }

  public int[] getWestEdge()
  {
    Raster data = img.getData();
    int[] a = new int[height];
    for (int i = 0; i < height; i++)
    {
      a[i] = RGBtoInt(data, x, y + i);
    }
    return a;
  }

  public int[] getEastEdge()
  {
    Raster data = img.getData();
    int[] a = new int[height];
    for (int i = 0; i < height; i++)
    {
      a[i] = RGBtoInt(data, x + width - 1, y + i);
    }
    return a;
  }

  public int[] getSouthFringe()
  {
    Raster data = img.getData();
    int[] a = new int[width];
    if (y + height + 1 >= img.getHeight() && !PicMain.WRAP_SOURCE)
    {
      return null;
    }
    if (y + height + 1 >= img.getHeight())
    {
      for (int i = 0; i < width; i++)
      {
        a[i] = RGBtoInt(data, x + i, 0);
      }
    }
    else
    {
      for (int i = 0; i < width; i++)
      {
        a[i] = RGBtoInt(data, x + i, y + height);
      }
    }
    return a;
  }

  public int[] getNorthFringe()
  {
    Raster data = img.getData();
    int[] a = new int[width];
    if (y - 1 < 0 && !PicMain.WRAP_SOURCE)
    {
      return null;
    }
    if (y - 1 < 0)
    {
      for (int i = 0; i < width; i++)
      {
        a[i] = RGBtoInt(data, x + i, img.getHeight() - 1);
      }
    }
    else
    {
      for (int i = 0; i < width; i++)
      {
        a[i] = RGBtoInt(data, x + i, y - 1);
      }
    }
    return a;
  }

  public int[] getNorthEdge()
  {
    Raster data = img.getData();
    int[] a = new int[width];
    for (int i = 0; i < width; i++)
    {
      a[i] = RGBtoInt(data, x + i, y);
    }
    return a;
  }

  public int[] getSouthEdge()
  {
    Raster data = img.getData();
    int[] a = new int[width];
    for (int i = 0; i < width; i++)
    {
      a[i] = RGBtoInt(data, x + i, y + height - 1);
    }
    return a;
  }

  private int RGBtoInt(Raster data, int x, int y)
  {
    int[] pixel = data.getPixel(x, y, (int[]) null);
    for (int i = 0; i < 3; i++) {
      if (PicMain.POSTERIZE) {
        pixel[i] = (pixel[i]/PicMain.POSTER_THRESHOLD)*PicMain.POSTER_THRESHOLD;
      }
//      if (pixel[i] < 128 && PicMain.POSTERIZE) {
//        pixel[i] = 0;
//      }
//      if (pixel[i] > 127 && PicMain.POSTERIZE) {
//        pixel[i] = 255;
//      }
    }
    return ((pixel[2] << 8) + pixel[1]) << 8 + pixel[0];
  }

  public void draw(Graphics g, int u, int v)
  {
    g.drawImage(img, u, v, u + width, v + height, x, y, x + width, y + height, null);
  }

  @Override
  public String toString()
  {
    return String.format("(%d, %d, %d, %d)", x, y, width, height);
  }

  public void draw(Graphics g, int u, int v, boolean reverse)
  {
    g.drawImage(img, u + width, v, u, v + height, x, y, x + width, y + height, null);
  }



}

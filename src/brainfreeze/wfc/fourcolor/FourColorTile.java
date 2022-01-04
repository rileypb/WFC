package brainfreeze.wfc.fourcolor;

import java.awt.Color;

import brainfreeze.wfc.Tile;

public class FourColorTile implements Tile
{
  public final Color color;
  public final String name;

  public FourColorTile(Color color, String name)
  {
    super();
    this.color = color;
    this.name = name;
  }
  
  @Override
  public String toString()
  {
    return name;
  }
}

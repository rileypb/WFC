package brainfreeze.wfc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class TileMap<T, V extends Tile>
{
  private Set<T> coords;
  private Map<T, V> tiles;
  
  
  public TileMap(Set<T> coords)
  {
    this.coords = coords;
    this.tiles = new HashMap<>();
  }

  public Tile get(T coord) {
    return tiles.get(coord); 
  }

  public void set(T coord, V tile) {
    this.tiles.put(coord, tile);
  }

  public Map<T, V> asMap()
  {
    return tiles;
  }

  public int size()
  {
    return tiles.size();
  }
}

package brainfreeze.wfc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TileMapWithCandidates<T, V extends Tile> extends TileMap<T, V>
{
  private Multimap<T, V> candidates;
  private CandidateRules<T, V> rules;

  public TileMapWithCandidates(Set<T> coords, CandidateRules<T, V> rules)
  {
    super(coords);
    this.rules = rules;
    this.candidates = new Multimap<>();
    rules.setInitialCandidates(candidates, coords);
  }

  public void set(T coord, V tile) {
    super.set(coord, tile);
    rules.eliminate(candidates, coord, tile, this);
  }
  
  public List<T> findLowestEntropy() {
    int lowestEntropy = Integer.MAX_VALUE;
    List<T> cells = new ArrayList<>();
    for (T coord : candidates) {
      if (candidates.get(coord).size() < lowestEntropy && get(coord) == null && candidates.get(coord).size() > 0) {
        cells.clear();
        cells.add(coord);
        lowestEntropy = candidates.get(coord).size();
      } else if (candidates.get(coord).size() == lowestEntropy && get(coord) == null && candidates.get(coord).size() > 0) {
        cells.add(coord);
      }
    }
    return Collections.unmodifiableList(cells);
  }

  public int findHighestEntropy()
  {
    int highestEntropy = Integer.MIN_VALUE;
    for (T coord : candidates) {
      if (candidates.get(coord).size() > highestEntropy) {
        highestEntropy = candidates.get(coord).size();
      } 
    }
    
    return highestEntropy;
  }

  public List<V> getCandidates(T coord)
  {
    return new ArrayList<>(candidates.get(coord));
  }

  public int getEntropy(T coord)
  {
    return candidates.get(coord).size();  
  }

  public Map<T, V> asMap()
  {
    return super.asMap();
  }

  public int size()
  {
    return super.size();
  }
  
}

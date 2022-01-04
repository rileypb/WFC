package brainfreeze.wfc;

import java.util.Collection;

public interface CandidateRules<T, V extends Tile>
{

  void eliminate(Multimap<T, V> candidates, T coord, V tile, TileMap<T, V> map);

  void setInitialCandidates(Multimap<T, V> candidates, Collection<T> coords);

}

package brainfreeze.wfc;

import org.jgrapht.Graph;

public interface WFCHandler<T, V extends Tile, E extends Edge>
{
  void after(TileMapWithCandidates<T, V> map, Graph<T, E> graph, T coord, V candidate);

  void first(TileMapWithCandidates<T, V> map, Graph<T, E> graph);
}

package brainfreeze.wfc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import brainfreeze.wfc.pic.Swatch;

public class Multimap<T, U> implements Iterable<T>
{
  private Map<T, Set<U>> map;

  public Multimap()
  {
    this.map = new HashMap<>();
  }

  public void add(T key, U value)
  {
    if (!map.containsKey(key))
    {
      map.put(key, new HashSet<>());
    }
    map.get(key).add(value);
  }

  public Set<U> get(T key)
  {
    return this.map.get(key);
  }

  @Override
  public Iterator<T> iterator()
  {
    return map.keySet().iterator();
  }

  public Set<T> keySet()
  {
    return map.keySet();
  }

  public void clear(T key)
  {
    if (!map.containsKey(key))
    {
      map.put(key, new HashSet<>());
    }
    else
    {
      get(key).clear();
    }
  }

  public void retainAll(T key, Set<U> values)
  {
    map.get(key).retainAll(values);
  }
}

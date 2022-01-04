package brainfreeze.wfc;

public class Coord
{
  public final int i;
  public final int j;
  public final int k;

  public Coord(int i, int j, int k)
  {
    this.i = i;
    this.j = j;
    this.k = k;
  }

  @Override
  public int hashCode()
  {
    return Integer.hashCode(i) + 31 * (Integer.hashCode(j) + 31 * Integer.hashCode(k));
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == this)
    {
      return true;
    }
    if (obj instanceof Coord)
    {
      Coord c = (Coord) obj;
      if (i == c.i && j == c.j && k == c.k)
      {
        return true;
      }
    }

    return false;
  }

  @Override
  public String toString()
  {
    return String.format("(%d, %d, %d)", i, j, k);
  }
}

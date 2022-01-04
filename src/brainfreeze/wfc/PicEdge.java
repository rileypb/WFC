package brainfreeze.wfc;

public class PicEdge
{
  private float r;
  private float g;
  private float b;

  public PicEdge(float r, float g, float b)
  {
    super();
    this.r = r;
    this.g = g;
    this.b = b;
  }

  public float distance(PicEdge e)
  {
    return (float) Math.sqrt((r - e.r) * (r - e.r) + (g - e.g) * (g - e.g) + (b - e.b) * (b - e.b));
  }

}

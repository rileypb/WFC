package brainfreeze.wfc.pic;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;
import org.jgrapht.GraphType;

import brainfreeze.wfc.Coord;

public class PicGraph implements Graph<Coord, String>
{

  @Override
  public String addEdge(Coord arg0, Coord arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean addEdge(Coord arg0, Coord arg1, String arg2)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Coord addVertex()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean addVertex(Coord arg0)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean containsEdge(String arg0)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean containsEdge(Coord arg0, Coord arg1)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean containsVertex(Coord arg0)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int degreeOf(Coord arg0)
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Set<String> edgeSet()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<String> edgesOf(Coord arg0)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<String> getAllEdges(Coord arg0, Coord arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getEdge(Coord arg0, Coord arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdgeFactory<Coord, String> getEdgeFactory()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Coord getEdgeSource(String arg0)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Supplier<String> getEdgeSupplier()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Coord getEdgeTarget(String arg0)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public double getEdgeWeight(String arg0)
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public GraphType getType()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Supplier<Coord> getVertexSupplier()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int inDegreeOf(Coord arg0)
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Set<String> incomingEdgesOf(Coord arg0)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int outDegreeOf(Coord arg0)
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Set<String> outgoingEdgesOf(Coord arg0)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean removeAllEdges(Collection<? extends String> arg0)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Set<String> removeAllEdges(Coord arg0, Coord arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean removeAllVertices(Collection<? extends Coord> arg0)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean removeEdge(String arg0)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String removeEdge(Coord arg0, Coord arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean removeVertex(Coord arg0)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setEdgeWeight(String arg0, double arg1)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Set<Coord> vertexSet()
  {
    // TODO Auto-generated method stub
    return null;
  }

}

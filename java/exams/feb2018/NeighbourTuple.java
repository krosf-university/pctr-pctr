package pctr.exams.feb2018;

/**
 * NeighbourTuple
 */
public class NeighbourTuple {
  public int dragons, dragonsReproductors;
  public int rorcuals, rorcualsReproductors;

  public NeighbourTuple(int w, int x, int y, int z) {
    dragons = w;
    dragonsReproductors = x;
    rorcuals = y;
    rorcualsReproductors = z;
  }
}


enum Neighbour {
  N(-1, 0), E(1, 0), S(1, 0), W(-1, 0), NE(-1, 1), SE(1, 1), SW(1, -1), NW(-1, -1);
  public final int x;
  public final int y;

  Neighbour(int x, int y) {
    this.x = x;
    this.y = y;
  }

}

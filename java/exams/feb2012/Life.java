package pctr.exams.feb2012;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Life
 */
public class Life implements Runnable {

  private static Toroid t;
  private static int dimension;
  private static CyclicBarrier barrier;
  private int row;

  public Life(int row) {
    this.row = row;
  }

  public static void setProperties(int dimension, int alivecells) {
    t = new Toroid(dimension, alivecells);
    Life.dimension = dimension;
    barrier = new CyclicBarrier(dimension, () -> {
      Toroid.updateToroid();
      t.status();
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
  }

  @Override
  public void run() {
    for (int i = 0; i < dimension; ++i) {
      t.nextGen(this.row);
      try {
        barrier.await();
      } catch (InterruptedException | BrokenBarrierException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    int cores = 10;
    Life.setProperties(cores, 50);
    ExecutorService eService = Executors.newFixedThreadPool(cores);
    for (int task = 0; task < cores; ++task) {
      eService.execute(new Life(task));
    }
    eService.shutdown();
    eService.awaitTermination(1, TimeUnit.DAYS);
  }

}



class Toroid {
  private static int alives;
  private static Boolean[][] toroid, nextGen;

  private static enum Neighbours {
    N(-1, 0), E(1, 0), S(1, 0), W(-1, 0), NE(-1, 1), SE(1, 1), SW(1, -1), NW(-1, -1);
    public final int x;
    public final int y;

    Neighbours(int x, int y) {
      this.x = x;
      this.y = y;
    }

  }

  public Toroid(int dimension, int alives) {
    Random r = new Random();
    Toroid.alives = alives;
    toroid = new Boolean[dimension][dimension];
    for (Boolean[] row : toroid) {
      Arrays.fill(row, false);
    }
    for (int cont = 0, i, j; cont < alives;) {
      i = r.nextInt(dimension);
      j = r.nextInt(dimension);
      if (!toroid[i][j]) {
        toroid[i][j] = true;
        cont++;
      }
    }
    nextGen = toroid.clone();
  }

  private int countNeighbours(int x, int y) {
    int aliveNeighbours = 0;
    for (Neighbours n : Neighbours.values()) {
      if (toroid[Math.floorMod(x + n.x, toroid.length)][Math.floorMod(y + n.y, toroid.length)]) {
        ++aliveNeighbours;
      }
    }
    return aliveNeighbours;
  }

  public synchronized static void updateToroid() {
    toroid = nextGen.clone();
  }

  public synchronized void nextGen(int row) {
    for (int col = 0, alives; col < toroid.length; ++col) {
      alives = countNeighbours(row, col);
      if (toroid[row][col] && (alives > 3 || alives < 2)) {
        nextGen[row][col] = false;
        --Toroid.alives;
      } else if (!toroid[row][col] && alives == 3) {
        nextGen[row][col] = true;
        ++Toroid.alives;
      }
    }
  }

  public synchronized void status() {
    System.out.format(
        "Soy el hilo %s, mi prioridad es %d y el estatus actual es: %d células vivas\n",
        Thread.currentThread().getId(), Thread.currentThread().getPriority(), Toroid.alives);
  }
}

package pctr.exams.feb2019;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * WireWorld
 */
public class WireWorld implements Runnable {
  private static int generations, dimension, tasks;
  private static Circuit[][] current, next;
  private static CyclicBarrier cBarrier;
  private static Scanner scan = new Scanner(System.in);
  private static Menu menu =
      new Menu(new String[] {"0. Random", "1. Manual", "2. Salir"}, "Seleccione una opcion");
  private int begin, end;

  public WireWorld(int begin, int end) {
    this.begin = begin;
    this.end = end;
  }

  public static void setProperties(int[] props) {
    WireWorld.generations = props[0];
    WireWorld.dimension = props[1];
    WireWorld.tasks = props[2];
    current = new Circuit[dimension][dimension];
    next = current.clone();
    cBarrier = new CyclicBarrier(WireWorld.tasks, () -> {
      current = next.clone();
    });
  }

  public void nextGen(int x, int y) {
    switch (current[x][y]) {
      case ELECTRON_FRONT:
        next[x][y] = Circuit.ELECTRON_TAIL;
        break;
      case ELECTRON_TAIL:
        next[x][y] = Circuit.CONDUCTIVE;
        break;
      case CONDUCTIVE:
        int count = Circuit.ELECTRON_FRONT.count(current, x, y);
        if (count == 1 || count == 2) {
          next[x][y] = Circuit.ELECTRON_FRONT;
        }
        break;
      default:
        break;
    }
  }

  @Override
  public void run() {
    for (int gen = 0; gen < generations; ++gen) {
      for (int i = begin; i < end; ++i) {
        for (int j = 0; j < dimension; ++j) {
          nextGen(i, j);
        }
      }
      try {
        cBarrier.await();
      } catch (InterruptedException | BrokenBarrierException e) {
        e.printStackTrace();
      }
    }
  }

  private static void fillRandom() {
    current = new Circuit[dimension][dimension];
    Random r = new Random();
    for (int i = 0; i < dimension; ++i) {
      for (int j = 0; j < dimension; ++j) {
        current[i][j] = Circuit.valueOf(r.nextInt(4));
      }
    }
    next = current.clone();
  }

  private static void fillManual() {
    current = new Circuit[dimension][dimension];
    System.out.println("Ingrese los valores de la matriz.");
    for (int i = 0; i < dimension; ++i) {
      for (int j = 0; j < dimension; ++j) {
        System.out.printf("[%d][%d]: ", i, j);
        current[i][j] = Circuit.valueOf(scan.nextInt() % 4);
      }
    }
    next = current.clone();
  }

  private static void showGrid() {
    for (int i = 0; i < dimension; ++i) {
      for (int j = 0; j < dimension; ++j) {
        System.out.printf("%d ", next[i][j].value());
      }
      System.out.println();
    }
    System.out.println("\n\n");
  }

  private static void execute() {
    ThreadPoolExecutor pool = new ThreadPoolExecutor(WireWorld.tasks, WireWorld.tasks, 1,
        TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>());
    int chunkSize = (WireWorld.dimension + WireWorld.tasks - 1) / WireWorld.tasks;
    for (int task = 0, begin, end; task < WireWorld.tasks; ++task) {
      begin = task * chunkSize;
      end = Math.min(begin + chunkSize, WireWorld.dimension);
      pool.execute(new WireWorld(begin, end));
    }
    pool.shutdown();
    try {
      pool.awaitTermination(1, TimeUnit.DAYS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


  public static void main(String[] args) {
    menu.setCallbacks(new Menu.MenuCallback[] {() -> {
      setProperties(menu.readProperties());
      fillRandom();
      showGrid();
      execute();
      showGrid();
    }, () -> {
      setProperties(menu.readProperties());
      fillManual();
      showGrid();
      execute();
      showGrid();
    }});
    menu.loop();
  }
}


enum Circuit {
  EMPTY(0), ELECTRON_FRONT(1), ELECTRON_TAIL(2), CONDUCTIVE(3);
  private final int value;
  private static Map<Integer, Circuit> map = new HashMap<Integer, Circuit>();

  Circuit(int value) {
    this.value = value;
  }

  static {
    for (Circuit c : Circuit.values()) {
      map.put(c.value, c);
    }
  }

  public static Circuit valueOf(int c) {
    return map.get(c);
  }

  public int value() {
    return value;
  }

  public int count(Circuit[][] neighbourhood, int x, int y) {
    int count = 0;
    for (Neighbour neigh : Neighbour.values()) {
      if (neighbourhood[Math.floorMod(x + neigh.x, neighbourhood.length)][Math.floorMod(y + neigh.y,
          neighbourhood.length)].equals(this)) {
        ++count;
      }
    }
    return count;
  }
}


/**
 * Neighbour
 */
enum Neighbour {
  N(-1, 0), E(1, 0), S(1, 0), W(-1, 0), NE(-1, 1), SE(1, 1), SW(1, -1), NW(-1, -1);
  public final int x;
  public final int y;

  Neighbour(int x, int y) {
    this.x = x;
    this.y = y;
  }
}

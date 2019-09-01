package pctr;

/**
 * Hilo
 */
public class Hilo implements Runnable {
  public Hilo() {
  }

  @Override
  public void run() {
    /** Codigo que ejecuta el hilo */
  }

  public static void main(String[] args) {
    int cores = Runtime.getRuntime().availableProcessors();
    int tasks = 3333;
    int chunkSize = (tasks + cores - 1) / cores;
    for (int task = 0, begin, end; task < cores; ++task) {
      begin = task * chunkSize;
      end = Math.min(begin + chunkSize, tasks);
      System.out.format("%d, %d\n", begin, end);
    }
  }
}

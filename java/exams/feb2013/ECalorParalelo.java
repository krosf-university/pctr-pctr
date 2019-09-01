package pctr.exams.feb2013;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ECalorParalelo
 */
public class ECalorParalelo implements Runnable {
  private int inicio;
  private int fin;
  private static int c = 3;
  private static Integer[][] cuerpo = new Integer[1000][1000];

  public ECalorParalelo(int inicio, int fin) {
    this.inicio = inicio;
    this.fin = fin;
  }

  public static void init() {
    for (int i = 0; i < 1000; ++i) {
      for (int j = 0; j < 1000; ++j) {
        if (i == 0 || j == 0 || i == 999 || j == 999) {
          cuerpo[i][j] = 100;
        } else if (i > 489 && i < 511 && j > 489 && j < 511) {
          cuerpo[i][j] = 50;
        } else {
          cuerpo[i][j] = 0;
        }
      }
    }
  }

  public static Integer[][] matrix() {
    return cuerpo;
  }

  @Override
  public void run() {
    System.out.format("inicio: %d, fin: %d, %s\n", inicio, fin, Thread.currentThread().getName());
    Instant start = Instant.now();
    for (int i = inicio; i < fin; ++i) {
      for (int j = inicio; j < fin; ++j) {
        cuerpo[i][j] = cuerpo[i][j] + c * (cuerpo[i + 1][j] + cuerpo[i - 1][j] - 2 * cuerpo[i][j])
            + c * 2 * (cuerpo[i][j + 1] + cuerpo[i][j - 1] - 2 * cuerpo[i][j]);
      }
    }
    Instant end = Instant.now();
    Duration timeElapsed = Duration.between(start, end);
    System.out.println("Time taken: " + timeElapsed.toMillis() + " milliseconds");
  }

  public static void main(String[] args) throws InterruptedException {
    ECalorParalelo.init();
    int cores = Runtime.getRuntime().availableProcessors();
    ExecutorService exec = Executors.newFixedThreadPool(cores);
    int coef = (int) Math.ceil((1000 * 1.) / cores);
    int start = 0;
    int end = 0;
    boolean tasks = true;
    for (int i = 0; i < cores; ++i) {
      start = i * coef;
      end = (i + 1) * coef;
      if (tasks) {
        tasks = (end < 1000);
        end = (tasks) ? end : 1000;
      }
      exec.submit(new ECalorParalelo(start, end));
    }
    exec.shutdown();
    for (Integer[] row : ECalorParalelo.matrix())
      System.out.println(Arrays.deepToString(row));
  }
}

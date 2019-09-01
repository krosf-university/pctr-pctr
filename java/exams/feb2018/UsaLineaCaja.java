package pctr.exams.feb2018;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * UsaLineaCaja
 */
public class UsaLineaCaja {

  public static void main(String[] args) throws InterruptedException {
    int cores = 100;
    ExecutorService eService = Executors.newFixedThreadPool(100);
    for (int task = 0; task < cores; ++task) {
      System.out.printf("Creando la tarea %d\n", task);
      eService.execute(new Cliente(task));
    }
    eService.shutdown();
    eService.awaitTermination(1, TimeUnit.DAYS);
  }
}

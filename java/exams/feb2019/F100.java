package pctr.exams.feb2019;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * F100
 */
public class F100 {

  public static void main(String[] args) {
    int stations = 16;
    ExecutorService eService = Executors.newFixedThreadPool(stations);
    CIWS weapones = new CIWS(4);
    for (int station = 0; station < stations; ++station) {
      eService.execute(new Station(weapones, station));
    }
    eService.shutdown();
    try {
      eService.awaitTermination(1, TimeUnit.HOURS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

package pctr.exams.tests;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Drakkar
 */
public class Drakkar implements Runnable {

  private static Marmita m = new Marmita();
  private int type;

  public Drakkar(int type) {
    this.type = type;
  }

  @Override
  public void run() {
    while (true) {
      if (type == 0) {
        m.cocinar();
      } else {
        m.comer();
      }
    }
  }

  public static void main(String[] args) {
    int cores = Runtime.getRuntime().availableProcessors();
    ExecutorService eService = Executors.newFixedThreadPool(cores);
    for (int i = 0; i < cores; i++) {
      eService.submit(new Drakkar(i));
    }
    eService.shutdown();
    try {
      eService.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}


class Marmita {
  private static int quantity = 0;

  public synchronized void cocinar() {
    quantity += 10;
    System.out.println(quantity);
    notifyAll();
    try {
      wait();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public synchronized void comer() {
    try {
      if (quantity > 0) {
        System.out.println(quantity);
        quantity -= 1;
      } else {
        notify();
        wait();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

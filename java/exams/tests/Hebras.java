package pctr.exams.tests;

/**
 * Hebras
 */
public class Hebras extends Thread {
  private Referencia r = new Referencia();

  public Hebras() {
  }



  public static void main(String[] args) {
    Thread[] hilos = new Thread[1000];
    for (int i = 0; i < 1000; i++) {
      hilos[i] = new Hebras();
      hilos[i].start();
    }
    for (int i = 0; i < 1000; i++) {
      try {
        hilos[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println(new Referencia().ver());
  }



  @Override
  public void run() {
    synchronized (this) {
      r.inc();
    }
  }
}


class Referencia {
  private static int n = -1;

  public void inc() {
    n++;
  }

  public int ver() {
    return n;
  }
}

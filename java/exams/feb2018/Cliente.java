package pctr.exams.feb2018;

import java.util.Random;

/**
 * Cliente
 */
public class Cliente implements Runnable {
  private int id;
  private static LineaCajas l = new LineaCajas();

  public Cliente(int id) {
    this.id = id;
  }

  @Override
  public void run() {
    try {
      l.irACaja(id);
      int t = new Random().nextInt(5000);
      System.out.println(Thread.currentThread().getName() + " en caja por " + t + "ms");
      Thread.sleep(t);
      l.irseDeCaja(id);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}

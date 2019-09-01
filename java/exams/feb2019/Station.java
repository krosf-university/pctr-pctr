package pctr.exams.feb2019;

import java.util.Random;

/**
 * Stations
 */
public class Station implements Runnable {
  private CIWS weapones;
  private int id;

  public Station(CIWS weapones, int id) {
    this.weapones = weapones;
    this.id = id;
  }

  @Override
  public void run() {
    int sleepTime = new Random().nextInt(5000) + 2000;
    try {
      weapones.acquireWeapon();
      Thread.sleep(sleepTime);
      System.out.format("station %d shot weapon and take %dms\n", id, sleepTime);
      weapones.releaseWeapon();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

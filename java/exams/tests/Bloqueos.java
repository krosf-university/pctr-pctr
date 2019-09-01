package pctr.exams.tests;

import java.util.concurrent.Semaphore;

/**
 * Bloqueos
 */
public class Bloqueos extends Thread {
  private static Semaphore s = new Semaphore(1);
  private static int n = 3;
  private int type;

  public Bloqueos(int type) {
    this.type = type;
  }

  @Override
  public void run() {
    switch (type) {
      case 0:
        m1();
        break;
      case 1:
        m2();
        break;
      case 2:
        m3();
        break;
    }
  }

  public void m1() {
    System.out.println("fuera m1");
    synchronized (this) {
      System.out.println("dentro m1");
      n++;
    }
  }

  public void m2() {
    try {
      System.out.println("fuera m2");
      s.acquire();
      System.out.println("dentro m2");
      n++;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    s.release();
  }

  public void m3() {
    System.out.println("fuera m3");
    synchronized (this) {
      System.out.println("dentro m3");
      n++;
    }
  }

  public static void main(String[] args) {
    Bloqueos A = new Bloqueos(0);
    Bloqueos B = new Bloqueos(2);
    Bloqueos C = new Bloqueos(1);

    A.start();
    C.start();
    B.start();

    try {
      A.join();
      C.join();
      B.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println(n);
  }
}

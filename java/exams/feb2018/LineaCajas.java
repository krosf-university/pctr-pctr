package pctr.exams.feb2018;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LineaCajas
 */
public class LineaCajas {
  private final ReentrantLock lock = new ReentrantLock();
  private final int cajas = 10;
  private final Queue<Integer> queue = new LinkedList<Integer>();
  private final Condition CajasLlenas = lock.newCondition();

  public LineaCajas() {
  }

  public void irACaja(int id) throws InterruptedException {
    try {
      lock.lock();
      while (queue.size() == cajas) {
        // System.out.println(Thread.currentThread().getName() + " esperando en la cola");
        CajasLlenas.await();
      }
      if (queue.size() < cajas) {
        queue.offer(id);
      }
    } finally {
      lock.unlock();
    }
  }

  public void irseDeCaja(int id) {
    try {
      lock.lock();
      if (queue.poll() != null) {
        System.out.println(Thread.currentThread().getName() + " deja una caja vacia");
        CajasLlenas.signalAll();
      }
    } finally {
      lock.unlock();
    }
  }
}

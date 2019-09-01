package pctr.exams.feb2019;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * CIWS
 */
public class CIWS {
  final ReentrantLock lock = new ReentrantLock();
  final Condition notFull = lock.newCondition();
  int n, count;

  public CIWS(int n) {
    this.n = n;
  }

  public void acquireWeapon() throws InterruptedException {
    lock.lock();
    try {
      while (count == n) {
        System.out.println("Waiting");
        notFull.await();
      }
      count++;
    } finally {
      lock.unlock();
    }
  }

  public void releaseWeapon() {
    lock.lock();
    try {
      count--;
      notFull.signal();
    } finally {
      lock.unlock();
    }
  }
}

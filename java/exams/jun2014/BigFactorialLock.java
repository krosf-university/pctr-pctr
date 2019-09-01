package pctr.exams.jun2014;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * BigFactorialLock
 */
public class BigFactorialLock implements Runnable {
  private BigInteger begin, end;
  private static ReentrantLock lock = new ReentrantLock();
  private static BigInteger result = BigInteger.ONE;


  public BigFactorialLock(BigInteger begin, BigInteger end) {
    this.begin = begin;
    this.end = end;
  }

  @Override
  public void run() {
    BigInteger partialFactorial = factorial();
    lock.lock();
    result = result.multiply(partialFactorial);
    lock.unlock();
  }

  public static BigInteger Factorial() {
    return result;
  }

  public static void main(String[] args) throws InterruptedException {
    BigInteger cores = new BigInteger(String.valueOf(Runtime.getRuntime().availableProcessors()));
    BigInteger number = new BigInteger(args[0]);
    BigInteger quot = number.divide(cores);
    BigInteger remain = number.remainder(cores);
    BigInteger begin, end;
    ExecutorService eService = Executors.newFixedThreadPool(cores.intValue());
    for (BigInteger i = BigInteger.ONE; i.compareTo(cores) != 1; i = i.add(BigInteger.ONE)) {
      begin = BigInteger.ONE.add(quot.multiply(i.subtract(BigInteger.ONE)));
      end = (i.compareTo(cores) == 0 ? i.multiply(quot).add(remain) : i.multiply(quot));
      eService.submit(new BigFactorialLock(begin, end));
    }
    eService.shutdown();
    eService.awaitTermination(1, TimeUnit.DAYS);
    System.out.println(BigFactorialLock.Factorial());
  }


  private BigInteger factorial() {
    BigInteger partial = BigInteger.ONE;
    for (BigInteger i = begin; i.compareTo(end) != 1; i = i.add(BigInteger.ONE)) {
      partial = partial.multiply(i);
    }
    return partial;
  }
}

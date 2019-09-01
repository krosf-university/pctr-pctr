package pctr.exams.jun2014;

import java.util.List;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * BigInteger
 */
public class BigFactorial implements Callable<BigInteger> {
  private BigInteger inicio;
  private BigInteger fin;

  public BigFactorial(BigInteger inicio, BigInteger fin) {
    this.inicio = inicio;
    this.fin = fin;
  }



  public static void main(String[] args) throws InterruptedException, ExecutionException {
    BigInteger cores = new BigInteger(String.valueOf(Runtime.getRuntime().availableProcessors()));
    BigInteger number = new BigInteger(args[0]);
    BigInteger coef = number.divide(cores);
    BigInteger rest = number.remainder(cores);
    ExecutorService callables = Executors.newFixedThreadPool(cores.intValue());
    List<Callable<BigInteger>> tasks = new ArrayList<Callable<BigInteger>>(cores.intValue());
    BigInteger inicio, fin;
    for (BigInteger i = BigInteger.ONE; i.compareTo(cores) != 1; i = i.add(BigInteger.ONE)) {
      inicio = BigInteger.ONE.add(coef.multiply(i.subtract(BigInteger.ONE)));
      fin = ((i.compareTo(cores) == 0) ? coef.multiply(i).add(rest) : coef.multiply(i));
      tasks.add(new BigFactorial(inicio, fin));
    }
    BigInteger fact = BigInteger.ONE;
    List<Future<BigInteger>> response = callables.invokeAll(tasks);
    callables.shutdown();
    callables.awaitTermination(1, TimeUnit.DAYS);
    for (Future<BigInteger> res : response) {
      if (res.isDone()) {
        fact = fact.multiply(res.get());
      }
    }
    System.out.println(fact);
  }



  @Override
  public BigInteger call() throws Exception {
    BigInteger partialProduct = BigInteger.ONE;
    for (BigInteger i = inicio; i.compareTo(fin) != 1; i = i.add(BigInteger.ONE)) {
      partialProduct = partialProduct.multiply(i);
    }
    return partialProduct;
  }
}

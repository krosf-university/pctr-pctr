package pctr.exams.feb2018;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ProductoEscalar
 */
public class ProductoEscalar implements Callable<Float> {
  private static Float[] A;
  private static Float[] B;
  private int begin, end;

  public ProductoEscalar(int begin, int end) {
    this.begin = begin;
    this.end = end;
  }

  public static void setProperties(int size) {
    Random r = new Random();
    A = new Float[size];
    B = new Float[size];
    for (int i = 0; i < size; ++i) {
      A[i] = r.nextFloat() + r.nextInt(100);
      B[i] = r.nextFloat() + r.nextInt(100);
    }
  }

  @Override
  public Float call() {
    Float partialResult = 0.0f;
    for (int i = begin; i < end; ++i) {
      partialResult += A[i] * B[i];
    }
    return partialResult;
  }

  public static void main(String[] args) {
    ExecutorService eService = Executors.newFixedThreadPool(4);
    setProperties(10000);
    List<Callable<Float>> tasks = new ArrayList<Callable<Float>>(4);
    int taskSize = 10000 / 4;
    for (int i = 0; i < 4; i++) {
      tasks.add(new ProductoEscalar(i * taskSize, taskSize * (i + 1)));
    }
    Float escalar = 0.0f;
    try {
      List<Future<Float>> results = eService.invokeAll(tasks);
      for (Future<Float> result : results) {
        escalar += result.get();
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    eService.shutdown();
    System.out.println(escalar);
  }


}

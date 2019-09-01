package pctr.exams.feb2013;

import java.util.Arrays;
import java.util.Random;

/**
 * ProductoEscalar
 */
public class ProductoEscalar extends Thread {
  private static Integer[][] matrix;
  private static Integer[] vector;
  private static Integer[] result;
  private int begin, end;

  public static void printResult() {
    System.out.println(Arrays.toString(result));
  }

  public ProductoEscalar(int begin, int end) {
    this.begin = begin;
    this.end = end;
  }

  public static void setProperties(int row, int col) {
    Random r = new Random();
    matrix = new Integer[row][0];
    for (int i = 0; i < row; ++i) {
      matrix[i] = r.ints(col, 0, 100).boxed().toArray(Integer[]::new);
    }
    vector = r.ints(col, 0, 100).boxed().toArray(Integer[]::new);
    result = new Integer[col];
    Arrays.fill(result, 0);
  }

  public static void setProperties(Integer[][] matrix, Integer[] vector) {
    ProductoEscalar.matrix = matrix;
    ProductoEscalar.vector = vector;
    result = new Integer[vector.length];
    Arrays.fill(result, 0);
  }

  @Override
  public void run() {
    for (int i = begin; i < end; ++i) {
      for (int j = 0; j < matrix[i].length; ++j) {
        result[i] += matrix[i][j] * vector[j];
      }
    }
  }

  public static void main(String[] args) {
    int cores = 1;
    Thread[] hilos = new Thread[cores];
    int tasks = 3;
    ProductoEscalar.setProperties(tasks, tasks);
    int chunkSize = (tasks + cores - 1) / cores;
    for (int h = 0, begin, end; h < cores; ++h) {
      begin = h * chunkSize;
      end = Math.min(begin + chunkSize, tasks);
      hilos[h] = new ProductoEscalar(begin, end);
      hilos[h].setPriority(6);
      hilos[h].start();
    }
    for (Thread t : hilos) {
      try {
        t.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    for (Integer[] row : matrix) {
      System.out.println(Arrays.toString(row));
    }
    System.out.println(Arrays.toString(vector));
    System.out.println(Arrays.toString(result));
  }
}

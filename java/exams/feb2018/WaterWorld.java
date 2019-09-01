package pctr.exams.feb2018;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



/**
 * WaterWorld
 */
public class WaterWorld implements Runnable {
  private static Specie[][] surface;
  private static Specie[][] nextSurface;
  private static CyclicBarrier cBarrier;
  private static int generations;
  private int begin, end;

  /**
   *
   * @param begin
   * @param end
   */
  public WaterWorld(int begin, int end) {
    this.begin = begin;
    this.end = end;
  }

  /**
   *
   * @param initial
   * @param threads
   * @param generations
   */
  public static void setProperties(Specie[][] initial, int threads, int generations) {
    surface = initial.clone();
    nextSurface = initial.clone();
    WaterWorld.generations = generations;
    cBarrier = new CyclicBarrier(threads, () -> {
      // System.out.println("Generations " + Thread.currentThread().getName());
      surface = nextSurface.clone();
    });
  }

  private NeighbourTuple countNeighbours(int x, int y) {
    NeighbourTuple neighbourTuple = new NeighbourTuple(0, 0, 0, 0);
    for (Neighbour neighbour : Neighbour.values()) {
      Specie s = surface[Math.floorMod(x + neighbour.x, surface.length)][Math
          .floorMod(y + neighbour.y, surface[0].length)];
      if (Dragon.class.isInstance(s)) {
        ++neighbourTuple.dragons;
        if (s.hasReproductionAge()) {
          ++neighbourTuple.dragonsReproductors;
        }
      } else if (Rorcual.class.isInstance(s)) {
        ++neighbourTuple.rorcuals;
        if (s.hasReproductionAge()) {
          ++neighbourTuple.rorcualsReproductors;
        }
      }
    }
    return neighbourTuple;
  }

  private static void printMenu() {
    System.out.println(" * MENU *");
    System.out.println(" 1- Tamaño 1000x2000");
    System.out.println(" 2- Elegir tamaño superficie");
    System.out.println(" 3- Salir");
  }

  private static void printPlanet() {
    for (int i = 0; i < nextSurface.length; ++i) {
      for (int j = 0; j < nextSurface[0].length; ++j) {
        if (Dragon.class.isInstance(nextSurface[i][j])) {
          System.out.print("D ");
        } else if (Rorcual.class.isInstance(nextSurface[i][j])) {
          System.out.print("R ");
        } else {
          System.out.print("- ");
        }
      }
      System.out.println();
    }
  }

  private static void execute(int tasks, int cores, boolean fill) throws InterruptedException {
    ExecutorService eService = Executors.newFixedThreadPool(cores);
    int chunkSize = (tasks + cores - 1) / cores;
    for (int task = 0, begin, end; task < cores; ++task) {
      begin = task * chunkSize;
      end = Math.min(begin + chunkSize, tasks);
      eService
          .execute((fill) ? new SpeciesRandomGenerator(begin, end) : new WaterWorld(begin, end));
    }
    eService.shutdown();
    eService.awaitTermination(1, TimeUnit.DAYS);
  }

  public static Specie[][] SpeciesManualGenerator(Scanner scanner, int rows, int cols) {
    Specie[][] species = new Specie[rows][cols];
    System.out.println("Acontinuacion inserte los elementos 1: Rorcual 2: Dragon");
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        System.out.format("Inserte el elemento [%d][%d]: ", i, j);
        switch (scanner.nextInt()) {
          case 1:
            species[i][j] = new Rorcual();
            break;
          case 2:
            species[i][j] = new Dragon();
            break;
          default:
            break;
        }
      }
    }
    return species;
  }

  public Specie lotkaVolterra(int i, int j) {
    Specie actual = surface[i][j];
    Specie nSpecies = null;
    NeighbourTuple neigh = countNeighbours(i, j);
    Random r = new Random(System.currentTimeMillis());
    if (actual == null) {
      if (neigh.dragons >= 4 && neigh.dragonsReproductors >= 3 && neigh.rorcuals < 4) {
        nSpecies = new Dragon();
      } else if (neigh.rorcuals >= 4 && neigh.rorcualsReproductors >= 3 && neigh.dragons < 4) {
        nSpecies = new Rorcual();
      }
    } else if (Rorcual.class.isInstance(actual)) {
      if (!(actual.getAge() == 10 || neigh.dragons > 4 || neigh.rorcuals == 8)) {
        nSpecies = actual;
        nSpecies.addAge();
      }
    } else {
      if (!(actual.getAge() == 20 || (neigh.dragons >= 6 && neigh.rorcuals == 0))
          && r.nextDouble() <= 0.7) {
        nSpecies = actual;
        nSpecies.addAge();
      }
    }
    return nSpecies;
  }

  @Override
  public void run() {
    for (int gen = 0; gen < generations; ++gen) {
      for (int i = begin; i < end; ++i) {
        for (int j = 0; j < surface[0].length; ++j) {
          nextSurface[i][j] = lotkaVolterra(i, j);
        }
      }
      try {
        cBarrier.await();
      } catch (InterruptedException | BrokenBarrierException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    int cores = Runtime.getRuntime().availableProcessors();
    Scanner scanner = new Scanner(System.in);
    String op = "";
    while (!op.equals("3")) {
      printMenu();
      op = scanner.nextLine();
      switch (op) {
        case "1":
          try {
            WaterWorld.execute(1000, cores, true);
            setProperties(SpeciesRandomGenerator.getSpecies(), cores, 1000);
            printPlanet();
            WaterWorld.execute(1000, cores, false);
            printPlanet();
            System.out.println("Sale ejecucion");
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          break;

        case "2":
          try {
            System.out.print("Introduzca el numero de filas: ");
            int rows = scanner.nextInt();
            System.out.print("Introduzca el numero de columnas: ");
            int cols = scanner.nextInt();
            System.out.print("Introduzca el numero de generaciones: ");
            int gens = scanner.nextInt();
            setProperties(WaterWorld.SpeciesManualGenerator(scanner, rows, cols), cores, gens);
            printPlanet();
            WaterWorld.execute(rows, cores, false);
            printPlanet();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          break;
      }
      System.out.println("Sale switch");
    }
    scanner.close();
  }
}

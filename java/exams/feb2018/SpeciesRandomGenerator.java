package pctr.exams.feb2018;

import java.util.Random;

/**
 * SpeciesRandomGenerator
 */
public class SpeciesRandomGenerator implements Runnable {
  private static Specie[][] species = new Specie[1000][2000];
  private static int cols = 2000;
  private int begin, end;

  public SpeciesRandomGenerator(int begin, int end) {
    this.begin = begin;
    this.end = end;
  }

  @Override
  public void run() {
    Random r = new Random(System.currentTimeMillis());
    for (int i = begin; i < end; ++i) {
      for (int j = 0, p05 = 0, p025 = 0, y = r.nextInt(cols); j < cols; ++j) {
        if (p05 < 0.5 * cols) {
          ++p05;
          while (species[i][y] != null) {
            y = r.nextInt(cols);
          }
          species[i][y] = new Rorcual();
        } else if (p025 < 0.25 * cols) {
          ++p025;
          while (species[i][y] != null) {
            y = r.nextInt(cols);
          }
          species[i][y] = new Dragon();
        }
      }
    }
  }

  /**
   * @return the species
   */
  public static Specie[][] getSpecies() {
    return species;
  }

}

package pctr.exams.feb2018;

/**
 * Rorcual
 */
public class Rorcual extends Specie {

  private static int ageOfReporduction = 2;

  public Rorcual() {
    super();
  }

  public boolean hasReproductionAge() {
    return this.age >= ageOfReporduction;
  }
}

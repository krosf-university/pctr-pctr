package pctr.exams.feb2018;

/**
 * Dragon
 */
public class Dragon extends Specie {

  private static int ageOfReporduction = 3;

  public Dragon() {
    super();
  }

  public boolean hasReproductionAge() {
    return this.age >= ageOfReporduction;
  }
}

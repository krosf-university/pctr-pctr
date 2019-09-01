package pctr.exams.feb2018;

/**
 * Specie
 */
public abstract class Specie {
  protected int age;

  public Specie() {
    this.age = 1;
  }

  public void addAge() {
    ++this.age;
  }

  public int getAge() {
    return this.age;
  }

  abstract boolean hasReproductionAge();

}

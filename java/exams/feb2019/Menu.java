package pctr.exams.feb2019;

import java.util.Scanner;

/**
 * Menu
 */
public class Menu {
  private static final Scanner scan = new Scanner(System.in);
  private String scanMessage;
  private String[] options;
  private MenuCallback[] callbacks;

  public interface MenuCallback {
    public void call();
  };

  public Menu(String[] options, String message) {
    this.options = options;
    this.scanMessage = message;
  }

  /**
   * @param callbacks the callbacks to set
   */
  public void setCallbacks(MenuCallback[] callbacks) {
    this.callbacks = callbacks;
  }

  public String build() {
    String menu = "*** *** MENU *** ***\n";
    for (String option : options) {
      menu += String.format("%s\n", option);
    }
    menu += "*** *** *** *** ***";
    return menu;
  }

  public Integer scanOption() {
    System.out.printf("%s: ", scanMessage);
    return scan.nextInt();
  }

  public int[] readProperties() {
    int[] props = new int[3];
    System.out.print("Ingrese el numero de generacions: ");
    props[0] = scan.nextInt();
    System.out.print("Ingrese la dimension: ");
    props[1] = scan.nextInt();
    System.out.print("Ingrese el numero de tareas: ");
    props[2] = scan.nextInt();
    return props;
  }

  public void loop() {
    Integer op = -1;
    Integer nOptions = options.length - 1;
    String menu = build();
    while (op != nOptions) {
      System.out.println(menu);
      op = scanOption();
      if (op != nOptions) {
        try {
          callbacks[op].call();
        } catch (ArrayIndexOutOfBoundsException e) {
          System.out.println("Opcion invalida!!");
        }
      }
    }
  }
}

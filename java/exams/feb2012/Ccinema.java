package pctr.exams.feb2012;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Ccinema.
 */
public class Ccinema {
  private static Icinema look_up;

  /**
   * Start Client cinema.
   *
   * @param args args[0] = movie, args[1] = seat
   * @throws MalformedURLException if url of server is wrong.
   * @throws RemoteException       if server not responses.
   * @throws NotBoundException     if server if not biding.
   */
  public static void main(String[] args)
      throws MalformedURLException, RemoteException, NotBoundException {
    Scanner sc = new Scanner(System.in);
    look_up = (Icinema) Naming.lookup("//localhost/cinema");
    String movie = args[0];
    String seat = args[1];
    String response = "412";
    while (!response.equals("200")) {
      System.out.println("in");
      response = look_up.buyTicket(movie, Integer.parseInt(seat));
      switch (response) {
        case "200":
          System.out.println("Your buy was succesful");
          break;
        case "404":
          System.out.println("The movie was not found");
          System.out.println("Choose another");
          movie = sc.nextLine();
          break;
        case "412":
          System.out.println("The seat you choose is no available choose another");
          seat = sc.nextLine();
          break;
      }
    }
    sc.close();
  }

}

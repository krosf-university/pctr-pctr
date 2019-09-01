package pctr.exams.feb2012;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Scinema.
 */
public class Scinema extends UnicastRemoteObject implements Icinema {
  private static final long serialVersionUID = 1L;

  private static Map<String, boolean[]> movies = new HashMap<String, boolean[]>() {
    private static final long serialVersionUID = 1L;
    {
      put("Yesterday", new boolean[100]);
      put("Toy Story 4", new boolean[100]);
      put("The Lion King", new boolean[100]);
      put("The Secret Life of Pets 2", new boolean[200]);
    }
  };

  protected Scinema() throws RemoteException {
    super();
  }

  @Override
  public synchronized String buyTicket(String movie, int seat) throws RemoteException {
    System.out.format("%s %d\n", movie, seat);
    String response = "404";
    if (movies.containsKey(movie)) {
      response = "412";
      if (!movies.get(movie)[seat]) {
        movies.get(movie)[seat] = true;
        response = "200";
      }
    }
    return response;
  }

  /**
   * Start Sever for cinema.
   *
   * @param args not require
   */
  public static void main(String[] args) {
    try {
      Naming.rebind("//localhost/cinema", new Scinema());
      System.out.println("Server Ready");
    } catch (Exception e) {
      System.err.println("Server exception: " + e.toString());
      e.printStackTrace();
    }
  }
}

package pctr.exams.jun2014;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * CIBEX35
 */
public class CIBEX35 {

  private static IIBEX35 bolsa;

  public static void main(String[] args) {
    try {
      bolsa = (IIBEX35) Naming.lookup("ibex");
      switch (args[0]) {
        case "all":
          System.out.println(Arrays.toString((bolsa.contizaciones())));
          break;
        default:
          System.out.println(bolsa.cotizacion(args[0]));
          break;
      }
    } catch (MalformedURLException | RemoteException | NotBoundException e) {
      e.printStackTrace();
    }
  }


}

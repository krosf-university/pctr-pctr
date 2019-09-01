package pctr.exams.jun2014;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * SIBEX35
 */
public class SIBEX35 extends UnicastRemoteObject implements IIBEX35 {

  private static final long serialVersionUID = 1L;
  private static Map<String, Double> valores = new HashMap<String, Double>();
  private static Random seed = new Random();

  protected SIBEX35() throws RemoteException {
    valores.put("TRI", seed.nextDouble());
    valores.put("CHN", seed.nextDouble());
    valores.put("BMM", seed.nextDouble());
    valores.put("CRR", seed.nextDouble());
    valores.put("TDC", seed.nextDouble());
  }


  @Override
  public Double cotizacion(String valor) throws RemoteException {
    return valores.get(valor);
  }

  @Override
  public Double[] contizaciones() throws RemoteException {
    return valores.values().toArray(new Double[valores.size()]);
  }

  public static void main(String[] args) {
    Runnable runnable = () -> {
      while (true) {
        try {
          Thread.sleep(100);
          synchronized (valores) {
            valores.forEach((k, v) -> valores.put(k, seed.nextDouble()));
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    Thread t = new Thread(runnable);
    try {
      Naming.rebind("ibex", new SIBEX35());
    } catch (RemoteException | MalformedURLException e) {
      e.printStackTrace();
    }
    t.start();
  }
}

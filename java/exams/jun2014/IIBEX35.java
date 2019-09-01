package pctr.exams.jun2014;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * IIBEX35
 */
public interface IIBEX35 extends Remote {
  public Double cotizacion(String valor) throws RemoteException;

  public Double[] contizaciones() throws RemoteException;
}

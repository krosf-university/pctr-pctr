package pctr.exams.feb2012;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Icinema.
 */
public interface Icinema extends Remote {
  public String buyTicket(String movie, int seat) throws RemoteException;
}

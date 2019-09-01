package pctr.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Ichat
 */
public interface IChatClient extends Remote {

  public void retrieveMessage(String message) throws RemoteException;
}

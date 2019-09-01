package pctr.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * IChatServer
 */
public interface IChatServer extends Remote {
  void registerChatClient(IChatClient client) throws RemoteException;

  void broadcastMessage(String message) throws RemoteException;
}

package pctr.chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * ChatServer
 */
public class ChatServer extends UnicastRemoteObject implements IChatServer {

  private ArrayList<IChatClient> clients;

  protected ChatServer() throws RemoteException {
    clients = new ArrayList<IChatClient>();
  }

  private static final long serialVersionUID = 2222L;

  @Override
  public synchronized void registerChatClient(IChatClient client) throws RemoteException {
    clients.add(client);
  }

  @Override
  public synchronized void broadcastMessage(String message) throws RemoteException {
    for (IChatClient client : clients) {
      client.retrieveMessage(message);
    }
  }

  public static void main(String[] args) {
    try {
      Naming.rebind("chatServer", new ChatServer());
    } catch (RemoteException | MalformedURLException e) {
      e.printStackTrace();
    }
  }
}

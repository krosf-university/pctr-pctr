package pctr.chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * ChatClient
 */
public class ChatClient extends UnicastRemoteObject implements IChatClient, Runnable {

  private static final long serialVersionUID = 1L;
  private IChatServer server;
  private String name;
  private Scanner scanner = new Scanner(System.in);

  protected ChatClient(String name, IChatServer server) throws RemoteException {
    this.name = name;
    this.server = server;
    server.registerChatClient(this);
  }

  @Override
  public void retrieveMessage(String message) throws RemoteException {
    System.out.println(message);
  }

  @Override
  public void run() {
    String message;
    while (true) {
      message = scanner.nextLine();
      try {
        server.broadcastMessage(name + ": " + message);
      } catch (RemoteException e) {
        System.out.println("Error: Sending Message");
      }
    }
  }

  public static void main(String[] args) {
    String url = "rmi://localhost/chatServer";
    try {
      IChatServer server = (IChatServer) Naming.lookup(url);
      new Thread(new ChatClient(args[0], server)).start();
    } catch (MalformedURLException | RemoteException | NotBoundException e) {
      e.printStackTrace();
    }
  }

}

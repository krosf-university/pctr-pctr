package pctr.exams.feb2013;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Irenal
 */
public interface IRenal extends Remote {
  public float aclarCreat(Datos t) throws RemoteException;

  public float creatininaMedia() throws RemoteException;

  public float indMasaCorporal(float peso, float altura) throws RemoteException;

}

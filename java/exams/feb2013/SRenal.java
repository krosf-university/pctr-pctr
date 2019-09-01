package pctr.exams.feb2013;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 * SRenal
 */
public class SRenal extends UnicastRemoteObject implements IRenal {

  private static final long serialVersionUID = 1L;
  private static Vector<Float> mean = new Vector<Float>();

  protected SRenal() throws RemoteException {
  }

  @Override
  public synchronized float aclarCreat(Datos datos) throws RemoteException {
    float result = ((140 - datos.getEdad()) * datos.getPeso()) / 72 * datos.getCreatinina();
    result = (datos.isMujer() ? ((float) (result * 0.85)) : result);
    mean.addElement(result);
    return result;
  }

  @Override
  public synchronized float creatininaMedia() throws RemoteException {
    return (float) mean.stream().mapToDouble(e -> e).average().getAsDouble();
  }

  @Override
  public float indMasaCorporal(float peso, float altura) throws RemoteException {
    return peso / (altura * altura);
  }


}

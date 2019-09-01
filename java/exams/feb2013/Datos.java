package pctr.exams.feb2013;

import java.io.Serializable;

/**
 * Datos
 */
public class Datos implements Serializable {
  private static final long serialVersionUID = 1L;
  private float creatinina;
  private float edad;
  private float peso;
  private float altura;
  private boolean mujer;

  public Datos(float creatinina, float edad, float peso, float altura, boolean mujer) {
    this.creatinina = creatinina;
    this.edad = edad;
    this.peso = peso;
    this.altura = altura;
    this.mujer = mujer;
  }

  public float getCreatinina() {
    return creatinina;
  }

  public float getEdad() {
    return edad;
  }

  public float getPeso() {
    return peso;
  }

  public float getAltura() {
    return altura;
  }

  public boolean isMujer() {
    return mujer;
  }

}

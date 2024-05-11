package Util;

import Controller.ControleAlgoritmoConsenso;
import Model.Inimigo;

public class AlvoCompartilhado {
  private static AlvoCompartilhado instancia = new AlvoCompartilhado();
  private static Inimigo alvoAtual;
  private ControleAlgoritmoConsenso controleAlgoritmoConsenso;

  private AlvoCompartilhado() {
  }

  public static AlvoCompartilhado getInstance() {
    return instancia;
  }

  public synchronized static Inimigo getAlvoAtual() {
    return alvoAtual;
  }

  public synchronized static void setAlvoAtual(Inimigo alvo) {
    AlvoCompartilhado.alvoAtual = alvo;
    System.out.println("ID INIMIGO = " + alvoAtual.getId());
    //controleAlgoritmoConsenso.rotacionarParaAtacar(alvoAtual.getId());
  }

  public void setControleAlgoritmoConsenso(ControleAlgoritmoConsenso controleAlgoritmoConsenso) {
    this.controleAlgoritmoConsenso = controleAlgoritmoConsenso;
  }
}

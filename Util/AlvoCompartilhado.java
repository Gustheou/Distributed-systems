package Util;

import Controller.ControleAlgoritmoConsenso;
import Model.Inimigo;

public class AlvoCompartilhado {
  private static AlvoCompartilhado instancia = new AlvoCompartilhado();
  private Inimigo alvoAtual;
  private ControleAlgoritmoConsenso controleAlgoritmoConsenso;

  private AlvoCompartilhado() {
    // Construtor privado para garantir que apenas uma instância seja criada
  }

  public static AlvoCompartilhado getInstance() {
    return instancia;
  }

  public synchronized Inimigo getAlvoAtual() {
    // alvoAtual.diminuirVida(100);
    return alvoAtual;
  }

  public synchronized void setAlvoAtual(Inimigo alvo) {
    this.alvoAtual = alvo;
    System.out.println("ID INIMIGO = " + alvoAtual.getId());
    controleAlgoritmoConsenso.rotacionarParaAtacar(alvoAtual.getId());
  }

  public void setControleAlgoritmoConsenso(ControleAlgoritmoConsenso controleAlgoritmoConsenso) {
    this.controleAlgoritmoConsenso = controleAlgoritmoConsenso;
  }
}

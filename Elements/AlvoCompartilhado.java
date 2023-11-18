package Elements;

public class AlvoCompartilhado {
  private static AlvoCompartilhado instancia = new AlvoCompartilhado();
  private Inimigo alvoAtual;

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
  }
}

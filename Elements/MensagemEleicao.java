package Elements;

import java.io.Serializable;

public class MensagemEleicao implements Serializable {
  //ALTERAR PARA A MENSAGEM 0 E 1
  private int idOrigem;

  public MensagemEleicao(int idOrigem) {
    this.idOrigem = idOrigem;
  }

  public int getIdOrigem() {
    return idOrigem;
  }
}

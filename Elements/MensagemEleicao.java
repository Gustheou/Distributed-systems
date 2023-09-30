package Elements;

import java.io.Serializable;

public class MensagemEleicao implements Serializable {
    private int idOrigem;

    public MensagemEleicao(int idOrigem) {
        this.idOrigem = idOrigem;
    }

    public int getIdOrigem() {
        return idOrigem;
    }
}

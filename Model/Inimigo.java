package Model;

import java.io.Serializable;
import Controller.ControleAlgoritmoConsenso;

public class Inimigo implements Serializable{
    private int id;

    public Inimigo(int id) {
        this.id = id;
    }
    public Inimigo(ControleAlgoritmoConsenso controleAlgoritmoConsenso, int id) {
        controleAlgoritmoConsenso.novoInimigo(id);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

package Elements;

import java.io.Serializable;
import java.util.Random;

import Controller.ControleAlgoritmoConsenso;

public class Inimigo implements Serializable{
    private int id;
    private int vida;
    private ControleAlgoritmoConsenso controleAlgoritmoConsenso;

    public Inimigo(int id) {
        this.id = id;
        // Gere um valor aleatório de vida entre 1 e 100 
        //this.vida = gerarVidaAleatoria();
        this.vida = gerarVida();
    }
    public Inimigo(int id, ControleAlgoritmoConsenso controleAlgoritmoConsenso) {
        this.id = id;
        //this.vida = vida;
        this.controleAlgoritmoConsenso = controleAlgoritmoConsenso;
        controleAlgoritmoConsenso.novoInimigo();
        this.vida = 5;
    }

    public int getId() {
        return id;
    }

    public int getVida() {
        return vida;
    }

    public void diminuirVida(int dano){
        /*
        if(dano>0 || dano<=id){
            this.vida=vida-dano;
        }else{
            this.vida=vida-1; 
        }
        */
        this.vida = vida - 1;
    }

    private int gerarVidaAleatoria() {
        Random random = new Random();
        
        int vidaAleatoria = random.nextInt(100) + 20;
        return vidaAleatoria;
    }

    private int gerarVida(){
        return controleAlgoritmoConsenso.getNumeroDeNave();
    }
}

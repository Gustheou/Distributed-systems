package Elements;

import java.util.Random;

public class Pedra {
    private int id;
    private int vida;

    public Pedra(int id) {
        this.id = id;
        // Gere um valor aleatório de vida entre 1 e 100 
        this.vida = gerarVidaAleatoria();
    }
    public Pedra(int id, int vida) {
        this.id = id;
        this.vida = vida;
    }

    public int getId() {
        return id;
    }

    public int getVida() {
        return vida;
    }

    public void diminuirVida(int dano){
        if(dano>0 || dano<=id){
            this.vida=vida-dano;
        }else{
            this.vida=vida-1; 
        }
        
    }

    private int gerarVidaAleatoria() {
        Random random = new Random();
        
        int vidaAleatoria = random.nextInt(100) + 20;
        return vidaAleatoria;
    }
}

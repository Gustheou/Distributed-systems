package Util;

public class ProcessoEleitoral {
    private static ProcessoEleitoral instancia = new ProcessoEleitoral();
    private boolean isLeader;
    private int liderAtual;
    private int numeroDeRespostas;

    private ProcessoEleitoral() {
        // Construtor privado para evitar instâncias múltiplas
        this.isLeader=false;
        this.liderAtual=-1;
        this.numeroDeRespostas=0;
    }

    public static ProcessoEleitoral getInstance() {
        return instancia;
    }

    // Implemente aqui a lógica do processo de eleição
    public void setLiderAtual(int liderAtual){
        this.liderAtual=liderAtual;
    }
    public void setIsLeader(boolean isLeader){
        this.isLeader=isLeader;
    }
    public void setRespostaPositiva(){
        this.numeroDeRespostas++;
    }
    public void setRespostaNegativa(){
        this.numeroDeRespostas--;
    }

    public int getNumeroDeRespotas(){
        return this.numeroDeRespostas;
    }
    public void zerarNumeroDeRespostas(){
        this.numeroDeRespostas=0;
    }
    public int getLiderAtual(){
        return this.liderAtual;
    }
    public boolean getIsLeader(){
        return this.isLeader;
    }

    
}

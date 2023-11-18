package Elements;

public class ProcessoEleitoral {
    private static ProcessoEleitoral instancia = new ProcessoEleitoral();
    private boolean isLeader;
    private int liderAtual;
    private int numeroDeRespostasPossitivas;

    private ProcessoEleitoral() {
        // Construtor privado para evitar instâncias múltiplas
        this.isLeader=false;
        this.liderAtual=-1;
        this.numeroDeRespostasPossitivas=0;
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
    public void setRespostasPositiva(){
        this.numeroDeRespostasPossitivas++;
    }
    public int getNumeroDeRespotasPositivas(){
        return this.numeroDeRespostasPossitivas;
    }
    public void zeraNumeroDeRespostasPositivas(){
        this.numeroDeRespostasPossitivas=0;
    }
    public int getLiderAtual(){
        return this.liderAtual;
    }
    public boolean getIsLeader(){
        return this.isLeader;
    }
    
    
}

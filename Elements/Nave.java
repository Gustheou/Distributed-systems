package Elements;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
public class Nave extends Thread {
    private double x;
    private double y;
    private double velocidade;
    private double vida;
    private List<Tiro> tiros;
    private int cooldownTiro;
    private int cooldownAtual;
    private boolean isLeader;
    private int id;
    private SistemaDistribuido sistemaDistribuido;

    public Nave(double x, double y,int id, SistemaDistribuido sistemaDistribuido) {
        this.x = x;
        this.y = y;
        this.velocidade = 5;
        this.vida = 100;
        this.tiros = new ArrayList<>();
        this.isLeader = false;
        this.cooldownTiro = 30;
        this.cooldownAtual = 0;
        this.id = id;
        this.sistemaDistribuido = sistemaDistribuido;
    }

    public void mover(String direcao) {
        // Lógica para mover a nave
        if (direcao.equals("esquerda")) {
            this.x -= this.velocidade;
        } else if (direcao.equals("direita")) {
            this.x += this.velocidade;
        }
    }

    public void atirar() {
        // Lógica para disparar tiros
        if (this.cooldownAtual <= 0) {
            // Crie um novo tiro na posição da nave
            Tiro tiro = new Tiro(this.x, this.y);
            this.tiros.add(tiro);
            // Reduza o cooldown para evitar tiros contínuos
            this.cooldownAtual = this.cooldownTiro;
        }
    }
    public void iniciarEleicao() {
        // A nave inicia um processo de eleição
        isLeader = true; // Inicialmente, a nave se propõe como líder
        System.out.println("Pedra de Numero "+ id +" é a atual lider");

        Nave[] outrasNaves = sistemaDistribuido.getNaves();

        for (Nave outraNave : outrasNaves) {
            if (outraNave.getId() != id) {
                // Envie uma mensagem de eleição para todas as outras naves
                boolean aceitaLideranca = outraNave.responderEleicao(id);
                if (aceitaLideranca && outraNave.getId() > id) {
                    // Outra nave aceitou liderança e tem um ID maior, desista da liderança
                    isLeader = false;
                }
            }
        }

        // A nave espera por um período para receber respostas das outras naves

        // Verifique se nenhuma outra nave expressou intenção de ser líder
        if (isLeader) {
            // A nave se torna o líder e ataca a pedra
            Pedra alvo = sistemaDistribuido.escolherAlvo();
            atacar(alvo);
        }
    }

    public boolean responderEleicao(int proponenteId) {
        // Uma nave recebe uma mensagem de eleição e decide se aceita a liderança do proponente
        return id > proponenteId;
    }

    public void atacar(Pedra alvo){
        if(alvo.getVida()>=1){
            System.out.println("Nave " + id + " is attacking Pedra " + alvo.getId()+" Nivel de vida: "+alvo.getVida());
        }else{
            System.out.println("Todos os alvos foram eliminados");
        }
            
    }
    public void resolverProblema(String problema) {
        // Lógica para resolver um problema (colisão com uma pedra)
        // Implemente a lógica de resolução de problemas aqui
    }

    public void atualizar() {
        // Atualiza a posição dos tiros e reduz o cooldown
        for (Tiro tiro : this.tiros) {
            tiro.atualizar();
        }
        if (this.cooldownAtual > 0) {
            this.cooldownAtual--;
        }
    }

    @Override
    public void run() {
        // Inicialize elementos do JavaFX, como imagens das naves
        Image naveImage = new Image("caminho/para/imagem/nave.png");
        ImageView naveImageView = new ImageView(naveImage);

        // Adicione naveImageView ao seu aplicativo JavaFX, defina sua posição usando this.x e this.y

        while (true) {
            // Lógica principal da nave (movimento, tiro, atualização)
            // Chame os métodos apropriados para realizar a lógica do jogo
            if (sistemaDistribuido.getNaves().length>=1) {
                iniciarEleicao();
            }
    
            // Se a nave foi eleita líder, execute a ação de ataque
            if (isLeader) {
                this.mover("direita");
                this.atirar();
                this.atualizar();
            }

            // Atualize a posição de naveImageView com this.x e this.y

            try {
                Thread.sleep(16); // Aguarda um curto período (aproximadamente 60 quadros por segundo)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
 
    


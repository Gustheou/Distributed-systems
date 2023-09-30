package Elements;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Random;

public class SistemaDistribuido {
    private List<Nave> naves;
    private List<Pedra> pedras;
    private boolean jogoEmexecucao=true;

    public SistemaDistribuido() {
        naves = new CopyOnWriteArrayList<>();
        pedras = new CopyOnWriteArrayList<>();
    }

    public void registrarNave(Nave nave) {
        naves.add(nave);
    }

    public void registrarPedra(Pedra pedra) {
        pedras.add(pedra);
        this.jogoEmexecucao=true;
    }

    public void iniciarJogo() {
        while (true) {
            if(jogoEmexecucao){
                 // Lógica de coordenação para escolher qual pedra atacar
                Pedra alvo = escolherAlvo();
                // Notificar todas as naves sobre o alvo escolhido
                if(alvo != null){
                    for (Nave nave : naves) {
                        nave.atacar(alvo);
                    }
                    alvo.diminuirVida(100);
                }else{
                    System.out.println("Alvo não encontrado");
                
                    this.jogoEmexecucao=false;
                }
            }
           
            try {
                Thread.sleep(5000); // Aguarda 5 segundos (ajuste conforme necessário)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }    
    public void criarNavesAutomaticamente(int quantidade) {
        Random random = new Random();

        for (int i = 0; i < quantidade; i++) {
            int id = i + 1; // Gere IDs únicos para cada nave
            int x = random.nextInt(800); // Gere coordenadas X aleatórias 
            int y = random.nextInt(600); // Gere coordenadas Y aleatórias 
            Nave nave = new Nave(id, x, y, this);
            registrarNave(nave);
        }
    }

    public void criarPedrasAutomaticamente(int quantidade) {
        Random random;
        for (int i = 0; i < quantidade; i++) {
            int id = i + 1; // Gere IDs únicos para cada pedra
            random = new Random();
            int vida = random.nextInt(100) + 1; // Gere valores de vida aleatórios de 1 a 100
            Pedra pedra = new Pedra(id, vida);
            System.out.println("Pedra de N "+pedra.getId()+" e de vida "+pedra.getVida()+" foi criada");
            registrarPedra(pedra);
        }
    }
    

    // private Pedra ultimoAlvo = null;

    // public Pedra escolherAlvo() {
    //     // Verifique se há pedras na lista
    //     if (pedras.isEmpty()) {
    //         return null; // Não há pedras disponíveis
    //     }

    //     // Crie um objeto Random para escolher aleatoriamente uma pedra
    //     Random random = new Random();

    //     // Escolha aleatoriamente uma pedra que não seja a mesma que na rodada anterior
    //     Pedra alvo = null;
    //     do {
    //         int indiceAleatorio = random.nextInt(pedras.size());
    //         alvo = pedras.get(indiceAleatorio);
    //     } while (alvo == ultimoAlvo);

    //     // Mantenha o registro do último alvo escolhido
    //     ultimoAlvo = alvo;

    //     return alvo;
    // }
    private int ultimoIndiceAlvo = -1;

public Pedra escolherAlvo() {
    // Verifique se há pedras na lista
    if (pedras.isEmpty()) {
        return null; // Não há pedras disponíveis
    }

    // Encontre a pedra com a maior vida na lista
    int indiceAleatorio;
    do {
        ultimoIndiceAlvo = (ultimoIndiceAlvo + 1) % pedras.size();
        indiceAleatorio = ultimoIndiceAlvo;
        Pedra alvo = pedras.get(indiceAleatorio);

        // Verifique se a pedra já foi destruída, se sim, continue procurando
        if (alvo.getVida() <= 0) {
            continue;
        }

        // Encontrou uma pedra válida, escolha-a como alvo
        return alvo;
    } while (indiceAleatorio != ultimoIndiceAlvo);

    // Se todas as pedras foram destruídas, retorne null
    return null;
}


    
    

    public static void main(String[] args) {
        SistemaDistribuido sistema = new SistemaDistribuido();

        sistema.criarNavesAutomaticamente(6);
        sistema.criarPedrasAutomaticamente(6);

        // Iniciar o jogo
        sistema.iniciarJogo();
    }

    public Nave[] getNaves() {
        return naves.toArray(new Nave[0]);
    }
    
}


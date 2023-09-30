package Elements;
import java.io.*;
import java.net.*;

import java.util.ArrayList;
import java.util.List;
public class Nave extends Thread {
    private double x;
    private double y;
    private double velocidade;
    private double vida;
    private List<Tiro> tiros;
    private int porta;
    private int cooldownTiro;
    private int cooldownAtual;
    private boolean isLeader;
    private int id;
    private SistemaDistribuido sistemaDistribuido;
    private LiderancaSemaphore liderancaSemaphore = LiderancaSemaphore.getInstance();
    private ProcessoEleitoral processoEleitoral = ProcessoEleitoral.getInstance();
    private int liderAtual;
    private boolean aguardandoRespostas;
    private Pedra alvoAtual;
    private ServerSocket serverSocket; 
    

    public Nave(double x, double y,int id, SistemaDistribuido sistemaDistribuido) {
        this.x = x;
        this.y = y;
        this.velocidade = 5;
        this.liderAtual=-1;
        this.vida = 100;
        this.tiros = new ArrayList<>();
        this.isLeader = false;
        this.cooldownTiro = 30;
        this.cooldownAtual = 0;
        this.id = id;
        this.porta=id+1000;
        this.sistemaDistribuido = sistemaDistribuido;
        this.alvoAtual=null;
        try {
            serverSocket = new ServerSocket(porta);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
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
     // Adicione este método para ouvir mensagens de eleição
     private void ouvirMensagens() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
    
                while (true) {
                    try {
                        Object mensagem = in.readObject();
    
                        if (mensagem instanceof MensagemEleicao) {
                            // Trate a mensagem de eleição
                            MensagemEleicao mensagemEleicao = (MensagemEleicao) mensagem;
                            responderEleicao(mensagemEleicao);
                        } else if (mensagem instanceof Integer) {
                            // Trate o ID do alvo a ser atacado
                            int idAlvo = (Integer) mensagem;
                            // Leia o alvo atual do banco de dados compartilhado
                            Pedra alvo = AlvoCompartilhado.getInstance().getAlvoAtual();
                            if (alvo != null) {
                                atacar(alvo);
                            }
                        }
                    } catch (EOFException e) {
                        // Fim da transmissão, saia do loop interno
                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
    
                in.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void iniciarEleicao() throws InterruptedException {
        // A nave inicia um processo de eleição
        Thread ouvirThread = new Thread(this::ouvirMensagens);
        ouvirThread.start();
        synchronized (processoEleitoral){
            //liderancaSemaphore.adquirir();
            isLeader = processoEleitoral.getIsLeader();
            liderAtual = processoEleitoral.getLiderAtual();
            aguardandoRespostas = true;
            //liderancaSemaphore.liberar();
            MensagemEleicao mensagemEleicao = new MensagemEleicao(id);
            
            List<Nave> outrasNavesList = sistemaDistribuido.getNavesList();
            
            for (Nave outraNave : outrasNavesList) {
                if (outraNave.getId() != id) {
                    // Envie uma mensagem de eleição para todas as outras naves
                    enviarMensagemEleicao(outraNave, mensagemEleicao);
                }
            }

            // Aguarde respostas das outras naves por um período de tempo
            try {
                Thread.sleep(5000); // Aguarda 5 segundos para respostas
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (processoEleitoral.getLiderAtual() == id) {
                processoEleitoral.setIsLeader(true);
                System.out.println("Nave " + id + " é o líder!");
                Pedra alvo = sistemaDistribuido.escolherAlvo();
                for (Nave outraNave : outrasNavesList) {
                if (outraNave.getId() != id) {
                    // Envie uma mensagem de eleição para todas as outras naves
                   // Leia o alvo atual do banco de dados compartilhado
                    
                    
                    enviarComandoDeAtaque(alvo);
                    
                
                }
            }
                // A nave se torna o líder e ataca a pedra
            } else {
                System.out.println("Nave " + id + " não é o líder.");
            }

            aguardandoRespostas = false;

        }
        
    }

    public boolean responderEleicao(int proponenteId) {
        // Uma nave recebe uma mensagem de eleição e decide se aceita a liderança do proponente
        return id > proponenteId;
    }

    public void atacar(Pedra alvo){
        if(alvo.getId()>=1){
            System.out.println("Nave " + id + " is attacking Pedra " + alvo.getId()+" Nivel de vida: "+alvo.getVida());
            alvo.diminuirVida(5);
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
    public void responderEleicao(MensagemEleicao mensagem) {
        // Uma nave recebe uma mensagem de eleição e decide se aceita a liderança do proponente
        if (mensagem.getIdOrigem() > id) {
            processoEleitoral.setLiderAtual(mensagem.getIdOrigem());
            liderAtual = mensagem.getIdOrigem();
        }
    }
    public String getHostName(){
        return"127.0.0.1";
    }
    public int getPorta(){
        return this.porta;
    }

    private void enviarMensagemEleicao(Nave destinatario, MensagemEleicao mensagem) {
        try {
            Socket socket = new Socket(destinatario.getHostName(), destinatario.getPorta());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(mensagem);
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void enviarComandoDeAtaque(Pedra alvo) {
        try {
            Nave[] outrasNaves = sistemaDistribuido.getNaves();
    
            for (Nave outraNave : outrasNaves) {
                if (outraNave.getId() != id) {
                    // Crie um soquete para se comunicar com a nave de destino
                    Socket socket = new Socket("localhost", outraNave.getPorta());
    
                    // Envie o comando de ataque
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    if (alvo != null) {
                        int idDoAlvo = alvo.getId();
                        out.writeObject(idDoAlvo);
                        out.flush();
                    }
    
                    // Feche o soquete
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void run() {
        while (true) {
            try{
                //Thread.sleep(5000);
                 if (sistemaDistribuido.getNaves().length>=1) {
                    
                    iniciarEleicao();
                    
                }
            }catch(InterruptedException e){e.printStackTrace();}
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
 
    


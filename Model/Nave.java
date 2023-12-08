package Model;

import java.io.*;
import java.net.*;

import java.util.List;
import java.util.Random;


import Controller.ControleAlgoritmoConsenso;
import Controller.ControleSistemaDistribuido;
import Util.AlvoCompartilhado;
import Util.LiderancaSemaphore;
import Util.MensagemEleicao;
import Util.ProcessoEleitoral;

public class Nave extends Thread {
  private double x;
  private double y;
  private int porta;
  private boolean isLeader;
  private int id;
  private ControleSistemaDistribuido sistemaDistribuido;
  private LiderancaSemaphore liderancaSemaphore = LiderancaSemaphore.getInstance();
  private ProcessoEleitoral processoEleitoral = ProcessoEleitoral.getInstance();
  private int liderAtual;
  private boolean aguardandoRespostas;
  private Inimigo alvoAtual,alvoEscolhido;
  private ServerSocket serverSocket;
  private AlvoCompartilhado alvoCompartilhado;

  private ControleAlgoritmoConsenso controleAlgoritmoConsenso;

  public Nave(int id, ControleSistemaDistribuido sistemaDistribuido, ControleAlgoritmoConsenso controleAlgoritmoConsenso) {
    this.liderAtual = -1;
    this.isLeader = false;
    this.id = id;
    this.porta = id +49152;
    this.sistemaDistribuido = sistemaDistribuido;
    this.alvoAtual = null;
    this.alvoCompartilhado=AlvoCompartilhado.getInstance();
    this.alvoCompartilhado.setControleAlgoritmoConsenso(controleAlgoritmoConsenso);
    this.controleAlgoritmoConsenso = controleAlgoritmoConsenso;
    try {
      serverSocket = new ServerSocket(porta);
    } catch (IOException e) {
      e.printStackTrace();
    }
    controleAlgoritmoConsenso.novaNave(id);
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
              // LIDER ESCOLHE MAS HAVERA OUTRA VOTACAO SE O ALVO SERA O ESCOLHIDO
              // Trate o ID do alvo a ser atacado
              int idAlvo = (Integer) mensagem;
              // Leia o alvo atual do banco de dados compartilhado
              Inimigo alvo = alvoCompartilhado.getAlvoAtual();
              if (alvo != null) {
                atacar(alvo);
              }else{
                System.out.println("Alvo em questão esta nulo");
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
    Inimigo alvo = sistemaDistribuido.escolherAlvo();
    Thread ouvirThread = new Thread(this::ouvirMensagens);
    ouvirThread.start();
    synchronized (processoEleitoral) {
      // liderancaSemaphore.adquirir();
      isLeader = processoEleitoral.getIsLeader();
      controleAlgoritmoConsenso.setMaybeLeader("Nave"+id);
      liderAtual = processoEleitoral.getLiderAtual();
      aguardandoRespostas = true;
      // liderancaSemaphore.liberar();
      MensagemEleicao mensagemEleicao = new MensagemEleicao(id);

      List<Nave> outrasNavesList = sistemaDistribuido.getNavesList();
      
      for (Nave outraNave : outrasNavesList) {
        if (outraNave.getId() != id) {
          // Envie uma mensagem de eleição para todas as outras naves
          enviarMensagemEleicao(outraNave, mensagemEleicao);
        }
      }

      // Aguarde respostas das outras naves por um período de tempo
      //AQUI EH O TEMPO DA ELEICAO
      try {
        Thread.sleep(5000); // Aguarda 5 segundos para respostas
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      int maioria = (outrasNavesList.size()/2)+1;
      if(processoEleitoral.getNumeroDeRespotasPositivas()>=maioria){
        processoEleitoral.setIsLeader(true);
        System.out.println("Nave " + id + " é o líder!");
        //controleAlgoritmoConsenso;
        String idNave = "Nave"+id;
        controleAlgoritmoConsenso.setLeader(idNave);
        System.out.println("nave id: " + idNave);

        alvoCompartilhado.setAlvoAtual(alvo);
        try{
          System.out.println("Nave de ID "+ id +" escolheu pedra de ID "+ alvo.getId());
        }catch(NullPointerException e){
          System.out.println("Nave nula ou não encontrada");
        }
        
        
        for (Nave outraNave : outrasNavesList) {
          if (outraNave.getId() != id) {
            // Envie uma mensagem de eleição para todas as outras naves
            // Leia o alvo atual do banco de dados compartilhado

            enviarComandoDeAtaque(alvoCompartilhado.getAlvoAtual());
          }
        }
        processoEleitoral.zeraNumeroDeRespostasPositivas();
      }else{
        System.out.println("Nave " + id + " não é o líder.");
      }
      aguardandoRespostas = false;

    }
  }

  public boolean responderEleicao(int proponenteId) {
    // Uma nave recebe uma mensagem de eleição e decide se aceita a liderança do
    // proponente
    Random randTom = new Random();

    return randTom.nextInt(2)==1 ? true:false;
  }

  //UM TIRO SO
  public void atacar(Inimigo alvo) {
    //rotacionar e atacar

    if (alvo.getVida() >= 1) {
      System.out.println("Nave " + id + " is attacking Pedra " + alvo.getId() + " Nivel de vida: " + alvo.getVida());
      alvo.diminuirVida(1);
    } else {
      controleAlgoritmoConsenso.eliminarAlvo(id);
      System.out.println("Pedra de ID "+alvo.getId()+" foi eliminada");
    }
  }

  //PROCESSO MAIS DEMOCRATICO 
  //RANDOMICO 0 E 1
  public void responderEleicao(MensagemEleicao mensagem) {
    // Uma nave recebe uma mensagem de eleição e decide se aceita a liderança do
    // proponente
    Random random = new Random();
    double limiteDeAceitacao = 0.5; //limite de aceitação (por exemplo, 0.5 para 50% de chance)
    double chanceDeAceitacao = random.nextDouble(); //gera números entre 0 e 1 
    if(chanceDeAceitacao > limiteDeAceitacao){
      // A nave aceitou a liderança do propante 
      processoEleitoral.setLiderAtual(mensagem.getIdOrigem());
      processoEleitoral.setRespostasPositiva();
      //LABEL PARA APROVAR ELEICAO
      controleAlgoritmoConsenso.aFavorEleicao(id);

      liderAtual = mensagem.getIdOrigem();
    }else{
      //LABEL PARA NEGAR A ELEICAO
      controleAlgoritmoConsenso.contraEleicao(id);
      
      System.out.println("Canditado "+mensagem.getIdOrigem()+" teve sua proposta negada por nave de ID "+id);
    }
   
  }

  public String getHostName() {
    return "127.0.0.1";
  }

  public int getPorta() {
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

  public void enviarComandoDeAtaque(Inimigo alvo) {
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
      try {
        if (sistemaDistribuido.getNaves().length > 1) {

          iniciarEleicao();

        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      // Se a nave foi eleita líder, execute a ação de ataque
      if (isLeader) {
        //escolherAlvo(Random int);
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

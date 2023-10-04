package Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import Controller.ControleAlgoritmoTeste;

import java.util.Random;

public class SistemaDistribuido {
  private List<Nave> naves;
  private List<Pedra> pedras;
  private boolean jogoEmexecucao = true;

  public SistemaDistribuido() {
    naves = new CopyOnWriteArrayList<>();
    pedras = new CopyOnWriteArrayList<>();
  }

  public void registrarNave(Nave nave) {
    naves.add(nave);
  }

  public void registrarPedra(Pedra pedra) {
    pedras.add(pedra);
    this.jogoEmexecucao = true;
  }

  public void iniciarJogo() {
    while (true) {
      if (jogoEmexecucao) {
        // Lógica de coordenação para escolher qual pedra atacar
        Pedra alvo = escolherAlvo();
        // Notificar todas as naves sobre o alvo escolhido
        if (alvo != null) {
          for (Nave nave : naves) {
            // nave.atacar(alvo);
          }
          // alvo.diminuirVida(100);
        } else {
          System.out.println("Alvo não encontrado");

          this.jogoEmexecucao = false;
        }
      }

      try {
        Thread.sleep(5000); // Aguarda 5 segundos (ajuste conforme necessário)
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void criarNavesAutomaticamente(int quantidade, ControleAlgoritmoTeste controleAlgoritmoTeste) {
    for (int i = 0; i < quantidade; i++) {
      int id = i + 1; // Gere IDs únicos para cada nave
      Nave nave = new Nave(id, this);
      nave.start();
      controleAlgoritmoTeste.novaNave();
      System.out.println("Nave de ID: " + nave.getId() + " foi iniciada na porta: " + nave.getPorta());
      registrarNave(nave);
    }
  }

  public void criarPedrasAutomaticamente(int quantidade, ControleAlgoritmoTeste controleAlgoritmoTeste) {
    Random random;
    for (int i = 0; i < quantidade; i++) {
      int id = i + 1; // Gere IDs únicos para cada pedra
      random = new Random();
      controleAlgoritmoTeste.novoInimigo();
      int vida = random.nextInt(100) + 1; // Gere valores de vida aleatórios de 1 a 100
      Pedra pedra = new Pedra(id, vida);
      System.out.println("Pedra de N " + pedra.getId() + " e de vida " + pedra.getVida() + " foi criada");
      registrarPedra(pedra);
    }
  }

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
      // Escolha um novo alvo

      // Defina o novo alvo no banco de dados compartilhado
      AlvoCompartilhado.getInstance().setAlvoAtual(alvo);

      return alvo;
    } while (indiceAleatorio != ultimoIndiceAlvo);

    // Se todas as pedras foram destruídas, retorne null
    return null;
  }

  // ...

  public Pedra encontrarAlvoPorID(int idAlvo) {
    for (Pedra pedra : pedras) {
      if (pedra.getId() == idAlvo) {
        return pedra;
      }
    }
    return null; // Retorna null se nenhum alvo com o ID especificado for encontrado
  }

  // ...
  

  public Nave[] getNaves() {
    return naves.toArray(new Nave[0]);
  }

  public List<Nave> getNavesList() {
    return this.naves;
  }

}
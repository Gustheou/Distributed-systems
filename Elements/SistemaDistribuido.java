package Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import Controller.ControleAlgoritmoConsenso;

import java.util.Random;

public class SistemaDistribuido {
  private List<Nave> naves;
  private List<Inimigo> inimigos;
  private boolean jogoEmexecucao = true;

  public SistemaDistribuido() {
    naves = new CopyOnWriteArrayList<>();
    inimigos = new CopyOnWriteArrayList<>();
  }

  public void registrarNave(Nave nave) {
    naves.add(nave);
  }

  public void registrarinimigo(Inimigo inimigo) {
    inimigos.add(inimigo);
    this.jogoEmexecucao = true;
  }

  public void iniciarJogo() {
    while (true) {
      if (jogoEmexecucao) {
        // Lógica de coordenação para escolher qual inimigo atacar
        Inimigo alvo = escolherAlvo();
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

  public void criarNavesAutomaticamente(int quantidade, ControleAlgoritmoConsenso controleAlgoritmoConsenso) {
    for (int i = 0; i < quantidade; i++) {
      int id = i + 1; // Gere IDs únicos para cada nave
      Nave nave = new Nave(id, this, controleAlgoritmoConsenso);
      nave.start();
      //controleAlgoritmoConsenso.novaNave();
      System.out.println("Nave de ID: " + nave.getId() + " foi iniciada na porta: " + nave.getPorta());
      registrarNave(nave);
    }
  }

  public void criarInimigosAutomaticamente(int quantidade, ControleAlgoritmoConsenso controleAlgoritmoConsenso) {
    Random random;
    for (int i = 0; i < quantidade; i++) {
      int id = i + 1; // Gere IDs únicos para cada inimigo
      random = new Random();
      int vida = random.nextInt(100) + 1; // Gere valores de vida aleatórios de 1 a 100
      Inimigo inimigo = new Inimigo(id, vida, controleAlgoritmoConsenso);
      System.out.println("inimigo de N " + inimigo.getId() + " e de vida " + inimigo.getVida() + " foi criada");
      registrarinimigo(inimigo);
    }
  }

  private int ultimoIndiceAlvo = -1;

  public Inimigo escolherAlvo() {
    // Verifique se há inimigos na lista
    if (inimigos.isEmpty()) {
      System.out.println("Lista de Inimigos vazia, portanto retornado Null");
      return null; // Não há inimigos disponíveis
    }

    // Encontre a inimigo com a maior vida na lista
    int indiceAleatorio;
    do {
      ultimoIndiceAlvo = (ultimoIndiceAlvo + 1) % inimigos.size();
      indiceAleatorio = ultimoIndiceAlvo;
      Inimigo alvo = inimigos.get(indiceAleatorio);

      // Verifique se a inimigo já foi destruída, se sim, continue procurando
      if (alvo.getVida() <= 0) {
        continue;
      }

      // Encontrou uma inimigo válida, escolha-a como alvo
      // Escolha um novo alvo

      // Defina o novo alvo no banco de dados compartilhado
      //AlvoCompartilhado.getInstance().setAlvoAtual(alvo);

      return alvo;
    } while (indiceAleatorio != ultimoIndiceAlvo);

    // Se todas as inimigos foram destruídas, retorne null
    System.out.println("Todos os inimigos foram destruída, portanto retornado Null");
    return null;
  }

  // ...

  public Inimigo encontrarAlvoPorID(int idAlvo) {
    for (Inimigo inimigo : inimigos) {
      if (inimigo.getId() == idAlvo) {
        return inimigo;
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
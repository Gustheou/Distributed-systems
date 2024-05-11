package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import Model.Inimigo;
import Model.Nave;

import java.util.Random;

public class ControleSistemaDistribuido {
  private List<Nave> naves;
  private List<Inimigo> inimigos;
  private boolean jogoEmexecucao = true;

  public ControleSistemaDistribuido() {
    naves = new CopyOnWriteArrayList<>();
    inimigos = new CopyOnWriteArrayList<>();
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
      //controleAlgoritmoConsenso.novaNave(id);
      System.out.println("Nave de ID: " + nave.getId() + " foi iniciada na porta: " + nave.getPorta());
      naves.add(nave);
    }
  }

  public void criarInimigosAutomaticamente(int quantidade, ControleAlgoritmoConsenso controleAlgoritmoConsenso) {
    //Random random;
    for (int i = 0; i < quantidade; i++) {
      int id = i + 1; // Gere IDs únicos para cada inimigo
      Inimigo inimigo = new Inimigo(controleAlgoritmoConsenso, id);
      System.out.println("inimigo de N " + inimigo.getId() + "foi criada");
      inimigos.add(inimigo);
      this.jogoEmexecucao = true;
    }
  }

  public Inimigo escolherAlvo() {
    // Verifique se há inimigos na lista
    if (inimigos.isEmpty()) {
      System.out.println("Lista de Inimigos vazia, portanto retornado Null");
      return null; // Não há inimigos disponíveis
    }
    // Encontre a inimigo com a maior vida na lista
    int indiceAleatorio = new Random().nextInt(inimigos.size());
    Inimigo inimigoEscolhido = inimigos.get(indiceAleatorio);
    return inimigoEscolhido;
  }



  public List<Nave> getNavesList() {
    return this.naves;
  }

  public void removerAlvo(Inimigo alvo) {
    inimigos.remove(alvo);
  }

}
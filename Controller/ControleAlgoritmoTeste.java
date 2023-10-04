package Controller;

import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javax.sound.sampled.Clip;

import Elements.Musicas;
import Elements.SistemaDistribuido;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ControleAlgoritmoTeste implements Initializable{

  @FXML
  private Label startMessageLabel;

  @FXML
  private AnchorPane blackAnchorPane;

  private Thread threadParaCriarNaves;

  //private int[] posicoesDasNaves = {89, 251, 415, 574, 732};
  private int[] posicoesDasNaves = {415, 251, 574, 89, 732};
  private boolean[] statusDeOcupacaoDasNaves = {false, false, false, false, false};

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //mensagemFlutuante(2, startMessageLabel, "APARECER");

    mensagemFlutuante(3, startMessageLabel);
    threadParaCriarNovasNaves().start();
    habilitarMusica();

  }

  private void habilitarMusica() {
    Musicas musicas = new Musicas(clip);
    musicas.audioIniciarAlgoritmo(new File("View/Songs/Weight_of_the_World_the_End_of_YoRHa.wav"));
    ControleTela.algoritmoRodando.setOnCloseRequest(e -> {
      Platform.runLater(() -> {
        musicas.interromperMusica();
        //tem que interromper as threads tambem
        System.out.println("ThreadAlive: " + threadParaCriarNaves.isAlive());
        if (threadParaCriarNaves.isAlive()){
          threadParaCriarNaves.interrupt();
        }
      });
    });
  }

  private Thread threadParaCriarNovasNaves() {
    threadParaCriarNaves = new Thread(() -> {
      try {
        Thread.sleep(5500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      //novaNave(364);
      SistemaDistribuido sistema = new SistemaDistribuido();
  
      sistema.criarNavesAutomaticamente(5, this);
      sistema.criarPedrasAutomaticamente(3, this);
  
      // Iniciar o jogo
      sistema.iniciarJogo();

    });
    return threadParaCriarNaves;
  }

  private static Clip clip;

  public void mensagemFlutuante(int segundos, Label objetoDaMensagem){
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(segundos), objetoDaMensagem);
    fadeTransition.setFromValue(100);
    fadeTransition.setToValue(0);
    fadeTransition.play();
  }

  public void novaNave(){
    int index = 0;
    for (int i = 0; i < statusDeOcupacaoDasNaves.length; i++){
      if (statusDeOcupacaoDasNaves[i] == false) {
        index = i;
        System.out.println(index);
        statusDeOcupacaoDasNaves[i] = true;
        break;
      }
    }
    ImageView ship = new ImageView(new Image("/View/Images/Player.png"));
    ship.setFitWidth(81);
    ship.setFitHeight(79);
    ship.setLayoutX(posicoesDasNaves[index]);
    movimentacao(ship, 2, 621, 443);
    Platform.runLater(() -> {
      blackAnchorPane.getChildren().add(ship);
    });
  }

  public void novoInimigo(){
    Label boss = new Label(inimigoAleatorio((new Random().nextInt(3))-1));
    //boss.setFont(Font.loadFont(getClass().getResourceAsStream("/View/Fonts/FOT_Rodin_Pro_DB.otf"), 22));
    boss.setFont(Font.loadFont(getClass().getResourceAsStream("/View/Fonts/FOT_Rodin_Pro_DB.otf"), (new Random().nextInt(20))+10));
    //boss.setText("NullPointerException");
    boss.setLayoutX(new Random().nextInt(700));
    boss.setTextFill(Color.WHITE);
    //MUDAR A COR
    movimentacao(boss, 4, -65, 100);
    Platform.runLater(() -> {
      blackAnchorPane.getChildren().add(boss);
    });
  }

  public void movimentacao(Node objeto, int duracaoEmSegundos, int posicaoInicialY, int posicaoFinalY){
    TranslateTransition animacao = new TranslateTransition(Duration.seconds(duracaoEmSegundos), objeto);
    animacao.setFromY(posicaoInicialY);
    animacao.setToY(posicaoFinalY);
    animacao.play();
  }

  public String inimigoAleatorio(int opcao){
    switch(opcao){
      case 0: {
        return "Problem";
      }
      case 1: {
        return "Boss";
      }
      case 2: {
        return "Enemy";
      }
      default: {
        return "Null";
      }
    }
  }
}

package Controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.sound.sampled.Clip;

import Util.Musicas;
import Util.Tiro;
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
import javafx.util.Duration;

public class ControleAlgoritmoConsenso implements Initializable{

  @FXML
  private Label startMessageLabel, alvoAtual;

  @FXML
  private AnchorPane blackAnchorPane;

  @FXML
  private ImageView ship;

  @FXML
  private Label respostaEleicao;

  private List<Label> multipleAnswer = new ArrayList<Label>();

  private List<ImageView> multipleShips = new ArrayList<ImageView>();

  private Thread threadParaCriarNaves;

  private ControleSistemaDistribuido sistema;

  List<Label> inimigosList = new ArrayList<Label>();

  private final int POSSIVEIS_INIMIGOS = 23;
  
  private List<Integer> inimigosSelecionaods = new ArrayList<Integer>();

  private int[] posicoesDasNaves = {415, 251, 574, 89, 732};
  private boolean[] statusDeOcupacaoDasNaves = {false, false, false, false, false};

  @Override
  public void initialize(URL location, ResourceBundle resources) {
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
      sistema = new ControleSistemaDistribuido();
      sistema.criarInimigosAutomaticamente(10, this);
      sistema.criarNavesAutomaticamente(5, this);
      sistema.iniciarJogo();
    });
    return threadParaCriarNaves;
  }

  private static Clip clip;

  private void mensagemFlutuante(int segundos, Label objetoDaMensagem){
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(segundos), objetoDaMensagem);
    fadeTransition.setFromValue(100);
    fadeTransition.setToValue(0);
    fadeTransition.play();
  }

  public void novaNave(int newID){
    int index = 0;
    for (int i = 0; i < statusDeOcupacaoDasNaves.length; i++){
      if (statusDeOcupacaoDasNaves[i] == false) {
        index = i;
        statusDeOcupacaoDasNaves[i] = true;
        break;
      }
    }
    ship = new ImageView(new Image("/View/Images/Player.png"));
    ship.setFitWidth(81);
    ship.setFitHeight(79);
    ship.setLayoutX(posicoesDasNaves[index]);
    ship.setId("Nave"+newID);
    System.out.println("ID nave: " + ship.getId());
    multipleShips.add(ship);
    Label info = new Label();
    info.setId(ship.getId());
    multipleAnswer.add(info);
    movimentacao(ship, 2, 621, 443);
    Platform.runLater(() -> {
      blackAnchorPane.getChildren().add(ship);
    });
  }

  public void novoInimigo(){
    int indiceInimigo = new Random().nextInt(POSSIVEIS_INIMIGOS);
    if (inimigosSelecionaods.contains(indiceInimigo)){
      while(inimigosSelecionaods.contains(indiceInimigo)){
        indiceInimigo = new Random().nextInt(POSSIVEIS_INIMIGOS);
      }
    }
    inimigosSelecionaods.add(indiceInimigo);
    Label enemy = new Label(inimigoAleatorio(indiceInimigo));
    enemy.setFont(Font.loadFont(getClass().getResourceAsStream("/View/Fonts/FOT_Rodin_Pro_DB.otf"), (new Random().nextInt(20))+10));
    enemy.setLayoutX(new Random().nextInt(700));
    enemy.setTextFill(Color.WHITE);
    //MUDAR A COR
    int posicaoFinalY = (new Random().nextInt(130))*2;
    movimentacao(enemy, 4, -65, posicaoFinalY);
    Platform.runLater(() -> {
      blackAnchorPane.getChildren().add(enemy);
    });
    inimigosList.add(enemy);
  }

  public Label escolherAlvo(){
    int index = new Random().nextInt(inimigosList.size());
    return inimigosList.get(index);
  }

  public void rotacionarParaAtacar(int id) {
    Label alvo = inimigosList.get(id);
    double posicaoXDaNave = ship.getLayoutX();
    double posicaoYDaNave = ship.getLayoutY();
    double posicaoXDoAlvo = alvo.getLayoutX();
    double posicaoYDoAlvo = alvo.getLayoutY();
    System.out.println(ship.getRotate());
    Platform.runLater(() -> {

      ship.setRotate(calcularAngulacaoDoSeno(posicaoXDaNave, posicaoYDaNave, posicaoXDoAlvo, posicaoYDoAlvo));
    });
  }

  private double calcularAngulacaoDoSeno(double posicaoXDaNave, double posicaoYDaNave, double posicaoXDoAlvo, double posicaoYDoAlvo){
    double catetoAdjacente = Math.abs(posicaoXDaNave - posicaoXDoAlvo);
    double catetoOposto = Math.abs(posicaoYDaNave - posicaoYDoAlvo);
    double hipotenusa = Math.sqrt((catetoAdjacente*catetoAdjacente)+(catetoOposto*catetoOposto));
    return catetoAdjacente/hipotenusa;
  }

  private void atirar(){
    Tiro tiro = new Tiro(ship);
    alvoAtual = escolherAlvo();
    tiro.atirar(alvoAtual);
  }

  private void movimentacao(Node objeto, int duracaoEmSegundos, int posicaoInicialY, int posicaoFinalY){
    TranslateTransition animacao = new TranslateTransition(Duration.seconds(duracaoEmSegundos), objeto);
    animacao.setFromY(posicaoInicialY);
    animacao.setToY(posicaoFinalY);
    animacao.play();
  }

  private String inimigoAleatorio(int opcao){
    switch(opcao){
      case 0: {
        return "Alan";
      }
      case 1: {
        return "Cris";
      }
      case 2: {
        return "Daniel A";
      }
      case 3: {
        return "Daniel N";
      }
      case 4: {
        return "Denise";
      }
      case 5: {
        return "Enzo";
      }
      case 6: {
        return "Eric";
      }
      case 7: {
        return "Euler";
      }
      case 8: {
        return "Gustavo";
      }
      case 9: {
        return "Ian";
      }
      case 10: {
        return "Joao";
      }
      case 11: {
        return "Jose";
      }
      case 12: {
        return "Jorge";
      }
      case 13: {
        return "Lailson";
      }
      case 14: {
        return "Lucas";
      }
      case 15: {
        return "Matheus";
      }
      case 16: {
        return "Nathan";
      }
      case 17: {
        return "Rafaela";
      }
      case 18: {
        return "Raman";
      }
      case 19: {
        return "Samanta";
      }
      case 20: {
        return "Tacio";
      }
      case 21: {
        return "Vinicius";
      }
      case 22: {
        return "Vitor";
      }
      case 23: {
        return "Weslei";
      }
      default: {
        return "NullPointerException";
      }
    }
  }

  public void aFavorEleicao(int id){
    ImageView ship = getNave("Nave"+id);
    Label respostaEleicao = getInfoLabel(ship.getId());
    respostaEleicao.setLayoutX(ship.getLayoutX());
    respostaEleicao.setLayoutY(450);
    Platform.runLater(() -> {
      if (!blackAnchorPane.getChildren().contains(respostaEleicao)){
        blackAnchorPane.getChildren().add(respostaEleicao);
        respostaEleicao.setVisible(false);
      }
      respostaEleicao.setVisible(true);
      respostaEleicao.setText("Sim");
    });
  }

  public void contraEleicao(int id){
    ImageView ship = getNave("Nave"+id);
    Label respostaEleicao = getInfoLabel(ship.getId());
    respostaEleicao.setLayoutX(ship.getLayoutX());
    respostaEleicao.setLayoutY(450);
    
    Platform.runLater(() -> {
      if (!blackAnchorPane.getChildren().contains(respostaEleicao)){
        respostaEleicao.setVisible(false);
        blackAnchorPane.getChildren().add(respostaEleicao);
      }
      respostaEleicao.setText("Não");
      respostaEleicao.setVisible(true);
    });
  }

  private ImageView getNave(String id){
    ImageView naveEscolhida = new ImageView();
    for(ImageView nave : multipleShips){
      if (nave.getId().equals(id)){
        naveEscolhida = nave;
        break;
      }
    }
    return naveEscolhida;
  }

  private Label getInfoLabel(String id){
    Label labelEscolhido = new Label();
    for(Label nave : multipleAnswer){
      if (nave.getId().equals(id)){
        labelEscolhido = nave;
        break;
      }
    }
    labelEscolhido.setFont(Font.loadFont(getClass().getResourceAsStream("/View/Fonts/FOT_Rodin_Pro_DB.otf"), 12));
    labelEscolhido.setTextFill(Color.WHITE);
    return labelEscolhido;
  }
  
  public int getNumeroDeNave(){
    int contador = 0;
    for (int i = 0; i < statusDeOcupacaoDasNaves.length; i++){
      if (statusDeOcupacaoDasNaves[i] == true) {
        contador = contador + 1;
      }
    }
    return contador;
  }

  public void setLeader(String id){
    Label lider = getInfoLabel(id);
    Platform.runLater(() -> {
      lider.setText("Lider");

    });
  }

  public void setMaybeLeader(String id){
    Label lider = getInfoLabel(id);
    Platform.runLater(() -> {
      lider.setText("Lider?");

    });
  }

  public void eliminarAlvo(int id) {
    Label inimigo = inimigosList.get(id);
    inimigo.setVisible(false);
  }
}

package Model;

import Controller.ControleAlgoritmoConsenso;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Tiro extends Thread{
  @FXML
  private ImageView bulletImage;
  @FXML
  private ImageView nave;
  @FXML
  private Label alvo;

  private final double VELOCIDADE_DO_TIRO = 5000;
  private final String CAMINHO_PARA_IMAGEM_DO_TIRO = "/View/Images/Bullet.png";
  private double origemDoTiroX, origemDoTiroY;
  private ControleAlgoritmoConsenso controleAlgoritmoConsenso;

  public Tiro(ImageView naveDeOrigem, ControleAlgoritmoConsenso controleAlgoritmoConsenso) {
    this.nave = naveDeOrigem;
    bulletImage = new ImageView(new Image(CAMINHO_PARA_IMAGEM_DO_TIRO));
    bulletImage.setFitWidth(10);
    bulletImage.setFitHeight(25);
    this.controleAlgoritmoConsenso = controleAlgoritmoConsenso;
  }
  
  public ImageView getBulletImage() {
    return this.bulletImage;
  }

  public void setAlvo(Label alvo){
    this.alvo = alvo;
  }

  public void atirar(Label inimigoDeDestino){
    double destinoX = inimigoDeDestino.getLayoutX();
    double destinoY = inimigoDeDestino.getLayoutY();
    //Thread.sleep("1000");
    origemDoTiroX = nave.getLayoutX();
    origemDoTiroY = nave.getLayoutY()+(nave.getFitHeight()/2);
    //rotacionarParaAtacar(inimigoDeDestino);
    controleAlgoritmoConsenso.movimentacao(bulletImage, 2, origemDoTiroX, destinoX, origemDoTiroY, destinoY);
    controleAlgoritmoConsenso.movimentacao(bulletImage, 2, 732, 732, 500, 700);
    //tiroEmMovimento(origemDoTiroX, origemDoTiroY, destinoX, destinoY);
  }

  public void tiroEmMovimento(double posicaoInicialEixoX, double posicaoInicialEixoY, double posicaoFinalEixoX, double posicaoFinalEixoY){
    TranslateTransition animacaoDoTiro = new TranslateTransition(Duration.millis(VELOCIDADE_DO_TIRO), bulletImage);
    animacaoDoTiro.setFromX(posicaoInicialEixoX);
    animacaoDoTiro.setFromY(posicaoInicialEixoY);
    animacaoDoTiro.setToX(posicaoFinalEixoX);
    animacaoDoTiro.setToY(posicaoFinalEixoY);
    animacaoDoTiro.play();
  }

  public void rotacionarParaAtacar(Label alvo) {
    double posicaoXDaNave = origemDoTiroX;
    double posicaoYDaNave = origemDoTiroY;
    double posicaoXDoAlvo = alvo.getLayoutX();
    double posicaoYDoAlvo = alvo.getLayoutY();
    System.out.println(nave.getRotate());
    bulletImage.setRotate(calcularAngulacaoDoSeno(posicaoXDaNave, posicaoYDaNave, posicaoXDoAlvo, posicaoYDoAlvo));
  }

  private double calcularAngulacaoDoSeno(double posicaoXDaNave, double posicaoYDaNave, double posicaoXDoAlvo, double posicaoYDoAlvo){
    double catetoAdjacente = Math.abs(posicaoXDaNave - posicaoXDoAlvo);
    double catetoOposto = Math.abs(posicaoYDaNave - posicaoYDoAlvo);
    double hipotenusa = Math.sqrt((catetoAdjacente*catetoAdjacente)+(catetoOposto*catetoOposto));
    return catetoAdjacente/hipotenusa;
  }

  @Override
  public void run(){
    try {
      atirar(alvo);
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

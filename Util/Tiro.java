package Util;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Tiro {
  @FXML
  private ImageView bulletImage;
  @FXML
  private ImageView nave;

  private final double VELOCIDADE_DO_TIRO = 5000;
  private final String CAMINHO_PARA_IMAGEM_DO_TIRO = "/View/Images/Bullet.png";
  private double origemDoTiroX, origemDoTiroY;

  public Tiro(ImageView naveDeOrigem) {
    this.nave = naveDeOrigem;
    bulletImage = new ImageView(new Image(CAMINHO_PARA_IMAGEM_DO_TIRO));
    bulletImage.setFitWidth(10);
    bulletImage.setFitHeight(25);
  }
  
  public ImageView getBulletImage() {
    return this.bulletImage;
  }

  public void atirar(Label inimigoDeDestino){
    double destinoX = inimigoDeDestino.getLayoutX();
    double destinoY = inimigoDeDestino.getLayoutY();
    rotacionarParaAtacar(inimigoDeDestino);
    //Thread.sleep("1000");
    double origemDoTiroX = nave.getLayoutX();
    double origemDoTiroY = nave.getLayoutY()+(nave.getFitHeight()/2);
    
    tiroEmMovimento(origemDoTiroX, origemDoTiroY, destinoX, destinoY);
  }

  public void tiroEmMovimento(double posicaoInicialEixoX, double posicaoInicialEixoY, double posicaoFinalEixoX, double posicaoFinalEixoY){
    TranslateTransition animacaoDoTiro = new TranslateTransition(Duration.millis(VELOCIDADE_DO_TIRO), bulletImage);
    animacaoDoTiro.setFromX(origemDoTiroX);
    animacaoDoTiro.setFromY(origemDoTiroY);
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
    nave.setRotate(calcularAngulacaoDoSeno(posicaoXDaNave, posicaoYDaNave, posicaoXDoAlvo, posicaoYDoAlvo));
  }

  private double calcularAngulacaoDoSeno(double posicaoXDaNave, double posicaoYDaNave, double posicaoXDoAlvo, double posicaoYDoAlvo){
    double catetoAdjacente = Math.abs(posicaoXDaNave - posicaoXDoAlvo);
    double catetoOposto = Math.abs(posicaoYDaNave - posicaoYDoAlvo);
    double hipotenusa = Math.sqrt((catetoAdjacente*catetoAdjacente)+(catetoOposto*catetoOposto));
    return catetoAdjacente/hipotenusa;
  }
}

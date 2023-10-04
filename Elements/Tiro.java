package Elements;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Tiro {
  private double x;
  private double y;
  private final double VELOCIDADE_DO_TIRO = 1500;
  private ImageView bulletImage;
  private final String pathToBulletImage = "/View/Images/Bullet.png";

  public Tiro(double x, double y) {
    this.x = x;
    this.y = y;

    // Inicialize a imagem do tiro (certifique-se de ter a imagem adequada)
    // Image tiroImage = new Image("/View/Images/Cursor.png");
    // this.imageView = new ImageView(tiroImage);

    // Defina a posição inicial da imagem do tiro com base em this.x e this.y
    // this.imageView.setX(this.x);
    // this.imageView.setY(this.y);

    // Adicione a imageView do tiro ao seu aplicativo JavaFX
    // Exemplo: rootPane.getChildren().add(this.imageView);
    bulletImage = new ImageView(new Image(pathToBulletImage));
  }
  
  // Getter para a imageView do tiro
  public ImageView getBulletImage() {
    return this.bulletImage;
  }

  public void tiroEmMovimento(int posicaoInicialEixoX, int posicaoInicialEixoY, int posicaoFinalEixoX, int posicaoFinalEixoY){
    TranslateTransition animacaoDoTiro = new TranslateTransition(Duration.millis(VELOCIDADE_DO_TIRO), bulletImage);
    animacaoDoTiro.setFromX(posicaoInicialEixoX);
    animacaoDoTiro.setFromY(posicaoFinalEixoY);
    //fazer o to (destino). Pegar informacao de onde esta a pedra
    //animacaoDoTiro.setToX(posicaoFinalEixoX);
    //animacaoDoTiro.setToY(posicaoFinalEixoY);
    animacaoDoTiro.play();
  }
}

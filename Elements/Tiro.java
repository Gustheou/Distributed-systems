package Elements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tiro {
    private double x;
    private double y;
    private double velocidade;
    private ImageView imageView;

    public Tiro(double x, double y) {
        this.x = x;
        this.y = y;
        this.velocidade = 8;

        // Inicialize a imagem do tiro (certifique-se de ter a imagem adequada)
        //Image tiroImage = new Image("/View/Images/Cursor.png");
        //this.imageView = new ImageView(tiroImage);

        // Defina a posição inicial da imagem do tiro com base em this.x e this.y
        //this.imageView.setX(this.x);
        //this.imageView.setY(this.y);

        // Adicione a imageView do tiro ao seu aplicativo JavaFX
        // Exemplo: rootPane.getChildren().add(this.imageView);
    }

    public void atualizar() {
        // Move o tiro para cima (ou na direção adequada)
        this.y -= this.velocidade;

        // Atualize a posição da imageView do tiro para refletir a nova posição
        //this.imageView.setY(this.y);
    }

    // Getter para a imageView do tiro
    public ImageView getImageView() {
        return this.imageView;
    }

    
}

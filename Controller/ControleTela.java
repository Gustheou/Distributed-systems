package Controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ControleTela {

  private boolean lockAlgorithm = false;

  private final Image AlgorithmSelected = new Image("/View/Images/AlgoritmoTesteSelectedSD.png");
  private final Image AlgorithmUnselected = new Image("/View/Images/AlgoritmoTesteSD.png");

  @FXML
  private ImageView firstAlgorithmImageView;
  
  @FXML
  private ImageView cursorImageView;

  @FXML
  void firstAlgorithmImageViewOnMouseClicked(MouseEvent event) {
    cursorImageView.setVisible(true);
    firstAlgorithmImageView.setImage(AlgorithmSelected);
    lockAlgorithm = true;

    try {
      Stage algoritmoRodando = new Stage();
      Scene algoritmoCena = new Scene(FXMLLoader.load(getClass().getResource("/View/Fxml/ExecucaoAlgoritmo.fxml")));
      algoritmoRodando.setResizable(false);
      algoritmoRodando.setScene(algoritmoCena);
      
      algoritmoRodando.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void firstAlgorithmImageViewOnMouseEntered(MouseEvent event) {
    if (!lockAlgorithm){
      firstAlgorithmImageView.setImage(AlgorithmSelected);
    }
  }

  @FXML
  void firstAlgorithmImageViewOnMouseExited(MouseEvent event) {
    if (!lockAlgorithm){
      firstAlgorithmImageView.setImage(AlgorithmUnselected);
    }
  }
}

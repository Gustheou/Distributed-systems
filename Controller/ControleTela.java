package Controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ControleTela {

  private boolean lockAlgorithm = false;

  private final Image algorithmSelected = new Image("/View/Images/AlgoritmoTesteSelectedSD.png");
  private final Image algorithmUnselected = new Image("/View/Images/AlgoritmoTesteSD.png");

  @FXML
  private ImageView firstAlgorithmImageView, startButtonImageView, cursorImageView, auxStartImageView;

  @FXML
  private Label algorithmDescription;

  @FXML
  private AnchorPane auxStartPane;

  @FXML
  private void firstAlgorithmImageViewOnMouseClicked(MouseEvent event) {
    cursorImageView.setVisible(true);
    firstAlgorithmImageView.setImage(algorithmSelected);
    lockAlgorithm = true;
    auxStartImageView.setVisible(true);
    startButtonImageView.setVisible(true);
    algorithmDescription.setVisible(true);

  }

  @FXML
  private void firstAlgorithmImageViewOnMouseEntered(MouseEvent event) {
    if (!lockAlgorithm) {
      firstAlgorithmImageView.setImage(algorithmSelected);
    }
  }

  @FXML
  private void firstAlgorithmImageViewOnMouseExited(MouseEvent event) {
    if (!lockAlgorithm) {
      firstAlgorithmImageView.setImage(algorithmUnselected);
    }
  }

  @FXML
  private void startButtonImageViewOnMouseClicked(MouseEvent event) {
    resetChoices();
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
  private void startButtonImageViewOnMouseEntered(MouseEvent event) {
    auxStartPane.setVisible(true);
  }

  @FXML
  private void startButtonImageViewOnMouseExited(MouseEvent event) {
    auxStartPane.setVisible(false);
  }

  private void resetChoices(){
    lockAlgorithm = false;
    algorithmDescription.setVisible(false);
    startButtonImageView.setVisible(false);
    auxStartImageView.setVisible(false);
    cursorImageView.setVisible(false);
    firstAlgorithmImageView.setImage(algorithmUnselected);
  }
}

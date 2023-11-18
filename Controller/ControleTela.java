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
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ControleTela {

  private boolean lockAlgorithm = false;

  private final Image algorithmSelected = new Image("/View/Images/AlgoritmoConsensoSelectedSD.png");
  private final Image algorithmUnselected = new Image("/View/Images/AlgoritmoDeConsenso.png");

  @FXML
  private ImageView consensusAlgorithmImageView, startButtonImageView, cursorImageView, auxStartImageView;

  @FXML
  private Label algorithmDescription;

  @FXML
  private AnchorPane auxStartPane;

  public static Stage algoritmoRodando;

  private final String DESCRICAO_CONSENSO = "Para chegar a um consenso, todo nó começa no estado indeciso e propõe um único valor, valor esse que será\n"+
  "representado por um alvo. E assim esse nós se elegirá como líder.\n"+
  "Se a maioria dos nós votarem a favor daquele nó como líder, então todos os nós se preparam e atacam o alvo\n"+
  "escolhido pelo líder. Posteriormente, um novo líder será escolhido e esse processo se repete até não restarem \n"+
  "alvos.";

  @FXML
  private void consensusAlgorithmImageViewOnMouseClicked(MouseEvent event) {
    cursorImageView.setVisible(true);
    consensusAlgorithmImageView.setImage(algorithmSelected);
    lockAlgorithm = true;
    auxStartImageView.setVisible(true);
    startButtonImageView.setVisible(true);
    setarDescricao(DESCRICAO_CONSENSO);
    algorithmDescription.setVisible(true);
  }

  private void setarDescricao(String descricao) {
    algorithmDescription.setFont(Font.loadFont(getClass().getResourceAsStream("/View/Fonts/FOT_Rodin_Pro_DB.otf"), 12));
    algorithmDescription.setText(descricao);
  }

  @FXML
  private void consensusAlgorithmImageViewOnMouseEntered(MouseEvent event) {
    if (!lockAlgorithm) {
      consensusAlgorithmImageView.setImage(algorithmSelected);
    }
  }

  @FXML
  private void consensusAlgorithmImageViewOnMouseExited(MouseEvent event) {
    if (!lockAlgorithm) {
      consensusAlgorithmImageView.setImage(algorithmUnselected);
    }
  }

  @FXML
  private void startButtonImageViewOnMouseClicked(MouseEvent event) {
    resetChoices();
    newScene();
  }

  private void newScene(){
    try {
      algoritmoRodando = new Stage();
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
    consensusAlgorithmImageView.setImage(algorithmUnselected);
  }
}

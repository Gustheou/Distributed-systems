package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class ControleAlgoritmoTeste implements Initializable{

  @FXML
  private Label startMessageLabel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //mensagemFlutuante(2, startMessageLabel, "APARECER");

    mensagemFlutuante(2, startMessageLabel);
  }

  public void mensagemFlutuante(int segundos, Label objetoDaMensagem){
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(segundos), objetoDaMensagem);
    fadeTransition.setFromValue(100);
    fadeTransition.setToValue(0);
    fadeTransition.play();
  }
}

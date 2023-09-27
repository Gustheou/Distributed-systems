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

    mensagemFlutuante(2, startMessageLabel, "DESAPARECER");
  }

  public void mensagemFlutuante(int segundos, Label objetoDaMensagem, String caso){
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(segundos), objetoDaMensagem);
    switch(caso){
      case "APARECER": {
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(100);
        fadeTransition.playFromStart();
        break;
      }
      case "DESAPARECER": {
        System.out.println("Entrou aqui");
        fadeTransition.setFromValue(100);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        break;
      }
      default: {
        break;
      }
    }
    
  }

}

package Util;
import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Controller.*;


public class MudancaDeTela {
  private static Stage stage;
  private static Scene telaMenu;

/* ***************************************************************
* Metodo: init
* Funcao: Iniciar a exibição de telas
* Parametros: cenario=responsavel por permitir o uso de telas
* Retorno: void
*************************************************************** */
  public static void init (Stage cenario) throws IOException {
    new ControleTela();
    stage = cenario;
    Parent fxmlTelaInicial = FXMLLoader.load(MudancaDeTela.class.getResource("/View/Fxml/Tela.fxml"));
    telaMenu = new Scene (fxmlTelaInicial);

    //cenario.getIcons().add(new Image("/View/Images/Icon.png"));
    cenario.setScene(telaMenu);
    cenario.show();
    cenario.setOnCloseRequest(e -> {
      Platform.exit();
      System.exit(0);
    });
  }//Fim do metodo start
}// Fim da classe Principal

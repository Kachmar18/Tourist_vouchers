package tourist_vouchers.v17_tourist_vouchers.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tourist_vouchers.v17_tourist_vouchers.MainApp;


import java.io.IOException;

public class InfoViewController {
    @FXML
    private Button btnGoBack;

    @FXML
    public void handleGoBack(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApp.class.getResource("/tourist_vouchers/v17_tourist_vouchers/login_view.fxml")
        );
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}

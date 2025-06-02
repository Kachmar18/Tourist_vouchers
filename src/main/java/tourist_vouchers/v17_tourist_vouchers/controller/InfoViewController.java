package tourist_vouchers.v17_tourist_vouchers.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import tourist_vouchers.v17_tourist_vouchers.util.WindowUtil;

import java.io.IOException;

public class InfoViewController {
    @FXML
    public void handleGoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        WindowUtil.switchScene(stage, "/tourist_vouchers/v17_tourist_vouchers/login_view.fxml");
    }
}

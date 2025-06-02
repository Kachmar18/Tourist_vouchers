package tourist_vouchers.v17_tourist_vouchers.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import tourist_vouchers.v17_tourist_vouchers.MainApp;
import tourist_vouchers.v17_tourist_vouchers.model.ClientChoice;
import tourist_vouchers.v17_tourist_vouchers.services.ClientChoiceService;
import tourist_vouchers.v17_tourist_vouchers.util.AlertUtil;

public class LoginController {
    @FXML
    private Button btnLogin, btnCreateAccount, btnInfoApp;
    @FXML
    private Pane logInPane, signUpPane;
    @FXML
    private PasswordField txtPassword, txtPasswordSignUp;
    @FXML
    private TextField txtPhone, txtUsername, txtUsernameSignUp;

    private final ClientChoiceService clientService = new ClientChoiceService();

    @FXML
    void switchToSignUpPane() {
        logInPane.setVisible(false);
        signUpPane.setVisible(true);
    }

    @FXML
    void switchToLoginPane() {
        signUpPane.setVisible(false);
        logInPane.setVisible(true);
    }

    @FXML
    void handleCreateAccount(ActionEvent event) {
        String name = txtUsernameSignUp.getText().trim();
        String phone = txtPhone.getText().trim();
        String password = txtPasswordSignUp.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            AlertUtil.showError("Помилка", "Усі поля мають бути заповнені.");
            return;
        }

        ClientChoice client = clientService.loginClient(name, password);
        if (client != null) {
            openMainView(event);
        } else {
            AlertUtil.showError("Помилка", "Невірне ім’я або пароль.");
        }
    }

    @FXML
    void handleLogin(ActionEvent event) throws IOException {
        String name = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (name.isEmpty() || password.isEmpty()) {
            AlertUtil.showError("Помилка", "Введіть логін та пароль.");
            return;
        }

        ClientChoice client = clientService.loginClient(name, password);
        if (client != null) {
            openMainView(event);
        } else {
            AlertUtil.showError("Помилка", "Невірне ім’я або пароль.");
        }
    }

    private void openMainView(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/tourist_vouchers/v17_tourist_vouchers/main_view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showError("Помилка", "Не вдалося відкрити головне вікно.");
        }
    }


    @FXML
    void handleInfoApp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApp.class.getResource("/tourist_vouchers/v17_tourist_vouchers/info_view.fxml")
        );
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

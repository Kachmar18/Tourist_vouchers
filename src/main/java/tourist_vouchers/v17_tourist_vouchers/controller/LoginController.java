package tourist_vouchers.v17_tourist_vouchers.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import tourist_vouchers.v17_tourist_vouchers.MainApp;
import tourist_vouchers.v17_tourist_vouchers.model.ClientChoice;
import tourist_vouchers.v17_tourist_vouchers.services.ClientChoiceService;
import tourist_vouchers.v17_tourist_vouchers.util.AlertUtil;
import tourist_vouchers.v17_tourist_vouchers.util.WindowUtil;

public class LoginController {
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
    void handleCreateAccount(ActionEvent event) throws IOException {
        String name = txtUsernameSignUp.getText().trim();
        String phone = txtPhone.getText().trim();
        String password = txtPasswordSignUp.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            AlertUtil.showError("Помилка", "Усі поля мають бути заповнені.");
            return;
        }

        boolean registered = clientService.registerClient(name, phone, password);
        if (registered) {
            ClientChoice client = clientService.loginClient(name, password); // після реєстрації логін
            if (client != null) {
                openMainView(event, client);
            } else {
                AlertUtil.showError("Помилка", "Щось пішло не так під час логіну.");
            }
        } else {
            AlertUtil.showError("Помилка", "Користувач з таким ім’ям вже існує.");
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
            openMainView(event, client);
        } else {
            AlertUtil.showError("Помилка", "Невірне ім’я або пароль.");
        }
    }

    private void openMainView(ActionEvent event, ClientChoice client) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/tourist_vouchers/v17_tourist_vouchers/main_view.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.setCurrentClient(client); // передаємо поточного користувача

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }



    @FXML
    void handleInfoApp(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        WindowUtil.switchScene(stage, "/tourist_vouchers/v17_tourist_vouchers/info_view.fxml");
    }
}

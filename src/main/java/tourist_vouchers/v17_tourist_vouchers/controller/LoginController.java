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
import java.util.logging.Level;
import java.util.logging.Logger;

import tourist_vouchers.v17_tourist_vouchers.MainApp;
import tourist_vouchers.v17_tourist_vouchers.model.ClientChoice;
import tourist_vouchers.v17_tourist_vouchers.services.ClientChoiceService;
import tourist_vouchers.v17_tourist_vouchers.util.*;

public class LoginController {
    @FXML
    private Pane logInPane, signUpPane;
    @FXML
    PasswordField txtPassword;
    @FXML
    private PasswordField txtPasswordSignUp;
    @FXML
    private TextField txtPhone;
    @FXML
    TextField txtUsername;
    @FXML
    private TextField txtUsernameSignUp;

    private final ClientChoiceService clientService = new ClientChoiceService();
    private static final Logger logger = LogUtil.getLogger();

    @FXML
    private void initialize() {
        if (!DBConnection.isDatabaseConnected()) {
            AlertUtil.showError("Помилка підключення", "Не вдалося підключитися до бази даних.\nПрограма буде закрита.");
            logger.severe("Неможливо підключитися до бази даних при запуску LoginController");
            EmailUtil.sendCriticalError("Помилка підключення до БД", new RuntimeException("DB unavailable"));
            Stage stage = (Stage) logInPane.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void switchToSignUpPane() {
        logger.info("Перехід на панель реєстрації");
        logInPane.setVisible(false);
        signUpPane.setVisible(true);
    }

    @FXML
    void switchToLoginPane() {
        logger.info("Перехід на панель входу");
        signUpPane.setVisible(false);
        logInPane.setVisible(true);
    }

    @FXML
    void handleCreateAccount(ActionEvent event) throws IOException {
        String name = txtUsernameSignUp.getText().trim();
        String phone = txtPhone.getText().trim();
        String password = txtPasswordSignUp.getText().trim();

        logger.info("Спроба створення облікового запису: " + name);

        try {
            if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                AlertUtil.showError("Помилка", "Усі поля мають бути заповнені.");
                return;
            }

            if (!Validator.isValidName(name)) {
                AlertUtil.showError("Помилка", "Ім’я повинно містити щонайменше 3 символи.");
                return;
            }

            if (!Validator.isValidPhone(phone)) {
                AlertUtil.showError("Помилка", "Телефон повинен містити рівно 9 цифр.");
                return;
            }

            if (!Validator.isValidPassword(password)) {
                AlertUtil.showError("Помилка", "Пароль повинен містити щонайменше 4 символи.");
                return;
            }

            boolean registered = clientService.registerClient(name, phone, password);
            if (registered) {
                ClientChoice client = clientService.loginClient(name, password);
                if (client != null) {
                    logger.info("Користувач зареєстрований та увійшов: " + name);
                    openMainView(event, client);
                } else {
                    logger.warning("Не вдалося увійти після реєстрації: " + name);
                    AlertUtil.showError("Помилка", "Щось пішло не так під час логіну.");
                }
            } else {
                logger.warning("Реєстрація не вдалася. Користувач уже існує: " + name);
                AlertUtil.showError("Помилка", "Користувач з таким ім’ям вже існує.");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Критична помилка під час створення облікового запису: " + name, e);
            EmailUtil.sendCriticalError("Критична помилка під час реєстрації", e);
            AlertUtil.showError("Помилка", "Сталася критична помилка. Адміністратор повідомлений.");
        }
    }


    @FXML
    void handleLogin(ActionEvent event) throws IOException {
        String name = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        logger.info("Спроба входу користувача: " + name);

        if (name.isEmpty() || password.isEmpty()) {
            AlertUtil.showError("Помилка", "Введіть логін та пароль.");
            return;
        }

        if (!Validator.isValidName(name)) {
            AlertUtil.showError("Помилка", "Ім’я повинно містити щонайменше 3 символи.");
            return;
        }

        if (!Validator.isValidPassword(password)) {
            AlertUtil.showError("Помилка", "Пароль повинен містити щонайменше 4 символи.");
            return;
        }

        try {
            ClientChoice client = clientService.loginClient(name, password);
            if (client != null) {
                logger.info("Успішний вхід користувача: " + name);
                openMainView(event, client);
            } else {
                logger.warning("Невдалий вхід користувача: " + name);
                AlertUtil.showError("Помилка", "Невірне ім’я або пароль.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Критична помилка під час входу користувача: " + name, e);
            EmailUtil.sendCriticalError("Критична помилка під час входу", e);
            AlertUtil.showError("Помилка", "Виникла критична помилка. Адміністратору надіслано повідомлення.");
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

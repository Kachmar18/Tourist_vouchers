package tourist_vouchers.v17_tourist_vouchers.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import tourist_vouchers.v17_tourist_vouchers.MainApp;
import tourist_vouchers.v17_tourist_vouchers.model.Admin;
import tourist_vouchers.v17_tourist_vouchers.model.Client;
import tourist_vouchers.v17_tourist_vouchers.services.UserService;
import tourist_vouchers.v17_tourist_vouchers.util.*;

public class LoginController {
    public Button btnUserType;
    public ImageView imgUserType;
    public Label lblSpecialPassword;
    public PasswordField txtSpecialPassword;
    @FXML
    private Pane logInPane, signUpPane;
    @FXML
    PasswordField txtPassword, txtPasswordSignUp;
    @FXML
    private TextField txtPhone, txtUsernameSignUp, txtUsername;


    private final UserService userService = new UserService();
    private static final Logger logger = LogUtil.getLogger();
    private boolean isAdminMode = false;

    @FXML
    private void initialize() {
        if (!DBConnection.isDatabaseConnected()) {
            AlertUtil.showError("Не вдалося підключитися до бази даних.\nПрограма буде закрита.");
            logger.severe("Неможливо підключитися до бази даних при запуску LoginController");
            EmailUtil.sendCriticalError("Помилка підключення до БД", new RuntimeException("DB unavailable"));
            Stage stage = (Stage) logInPane.getScene().getWindow();
            stage.close();
        }

        updateUserTypeUI();
    }

    @FXML
    public void changeTypeUser() {
        isAdminMode = !isAdminMode;
        updateUserTypeUI();
        setupAdminRegistrationFields();
        logger.info("Змінено режим на: " + (isAdminMode ? "Адміністратор" : "Клієнт"));
    }

    private void updateUserTypeUI() {
        if (isAdminMode) {
            btnUserType.setText("Адміністратор");
            imgUserType.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(WindowUtil.ADMIN_ICON))));
        } else {
            btnUserType.setText("Клієнт");
            imgUserType.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(WindowUtil.USER_ICON))));
        }
    }
    private void setupAdminRegistrationFields() {
        boolean showAdminFields = isAdminMode && signUpPane.isVisible();
        lblSpecialPassword.setVisible(showAdminFields);
        txtSpecialPassword.setVisible(showAdminFields);
    }


    @FXML
    void switchToSignUpPane() {
        logger.info("Перехід на панель реєстрації");
        logInPane.setVisible(false);
        signUpPane.setVisible(true);
        setupAdminRegistrationFields();
    }

    @FXML
    void switchToLoginPane() {
        logger.info("Перехід на панель входу");
        signUpPane.setVisible(false);
        logInPane.setVisible(true);
    }

    @FXML
    void handleCreateAccount(ActionEvent event){
        String fullName = txtUsernameSignUp.getText().trim();
        String phone = txtPhone.getText().trim();
        String password = txtPasswordSignUp.getText().trim();
        String specialPassword = txtSpecialPassword.getText().trim();

        logger.info("Спроба створення облікового запису: " + fullName + " тип: " + (isAdminMode ? "Адмін" : "Клієнт"));

        try {
            // Загальні перевірки
            if (fullName.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                AlertUtil.showError("Усі обов'язкові поля мають бути заповнені.");
                return;
            }
            if (!Validator.isValidName(fullName)) {
                AlertUtil.showError("Ім'я повинно містити щонайменше 3 символи.");
                return;
            }
            if (!Validator.isValidPhone(phone)) {
                AlertUtil.showError("Телефон повинен містити рівно 9 цифр.");
                return;
            }
            if (!Validator.isValidPassword(password)) {
                AlertUtil.showError("Пароль повинен містити щонайменше 4 символи.");
                return;
            }

            if (isAdminMode) {
                if (specialPassword.isEmpty()) {
                    AlertUtil.showError("Спеціальний пароль обов'язковий для реєстрації адміністратора.");
                    return;
                }

                if (!userService.isValidAdminPassword(specialPassword)) {
                    AlertUtil.showError("Невірний спеціальний пароль для реєстрації адміністратора.");
                    return;
                }

                if (!userService.isAdminPhoneUnique(phone)) {
                    AlertUtil.showError("Адміністратор з таким номером телефону вже існує.");
                    return;
                }

                boolean registered = userService.registerAdmin(fullName, phone, password);
                if (registered) {
                    Object user = userService.loginUser(fullName, password);
                    if (user != null && userService.isAdmin(user)) {
                        logger.info("Адміністратор зареєстрований та увійшов: " + fullName);
                        openMainView(event, user);
                    } else {
                        logger.warning("Не вдалося увійти після реєстрації адміна: " + fullName);
                        AlertUtil.showError("Щось пішло не так під час логіну адміністратора.");
                    }
                } else {
                    logger.warning("Реєстрація адміністратора не вдалася: " + fullName);
                    AlertUtil.showError("Не вдалося зареєструвати адміністратора.");
                }
            } else {
                if (!userService.isClientNameUnique(fullName)) {
                    AlertUtil.showError("Користувач з таким ім'ям вже існує.");
                    return;
                }
                if (!userService.isPhoneUnique(phone)) {
                    AlertUtil.showError("Користувач з таким номером телефону вже існує.");
                    return;
                }

                // Реєстрація клієнта
                boolean registered = userService.registerClient(fullName, phone, password);
                if (registered) {
                    Object user = userService.loginUser(fullName, password);
                    if (user != null && userService.isClient(user)) {
                        logger.info("Клієнт зареєстрований та увійшов: " + fullName);
                        openMainView(event, user);
                    } else {
                        logger.warning("Не вдалося увійти після реєстрації: " + fullName);
                        AlertUtil.showError("Щось пішло не так під час логіну.");
                    }
                } else {
                    logger.warning("Реєстрація не вдалася. Клієнт уже існує: " + fullName);
                    AlertUtil.showError("Клієнт з таким телефоном вже існує.");
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Критична помилка під час створення облікового запису: " + fullName, e);
            EmailUtil.sendCriticalError("Критична помилка під час реєстрації", e);
            AlertUtil.showError("Сталася критична помилка. Адміністратор повідомлений.");
        }
    }


    @FXML
    void handleLogin(ActionEvent event){
        String fullName = txtUsername.getText();
        String password = txtPassword.getText().trim();

        logger.info("Спроба входу користувача: " + fullName + " режим: " + (isAdminMode ? "Адмін" : "Клієнт"));

        if (fullName.isEmpty() || password.isEmpty()) {
            AlertUtil.showError("Введіть логін та пароль.");
            return;
        }

        if (!Validator.isValidName(fullName)) {
            AlertUtil.showError("Ім’я повинно містити щонайменше 3 символи.");
            return;
        }

        if (!Validator.isValidPassword(password)) {
            AlertUtil.showError("Пароль повинен містити щонайменше 4 символи.");
            return;
        }

        try {
            Object user = userService.loginUser(fullName, password);
            if (user != null) {
                if (isAdminMode && user instanceof Client) {
                    AlertUtil.showError("Цей обліковий запис не є адміністратором.");
                    return;
                }
                if (!isAdminMode && user instanceof Admin) {
                    AlertUtil.showError("Цей обліковий запис не є клієнтом.");
                    return;
                }

                logger.info("Успішний вхід: " + fullName + " тип: " +
                        (user instanceof Admin ? "Адмін" : "Клієнт"));
                openMainView(event, user);
            } else {
                logger.warning("Невдалий вхід: " + fullName);
                AlertUtil.showError("Невірний ПІБ або пароль.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Критична помилка під час входу: " + fullName, e);
            EmailUtil.sendCriticalError("Критична помилка під час входу", e);
            AlertUtil.showError("Виникла критична помилка. Адміністратору надіслано повідомлення.");
        }
    }

    private void openMainView(ActionEvent event,  Object user) throws IOException {
        String fxmlFile;
        String title;

        if (user instanceof Admin) {
            fxmlFile = "/tourist_vouchers/v17_tourist_vouchers/admin_view.fxml";
            title = "Туристичні путівки - Адміністратор";
        } else {
            fxmlFile = "/tourist_vouchers/v17_tourist_vouchers/user_view.fxml";
            title = "Туристичні путівки - Клієнт";
        }

        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
        Parent root = loader.load();

        if (user instanceof Admin) {
            setupAdminController(loader, (Admin) user);
        } else {
            setupClientController(loader, (Client) user);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void setupClientController(FXMLLoader loader, Client client) {
        try {
            Object controller = loader.getController();

            if (controller instanceof UserController userController) {
                userController.setCurrentClient(client);
            }

            logger.info("Контролер клієнта успішно налаштований для: " + client.getFullName());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Не вдалося налаштувати контролер клієнта", e);
        }
    }


    private void setupAdminController(FXMLLoader loader, Admin admin) {
        try {
            Object controller = loader.getController();

            if (controller instanceof AdminController adminController) {
                adminController.setCurrentAdmin(admin);
            }

            logger.info("Контролер адміна успішно налаштований для: " + admin.getFullName());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Не вдалося налаштувати контролер адміна", e);
        }
    }

    @FXML
    void handleInfoApp(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        WindowUtil.switchScene(stage, "/tourist_vouchers/v17_tourist_vouchers/info_view.fxml");
    }
}
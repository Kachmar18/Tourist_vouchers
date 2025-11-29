package tourist_vouchers.v17_tourist_vouchers.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tourist_vouchers.v17_tourist_vouchers.model.Admin;
import tourist_vouchers.v17_tourist_vouchers.model.Client;

import java.io.IOException;
import java.util.logging.Logger;

public class WindowUtil {
    private static final Logger logger = LogUtil.getLogger();
    public static final String NAME = "Туристична агенція";
    public static final String ICON = "/tourist_vouchers/v17_tourist_vouchers/Icons/tour-guide.png";

    public static final String USER_ICON = "/tourist_vouchers/v17_tourist_vouchers/Icons/icon_user.png";
    public static final String ADMIN_ICON = "/tourist_vouchers/v17_tourist_vouchers/Icons/icon_admin.png";

    public static void openNewWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowUtil.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showError("Не вдалося відкрити вікно: " + title);
        }
    }

    public static void switchScene(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowUtil.class.getResource(fxmlPath));
            Parent root = loader.load();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showError("Не вдалося завантажити вікно.");
        }
    }

    public static void closeWindow(javafx.scene.Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public static void showClientWelcomeMessage(Client client) {
        if (client != null) {
            String fullName = client.getFullName();

            String welcomeMessage = "Ласкаво просимо, " + fullName + "!\n" +
                    "Бажаємо приємного вибору туру!";

            AlertUtil.showInfo("Вітаємо!", welcomeMessage);
            logger.info("Показано вітальне повідомлення для клієнта: " + fullName);
        }
    }

    public static void showAdminWelcomeMessage(Admin admin) {
        if (admin != null) {
            String fullName = admin.getFullName();

            String welcomeMessage = "Ласкаво просимо, Адміністратор " + fullName + "!\n" +
                    "Готові до управління турами!";

            AlertUtil.showInfo("Вітаємо!", welcomeMessage);
            logger.info("Показано вітальне повідомлення для адміна: " + fullName);
        }
    }
}

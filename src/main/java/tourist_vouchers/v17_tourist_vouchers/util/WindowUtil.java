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
//    public static final int WINDOW_WIDTH = 1400;
//    public static final int WINDOW_HEIGHT = 750;

//    public static final String NAME = "АвтоПро";
//    public static final String ICON = "/autoschool/autoschool_javafx/Icons/Icon2.png";

    public static final String USER_ICON = "/tourist_vouchers/v17_tourist_vouchers/Icons/icon_user.png";
    public static final String ADMIN_ICON = "/tourist_vouchers/v17_tourist_vouchers/Icons/icon_admin.png";

//    public static final String UKRAINE_FLAG_ICON = "/autoschool/autoschool_javafx/Icons/UkraineFlag_icon.png";
//    public static final String ENGLAND_FLAG_ICON =  "/autoschool/autoschool_javafx/Icons/EnglandFlag_icon.png";
//
//    public static final String EMAIL_FORMAT = "[a-zA-Z0-9]+@[a-zA-Z.-]+\\.[a-zA-Z]{2,}";
//    public static final String PHONE_FORMAT = "\\+380\\d{9}";

//    public static final String LeftICON =  "/autoschool/autoschool_javafx/Icons/Icons_Record/turn_arrow_icon.png";
//    public static final String EDIT_RECORD_ICON =  "/autoschool/autoschool_javafx/Icons/Icons_Record/editRecord_icon.png";
//    public static final String DONE_RECORD_ICON =  "/autoschool/autoschool_javafx/Icons/Icons_Record/done_icon.png";
//    public static final String DELETE_RECORD_ICON =  "/autoschool/autoschool_javafx/Icons/Icons_Record/deleteRecord_icon.png";

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

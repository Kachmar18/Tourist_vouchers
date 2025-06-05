package tourist_vouchers.v17_tourist_vouchers.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.transformation.FilteredList;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import tourist_vouchers.v17_tourist_vouchers.MainApp;
import tourist_vouchers.v17_tourist_vouchers.model.*;
import tourist_vouchers.v17_tourist_vouchers.services.ClientChoiceService;
import tourist_vouchers.v17_tourist_vouchers.services.TourPackageService;
import tourist_vouchers.v17_tourist_vouchers.util.*;

public class MainController {
    public MenuItem menuItemAddTour, menuItemEditTour, menuExit, menuItemDeleteTour;
    public VBox rootPane;
    @FXML private ComboBox<String> cmbDestination;
    @FXML private ComboBox<FoodType> cmbFoodType;
    @FXML private ComboBox<TourType> cmbTourType;
    @FXML private ComboBox<TransportType> cmbTransportType;
    @FXML private TextField txtDays, txtPrice, txtKeyWord;
    @FXML private TableView<TourPackage> tblTours;
    @FXML private TableColumn<TourPackage, String> priceCol, titleCol, tour_typeCol, transport_typeCol, daysCol, destinationCol, food_typeCol;

    private static final Logger logger = LogUtil.getLogger();
    private final TourPackageService tourService = new TourPackageService(); // Сервіс для роботи з турами
    private final ObservableList<TourPackage> toursData = FXCollections.observableArrayList();
    private FilteredList<TourPackage> filteredTours;
    private final ClientChoiceService clientChoiceService = new ClientChoiceService();
    private ClientChoice currentClient;

    public void setCurrentClient(ClientChoice client) {
        this.currentClient = client;
        logger.info("Поточний клієнт встановлений: ID=" + client.getId_client());
    }

    @FXML
    private void initialize() {
        logger.info("Ініціалізація MainController");
        if (!DBConnection.isDatabaseConnected()) {
            AlertUtil.showError("Помилка підключення", "Не вдалося підключитися до бази даних.\nПрограма буде закрита.");
            logger.severe("Не вдалося підключитися до бази даних");
            handleMenuExit();
        }

        setupComboBoxes();
        setupTableColumns();
        loadTours();
        loadDestinations();

        txtKeyWord.textProperty().addListener((obs, oldVal, newVal) -> onSearchByKeyword());
    }

    private void setupComboBoxes() {
        cmbFoodType.setItems(FXCollections.observableArrayList(FoodType.values()));
        cmbTourType.setItems(FXCollections.observableArrayList(TourType.values()));
        cmbTransportType.setItems(FXCollections.observableArrayList(TransportType.values()));
    }

    private void setupTableColumns() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("days"));
        transport_typeCol.setCellValueFactory(cellData -> cellData.getValue().getTransport().displayNameProperty());
        food_typeCol.setCellValueFactory(cellData -> cellData.getValue().getFoodType().displayNameProperty());
        tour_typeCol.setCellValueFactory(cellData -> cellData.getValue().getTourType().displayNameProperty());
    }

    private void loadTours() {
        logger.info("Завантаження всіх турів");
        List<TourPackage> tours = tourService.getAllTours();
        toursData.setAll(tours);

        if (filteredTours == null) {
            filteredTours = new FilteredList<>(toursData, p -> true);
            tblTours.setItems(filteredTours);
        }
    }

    private void loadDestinations() {
        logger.info("Завантаження напрямків");
        List<String> destinations = tourService.getAllDestinations();
        cmbDestination.setItems(FXCollections.observableArrayList(destinations));
        cmbDestination.getItems().addFirst(""); // Пустий рядок для відображення всіх пунктів
    }

    @FXML
    private void handleApplyFilter() {
        logger.info("Застосування фільтрів");

        try {
            Integer days = txtDays.getText().isEmpty() ? null : Integer.parseInt(txtDays.getText());
            Double price = txtPrice.getText().isEmpty() ? null : Double.parseDouble(txtPrice.getText());

            List<TourPackage> filtered = tourService.filterTours(
                    cmbDestination.getValue(),
                    price,
                    days,
                    cmbTourType.getValue(),
                    cmbFoodType.getValue(),
                    cmbTransportType.getValue()
            );
            toursData.setAll(filtered);
            logger.info("Фільтрацію завершено. Знайдено: " + filtered.size());
        } catch (NumberFormatException e) {
            logger.warning("Невірні дані для фільтрації: " + e.getMessage());
            AlertUtil.showError("Невірні дані", "Ціна або кількість днів мають бути числами.");
        }
    }

    @FXML
    private void handleResetFilter() {
        logger.info("Скидання фільтрів");
        cmbDestination.getSelectionModel().clearSelection();
        cmbFoodType.getSelectionModel().clearSelection();
        cmbTourType.getSelectionModel().clearSelection();
        cmbTransportType.getSelectionModel().clearSelection();
        txtDays.clear();
        txtPrice.clear();
        loadTours();
    }

    @FXML
    private void onSearchByKeyword() {
        String keyword = txtKeyWord.getText().toLowerCase();
        logger.info("Пошук за ключовим словом: " + keyword);

        filteredTours.setPredicate(tour -> {
            if (keyword == null || keyword.isEmpty()) return true;

            return tour.getTitle().toLowerCase().contains(keyword)
                    || tour.getDestination().toLowerCase().contains(keyword)
                    || String.valueOf(tour.getPrice()).contains(keyword)
                    || String.valueOf(tour.getDays()).contains(keyword)
                    || tour.getTransport().name().toLowerCase().contains(keyword)
                    || tour.getFoodType().name().toLowerCase().contains(keyword)
                    || tour.getTourType().name().toLowerCase().contains(keyword);
        });
    }

    @FXML
    public void handleMenuItemAddTour() {
        logger.info("Відкриття вікна додавання туру");
        WindowUtil.openNewWindow("/tourist_vouchers/v17_tourist_vouchers/edit_tour.fxml", "Додати тур");
        loadTours();
    }

    @FXML
    public void handleMenuItemEditTour() {
        TourPackage selectedTour = tblTours.getSelectionModel().getSelectedItem();
        if (selectedTour == null) {
            AlertUtil.showError("Помилка", "Спочатку виберіть тур для редагування.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/tourist_vouchers/v17_tourist_vouchers/edit_tour.fxml"));
            Parent root = loader.load();
            EditTourController controller = loader.getController();
            controller.setTourForEdit(selectedTour);

            Stage stage = new Stage();
            stage.setTitle("Редагувати тур");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            logger.info("Редаговано тур: ID=" + selectedTour.getId());
            loadTours();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Помилка при відкритті редактора туру", e);
            EmailUtil.sendCriticalError("Помилка при редагуванні туру", e);
            AlertUtil.showError("Помилка", "Не вдалося відкрити вікно редагування туру.");
        }
    }

    @FXML
    public void handleMenuItemDeleteTour() {
        TourPackage selectedTour = tblTours.getSelectionModel().getSelectedItem();
        if (selectedTour == null) {
            AlertUtil.showError("Помилка", "Спочатку виберіть тур для видалення.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Підтвердження");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Ви впевнені, що хочете видалити тур \"" + selectedTour.getTitle() + "\"?");

        logger.info("Користувач намагається видалити тур ID: " + selectedTour.getId());
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    tourService.deleteTour(selectedTour.getId());
                    logger.info("Тур видалено: ID=" + selectedTour.getId());
                    loadTours();
                    AlertUtil.showInfo("Успіх", "Тур видалено успішно.");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Не вдалося видалити тур", e);
                    EmailUtil.sendCriticalError("Помилка видалення туру", e);
                    AlertUtil.showError("Помилка", "Не вдалося видалити тур.");
                }
            }
        });
    }

    @FXML
    public void handleMenuExit() {
        logger.info("Вихід з програми");
        WindowUtil.switchScene((Stage) tblTours.getScene().getWindow(), "/tourist_vouchers/v17_tourist_vouchers/login_view.fxml");
    }

    @FXML
    public void handleMenuInfo() {
        logger.info("Перехід до сторінки 'Про програму'");
        WindowUtil.switchScene((Stage) tblTours.getScene().getWindow(), "/tourist_vouchers/v17_tourist_vouchers/info_view.fxml");
    }

    @FXML
    public void handleBtnBooking() {
        TourPackage selectedTour = tblTours.getSelectionModel().getSelectedItem();
        if (selectedTour == null) {
            AlertUtil.showError("Помилка", "Спочатку виберіть тур для бронювання.");
            return;
        }

        logger.info("Користувач намагається забронювати тур (ID: " + selectedTour.getId() + ")");
        try {
            boolean success = clientChoiceService.bookTour(currentClient.getId_client(), selectedTour.getId());
            if (success) {
                logger.info("Тур успішно заброньовано користувачем ID: " + currentClient.getId_client());
                currentClient.setId_selectedTour(selectedTour.getId());
                AlertUtil.showInfo("Успіх", "Тур успішно заброньовано!");
            } else {
                logger.warning("Не вдалося забронювати тур користувачу ID: " + currentClient.getId_client());
                AlertUtil.showError("Помилка", "Не вдалося забронювати тур.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Критична помилка під час бронювання туру", e);
            EmailUtil.sendCriticalError("Критична помилка при бронюванні", e);
            AlertUtil.showError("Критична помилка", "Помилка при бронюванні туру. Повідомлення надіслано адміністратору.");
        }
    }

    @FXML
    public void handleBtnSelectedTour() {
        int selectedTourId = currentClient.getId_selectedTour();
        if (selectedTourId == 0) {
            AlertUtil.showInfo("Інформація", "Ви ще не забронювали жодного туру.");
            return;
        }
        logger.info("Користувач ID " + currentClient.getId_client() + " скасовує бронювання туру.");

        TourPackage tour = tourService.getTourById(selectedTourId);
        if (tour != null) {
            String info = "Назва: " + tour.getTitle() + "\n"
                    + "Місце призначення: " + tour.getDestination() + "\n"
                    + "Ціна: " + tour.getPrice() + "\n"
                    + "Дні: " + tour.getDays() + "\n"
                    + "Транспорт: " + tour.getTransport().getDisplayName() + "\n"
                    + "Харчування: " + tour.getFoodType().getDisplayName() + "\n"
                    + "Тип туру: " + tour.getTourType().getDisplayName();

            AlertUtil.showInfo("Обраний тур", info);
        } else {
            AlertUtil.showError("Помилка", "Не вдалося знайти тур у базі.");
        }
    }


    @FXML
    public void handleBtnClearSelectedTour() {
        if (currentClient.getId_selectedTour() == 0) {
            AlertUtil.showInfo("Інформація", "У вас вже немає обраного туру.");
            return;
        }

        boolean success = clientChoiceService.clearBookedTour(currentClient.getId_client());
        if (success) {
            logger.info("Тур скасовано: clientId=" + currentClient.getId_client());
            currentClient.setId_selectedTour(0);
            AlertUtil.showInfo("Успіх", "Ваш обраний тур було скасовано.");
        } else {
            logger.warning("Не вдалося скасувати тур для clientId=" + currentClient.getId_client());
            AlertUtil.showError("Помилка", "Не вдалося скасувати тур.");
        }
    }

    @FXML
    private void handleExportReport() {
        Stage stage = (Stage) tblTours.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Зберегти звіт");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel файли", "*.xlsx")
        );
        fileChooser.setInitialFileName("clients_with_tours.xlsx");

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                ClientChoiceService service = new ClientChoiceService();
                List<ClientChoice> clients = service.getClientsWithTours(); // цей метод має повертати лише тих, у кого tour_id != null

                if (clients.isEmpty()) {
                    AlertUtil.showInfo("Немає даних", "Жоден клієнт ще не обрав тур.");
                    return;
                }

                ReportUtil.exportClientsToExcel(clients, file.getAbsolutePath());
                AlertUtil.showInfo("Успіх", "Звіт збережено у файл:\n" + file.getAbsolutePath());
            } catch (Exception e) {
                LogUtil.getLogger().severe("Помилка при збереженні звіту: " + e.getMessage());
                AlertUtil.showError("Помилка", "Не вдалося зберегти звіт.");
            }
        }
    }



}
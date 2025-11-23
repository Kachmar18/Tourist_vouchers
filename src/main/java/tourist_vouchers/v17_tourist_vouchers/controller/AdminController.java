package tourist_vouchers.v17_tourist_vouchers.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tourist_vouchers.v17_tourist_vouchers.MainApp;
import tourist_vouchers.v17_tourist_vouchers.model.*;
import tourist_vouchers.v17_tourist_vouchers.services.ClientChoiceService;
import tourist_vouchers.v17_tourist_vouchers.services.TourPackageService;
import tourist_vouchers.v17_tourist_vouchers.services.UserService;
import tourist_vouchers.v17_tourist_vouchers.util.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminController {
    public MenuItem menuItemAddTour, menuItemEditTour, menuExit, menuItemDeleteTour;
    public VBox rootPane;
    @FXML
    public ComboBox<Client> cmbClients;
    @FXML
    private ComboBox<String> cmbDestination;
    @FXML private ComboBox<FoodType> cmbFoodType;
    @FXML private ComboBox<TourType> cmbTourType;
    @FXML private ComboBox<TransportType> cmbTransportType;
    @FXML private TextField txtDays, txtPrice, txtKeyWord;
    @FXML private TableView<TourPackage> tblTours;
    @FXML private TableColumn<TourPackage, String> priceCol, titleCol, tour_typeCol, transport_typeCol, daysCol, destinationCol, food_typeCol;

    private static final Logger logger = LogUtil.getLogger();
    private final TourPackageService tourService = new TourPackageService(); // Сервіс для роботи з турами
    private final ObservableList<TourPackage> toursData = FXCollections.observableArrayList();
    private final UserService userService = new UserService();
    private final ClientChoiceService clientChoiceService = new ClientChoiceService();
    private FilteredList<TourPackage> filteredTours;
    private Client selectedClient;

    private Admin currentAdmin;

    public void setCurrentAdmin(Admin admin) {
        this.currentAdmin = admin;
        initialize();
    }

    @FXML
    private void initialize() {
        if (currentAdmin != null) {
            System.out.println("Адмін увійшов: " + currentAdmin.getFullName());
        }
        logger.info("Ініціалізація AdminController");
        if (!DBConnection.isDatabaseConnected()) {
            AlertUtil.showError("Не вдалося підключитися до бази даних.\nПрограма буде закрита.");
            logger.severe("Не вдалося підключитися до бази даних");
            handleMenuExit();
        }

        setupComboBoxes();
        setupTableColumns();
        loadTours();
        loadDestinations();
        loadClients();

        txtKeyWord.textProperty().addListener((_, _, _) -> onSearchByKeyword());
    }

    private void setupComboBoxes() {
        cmbFoodType.setItems(FXCollections.observableArrayList(FoodType.values()));
        cmbTourType.setItems(FXCollections.observableArrayList(TourType.values()));
        cmbTransportType.setItems(FXCollections.observableArrayList(TransportType.values()));

        cmbClients.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                } else {
                    setText(client.getFullName() + " (" + client.getPhone() + ")");
                }
            }
        });

        cmbClients.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText("Оберіть клієнта");
                } else {
                    setText(client.getFullName() + " (" + client.getPhone() + ")");
                }
            }
        });
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

    private void loadClients() {
        logger.info("Завантаження списку клієнтів");
        try {
            List<Client> clients = userService.getAllClients();
            ObservableList<Client> clientList = FXCollections.observableArrayList(clients);
            cmbClients.setItems(clientList);
            logger.info("Завантажено клієнтів: " + clients.size());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Помилка при завантаженні клієнтів", e);
            AlertUtil.showError("Не вдалося завантажити список клієнтів.");
        }
    }

    private void loadTours() {
        logger.info("Завантаження всіх турів");
        List<TourPackage> tours = tourService.getAllTours();
        toursData.setAll(tours);

        if (filteredTours == null) {
            filteredTours = new FilteredList<>(toursData, _ -> true);
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
            AlertUtil.showError("Ціна або кількість днів мають бути числами.");
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
            if (keyword.isEmpty()) return true;

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
            AlertUtil.showError("Спочатку виберіть тур для редагування.");
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
            AlertUtil.showError("Не вдалося відкрити вікно редагування туру.");
        }
    }

    @FXML
    public void handleMenuItemDeleteTour() {
        TourPackage selectedTour = tblTours.getSelectionModel().getSelectedItem();
        if (selectedTour == null) {
            AlertUtil.showError("Спочатку виберіть тур для видалення.");
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
                    AlertUtil.showInfo("Тур видалено успішно.");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Не вдалося видалити тур", e);
                    EmailUtil.sendCriticalError("Помилка видалення туру", e);
                    AlertUtil.showError("Не вдалося видалити тур.");
                }
            }
        });
    }

    @FXML
    public void handleMenuExit() {
        logger.info("Вихід в головне меню");
        try {
            if (currentAdmin != null) {
                userService.logoutUser(currentAdmin);
            logger.info("Клієнт вийшов і деактивований: " + currentAdmin.getFullName());
            }

            Stage stage = (Stage) tblTours.getScene().getWindow();
            WindowUtil.switchScene(stage, "/tourist_vouchers/v17_tourist_vouchers/login_view.fxml");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Помилка при виході з програми", e);
        }
    }

    @FXML
    public void handleBtnBooking() {
        if (selectedClient == null) {
            AlertUtil.showError("Спочатку оберіть клієнта.");
            return;
        }

        TourPackage selectedTour = tblTours.getSelectionModel().getSelectedItem();
        if (selectedTour == null) {
            AlertUtil.showError("Спочатку виберіть тур для бронювання.");
            return;
        }

        logger.info("Адмін намагається забронювати тур (ID: " + selectedTour.getId() +
                ") для клієнта: " + selectedClient.getFullName());

        try {
            boolean success = clientChoiceService.bookTour(selectedClient.getIdClient(), selectedTour.getId());
            if (success) {
                logger.info("Тур успішно заброньовано адміном для клієнта ID: " + selectedClient.getIdClient());
                AlertUtil.showInfo("Успіх", "Тур успішно заброньовано для клієнта " + selectedClient.getFullName() + "!");
            } else {
                logger.warning("Не вдалося забронювати тур для клієнта ID: " + selectedClient.getIdClient());
                AlertUtil.showError("Не вдалося забронювати тур.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Критична помилка під час бронювання туру адміном", e);
            EmailUtil.sendCriticalError("Критична помилка при бронюванні адміном", e);
            AlertUtil.showCriticalError("Помилка при бронюванні туру. Повідомлення надіслано адміністратору.");
        }
    }

    @FXML
    public void handleBtnSelectedTour() {
        if (selectedClient == null) {
            AlertUtil.showError("Спочатку оберіть клієнта.");
            return;
        }

        try {
            TourPackage bookedTour = clientChoiceService.getBookedTour(selectedClient.getIdClient());
            if (bookedTour == null) {
                AlertUtil.showInfo("Інформація", "Клієнт " + selectedClient.getFullName() + " ще не забронював жодного туру.");
                return;
            }

            String info = "Клієнт: " + selectedClient.getFullName() + "\n"
                    + "Телефон: " + selectedClient.getPhone() + "\n\n"
                    + "Заброньований тур:\n"
                    + "Назва: " + bookedTour.getTitle() + "\n"
                    + "Місце призначення: " + bookedTour.getDestination() + "\n"
                    + "Ціна: " + bookedTour.getPrice() + " грн\n"
                    + "Тривалість: " + bookedTour.getDays() + " днів\n"
                    + "Транспорт: " + (bookedTour.getTransport() != null ? bookedTour.getTransport().getDisplayName() : "") + "\n"
                    + "Харчування: " + (bookedTour.getFoodType() != null ? bookedTour.getFoodType().getDisplayName() : "") + "\n"
                    + "Тип туру: " + (bookedTour.getTourType() != null ? bookedTour.getTourType().getDisplayName() : "");

            AlertUtil.showInfo("Заброньований тур клієнта", info);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Помилка при отриманні інформації про заброньований тур клієнта", e);
            AlertUtil.showError("Не вдалося отримати інформацію про заброньований тур.");
        }
    }


    @FXML
    public void handleBtnClearSelectedTour() {
        if (selectedClient == null) {
            AlertUtil.showError("Спочатку оберіть клієнта.");
            return;
        }

        try {
            TourPackage bookedTour = clientChoiceService.getBookedTour(selectedClient.getIdClient());
            if (bookedTour == null) {
                AlertUtil.showInfo("Інформація", "У клієнта " + selectedClient.getFullName() + " немає заброньованих турів.");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Підтвердження скасування");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Ви впевнені, що хочете скасувати бронювання туру '" +
                    bookedTour.getTitle() + "' для клієнта " + selectedClient.getFullName() + "?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean success = clientChoiceService.clearBookedTour(selectedClient.getIdClient());
                    if (success) {
                        logger.info("Бронювання скасовано адміном для клієнта: " + selectedClient.getFullName());
                        AlertUtil.showInfo("Успіх", "Бронювання туру '" + bookedTour.getTitle() + "' для клієнта " +
                                selectedClient.getFullName() + " скасовано.");
                    } else {
                        logger.warning("Не вдалося скасувати бронювання для клієнта: " + selectedClient.getFullName());
                        AlertUtil.showError("Не вдалося скасувати бронювання туру.");
                    }
                }
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Помилка при скасуванні бронювання туру адміном", e);
            AlertUtil.showError("Сталася помилка при скасуванні бронювання.");
        }
    }

    @FXML
    private void handleClientSelection() {
        selectedClient = cmbClients.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            logger.info("Обрано клієнта: " + selectedClient.getFullName());
        } else {
            logger.info("Клієнта не обрано");
        }
    }

    @FXML
    private void handleExportReport() {
        logger.info("Експорт звіту про клієнтів з турами");

        try {
            List<ClientChoice> clientsWithTours = clientChoiceService.getClientsWithTours();

            if (clientsWithTours.isEmpty()) {
                AlertUtil.showInfo("Інформація", "Немає клієнтів з заброньованими турами для експорту.");
                return;
            }

            // Створюємо діалог вибору файлу
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Зберегти звіт як CSV");
            fileChooser.getExtensionFilters().add(
                    new javafx.stage.FileChooser.ExtensionFilter("CSV файли (*.csv)", "*.csv")
            );
            fileChooser.setInitialFileName("clients_with_tours_report.csv");

            Stage stage = (Stage) tblTours.getScene().getWindow();
            java.io.File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                String filePath = file.getAbsolutePath();

                ReportUtil.exportClientsToCSV(clientsWithTours, filePath);

                logger.info("Звіт успішно експортовано: " + filePath);
                AlertUtil.showInfo("Успіх", "Звіт успішно експортовано у файл: " + file.getName());
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Помилка при експорті звіту", e);
            AlertUtil.showError("Не вдалося експортувати звіт. Спробуйте ще раз.");
        }
    }
}

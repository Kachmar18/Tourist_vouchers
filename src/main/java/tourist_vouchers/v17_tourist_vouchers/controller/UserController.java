package tourist_vouchers.v17_tourist_vouchers.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tourist_vouchers.v17_tourist_vouchers.model.*;
import tourist_vouchers.v17_tourist_vouchers.services.ClientChoiceService;
import tourist_vouchers.v17_tourist_vouchers.services.TourPackageService;
import tourist_vouchers.v17_tourist_vouchers.services.UserService;
import tourist_vouchers.v17_tourist_vouchers.util.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController {
    public MenuItem menuExit;
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
    private final UserService userService = new UserService();

    private Client currentClient;

    public void setCurrentClient(Client client) {
        this.currentClient = client;
        logger.info("Поточний клієнт встановлений: " + client.getFullName());
        WindowUtil.showClientWelcomeMessage(currentClient);
    }
    @FXML
    private void initialize() {
        logger.info("Ініціалізація UserController");
        if (!DBConnection.isDatabaseConnected()) {
            AlertUtil.showError("Не вдалося підключитися до бази даних.\nПрограма буде закрита.");
            logger.severe("Не вдалося підключитися до бази даних");
            handleMenuExit();
        }

        setupComboBoxes();
        setupTableColumns();
        loadTours();
        loadDestinations();

        txtKeyWord.textProperty().addListener((_, _, _) -> onSearchByKeyword());
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
        transport_typeCol.setCellValueFactory(cellData -> {
            TransportType transport = cellData.getValue().getTransport();
            return new javafx.beans.property.SimpleStringProperty(transport != null ? transport.getDisplayName() : "");
        });

        food_typeCol.setCellValueFactory(cellData -> {
            FoodType foodType = cellData.getValue().getFoodType();
            return new javafx.beans.property.SimpleStringProperty(foodType != null ? foodType.getDisplayName() : "");
        });

        tour_typeCol.setCellValueFactory(cellData -> {
            TourType tourType = cellData.getValue().getTourType();
            return new javafx.beans.property.SimpleStringProperty(tourType != null ? tourType.getDisplayName() : "");
        });
    }

    private void loadTours() {
        logger.info("Завантаження всіх турів");
        try {
            List<TourPackage> tours = tourService.getAllTours();
            toursData.setAll(tours);

            if (filteredTours == null) {
                filteredTours = new FilteredList<>(toursData, _ -> true);
                tblTours.setItems(filteredTours);
            } else {
                filteredTours.setPredicate(_ -> true); // Скидаємо фільтр
            }

            logger.info("Завантажено турів: " + tours.size());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Помилка при завантаженні турів", e);
            AlertUtil.showError("Не вдалося завантажити тури з бази даних.");
        }
    }

    private void loadDestinations() {
        logger.info("Завантаження напрямків");
        try {
            List<String> destinations = tourService.getAllDestinations();
            ObservableList<String> destinationList = FXCollections.observableArrayList(destinations);
            destinationList.addFirst(""); // Пустий рядок для відображення всіх пунктів
            cmbDestination.setItems(destinationList);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Помилка при завантаженні напрямків", e);
        }
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
    public void handleBtnSelectedTour() {
        if (currentClient == null) {
            AlertUtil.showError("Клієнт не авторизований.");
            return;
        }

        try {
            // Отримуємо заброньований тур з бази даних
            TourPackage bookedTour = clientChoiceService.getBookedTour(currentClient.getIdClient());
            if (bookedTour == null) {
                AlertUtil.showInfo("Інформація", "Ви ще не забронювали жодного туру.");
                return;
            }

            String info = "Назва: " + bookedTour.getTitle() + "\n"
                    + "Місце призначення: " + bookedTour.getDestination() + "\n"
                    + "Ціна: " + bookedTour.getPrice() + " грн\n"
                    + "Тривалість: " + bookedTour.getDays() + " днів\n"
                    + "Транспорт: " + (bookedTour.getTransport() != null ? bookedTour.getTransport().getDisplayName() : "") + "\n"
                    + "Харчування: " + (bookedTour.getFoodType() != null ? bookedTour.getFoodType().getDisplayName() : "") + "\n"
                    + "Тип туру: " + (bookedTour.getTourType() != null ? bookedTour.getTourType().getDisplayName() : "");

            AlertUtil.showInfo("Ваш заброньований тур", info);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Помилка при отриманні інформації про заброньований тур", e);
            AlertUtil.showError("Не вдалося отримати інформацію про заброньований тур.");
        }
    }


    @FXML
    public void handleBtnBooking() {
        TourPackage selectedTour = tblTours.getSelectionModel().getSelectedItem();
        if (selectedTour == null) {
            AlertUtil.showError("Спочатку виберіть тур для бронювання.");
            return;
        }

        logger.info("Користувач намагається забронювати тур (ID: " + selectedTour.getId() + ")");
        try {
            boolean success = clientChoiceService.bookTour(currentClient.getIdClient(), selectedTour.getId());
            if (success) {
                logger.info("Тур успішно заброньовано користувачем ID: " + currentClient.getIdClient());
                AlertUtil.showInfo("Успіх", "Тур успішно заброньовано!");
            } else {
                logger.warning("Не вдалося забронювати тур користувачу ID: " + currentClient.getIdClient());
                AlertUtil.showError("Не вдалося забронювати тур.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Критична помилка під час бронювання туру", e);
            EmailUtil.sendCriticalError("Критична помилка при бронюванні", e);
            AlertUtil.showCriticalError("Помилка при бронюванні туру. Повідомлення надіслано адміністратору.");
        }
    }

    @FXML
    public void handleBtnClearSelectedTour() {
        if (currentClient == null) {
            AlertUtil.showError("Клієнт не авторизований.");
            return;
        }

        try {
            TourPackage bookedTour = clientChoiceService.getBookedTour(currentClient.getIdClient());
            if (bookedTour == null) {
                AlertUtil.showInfo("Інформація", "У вас немає заброньованих турів.");
                return;
            }

            boolean success = clientChoiceService.clearBookedTour(currentClient.getIdClient());
            if (success) {
                logger.info("Тур скасовано: clientId=" + currentClient.getIdClient());
                AlertUtil.showInfo("Успіх", "Бронювання туру '" + bookedTour.getTitle() + "' скасовано.");
            } else {
                logger.warning("Не вдалося скасувати тур для clientId=" + currentClient.getIdClient());
                AlertUtil.showError("Не вдалося скасувати бронювання туру.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Помилка при скасуванні бронювання туру", e);
            AlertUtil.showError("Сталася помилка при скасуванні бронювання.");
        }
    }


    @FXML
    public void handleMenuExit() {
        logger.info("Вихід в головне меню");
        try {

            if (currentClient != null) {
                userService.logoutUser(currentClient);
                logger.info("Клієнт вийшов і деактивований: " + currentClient.getFullName());
            }

            Stage stage = (Stage) tblTours.getScene().getWindow();
            WindowUtil.switchScene(stage, "/tourist_vouchers/v17_tourist_vouchers/login_view.fxml");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Помилка при виході з програми", e);
        }
    }
}
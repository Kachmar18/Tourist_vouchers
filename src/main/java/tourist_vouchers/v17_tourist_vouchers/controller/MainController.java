package tourist_vouchers.v17_tourist_vouchers.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.transformation.FilteredList;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import tourist_vouchers.v17_tourist_vouchers.MainApp;
import tourist_vouchers.v17_tourist_vouchers.model.*;
import tourist_vouchers.v17_tourist_vouchers.services.ClientChoiceService;
import tourist_vouchers.v17_tourist_vouchers.services.TourPackageService;
import tourist_vouchers.v17_tourist_vouchers.util.AlertUtil;
import tourist_vouchers.v17_tourist_vouchers.util.WindowUtil;

public class MainController {
    public MenuItem menuItemAddTour, menuItemEditTour, menuExit, menuItemDeleteTour;
    @FXML private ComboBox<String> cmbDestination;
    @FXML private ComboBox<FoodType> cmbFoodType;
    @FXML private ComboBox<TourType> cmbTourType;
    @FXML private ComboBox<TransportType> cmbTransportType;
    @FXML private TextField txtDays, txtPrice, txtKeyWord;
    @FXML private TableView<TourPackage> tblTours;
    @FXML private TableColumn<TourPackage, String> priceCol, titleCol, tour_typeCol, transport_typeCol, daysCol, destinationCol, food_typeCol;

    private final TourPackageService tourService = new TourPackageService(); // Сервіс для роботи з турами
    private final ObservableList<TourPackage> toursData = FXCollections.observableArrayList();
    private FilteredList<TourPackage> filteredTours;
    private final ClientChoiceService clientChoiceService = new ClientChoiceService();
    private ClientChoice currentClient;

    public void setCurrentClient(ClientChoice client) {
        this.currentClient = client;
    }

    @FXML
    private void initialize() {
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
        List<TourPackage> tours = tourService.getAllTours();
        toursData.setAll(tours);

        if (filteredTours == null) {
            filteredTours = new FilteredList<>(toursData, p -> true);
            tblTours.setItems(filteredTours);
        }
    }

    private void loadDestinations() {
        List<String> destinations = tourService.getAllDestinations();
        cmbDestination.setItems(FXCollections.observableArrayList(destinations));
        cmbDestination.getItems().addFirst(""); // Пустий рядок для відображення всіх пунктів
    }

    @FXML
    private void handleApplyFilter() {
        String destination = cmbDestination.getValue();
        FoodType foodType = cmbFoodType.getValue();
        TourType tourType = cmbTourType.getValue();
        TransportType transportType = cmbTransportType.getValue();

        Integer days = null;
        Double price = null;

        try {
            if (!txtDays.getText().isEmpty()) days = Integer.parseInt(txtDays.getText());
            if (!txtPrice.getText().isEmpty()) price = Double.parseDouble(txtPrice.getText());
        } catch (NumberFormatException e) {
            AlertUtil.showError("Невірні дані", "Ціна або кількість днів мають бути числами.");
            return;
        }

        List<TourPackage> filtered = tourService.filterTours(destination, price, days, tourType, foodType, transportType);
        toursData.setAll(filtered);
    }

    @FXML
    private void handleResetFilter() {
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

            loadTours();
        } catch (IOException e) {
            e.printStackTrace();
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

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    tourService.deleteTour(selectedTour.getId());
                    loadTours();
                    AlertUtil.showInfo("Успіх", "Тур видалено успішно.");
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertUtil.showError("Помилка", "Не вдалося видалити тур.");
                }
            }
        });
    }

    @FXML
    public void handleMenuExit() {
        WindowUtil.switchScene((Stage) tblTours.getScene().getWindow(), "/tourist_vouchers/v17_tourist_vouchers/login_view.fxml");
    }

    @FXML
    public void handleMenuInfo() {
        WindowUtil.switchScene((Stage) tblTours.getScene().getWindow(), "/tourist_vouchers/v17_tourist_vouchers/info_view.fxml");
    }

    @FXML
    public void handleBtnBooking() {
        TourPackage selectedTour = tblTours.getSelectionModel().getSelectedItem();
        if (selectedTour == null) {
            AlertUtil.showError("Помилка", "Спочатку виберіть тур для бронювання.");
            return;
        }

        boolean success = clientChoiceService.bookTour(currentClient.getId_client(), selectedTour.getId());
        if (success) {
            AlertUtil.showInfo("Успіх", "Тур успішно заброньовано!");
            currentClient.setId_selectedTour(selectedTour.getId()); // оновлюємо локально
        } else {
            AlertUtil.showError("Помилка", "Не вдалося забронювати тур.");
        }
    }

    @FXML
    public void handleBtnSelectedTour() {
        int selectedTourId = currentClient.getId_selectedTour();
        if (selectedTourId == 0) {
            AlertUtil.showInfo("Інформація", "Ви ще не забронювали жодного туру.");
            return;
        }

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
            currentClient.setId_selectedTour(0); // оновлюємо локально
            AlertUtil.showInfo("Успіх", "Ваш обраний тур було скасовано.");
        } else {
            AlertUtil.showError("Помилка", "Не вдалося скасувати тур.");
        }
    }

}
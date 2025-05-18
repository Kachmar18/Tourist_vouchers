package tourist_vouchers.v17_tourist_vouchers.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

import javafx.scene.control.cell.PropertyValueFactory;
import tourist_vouchers.v17_tourist_vouchers.model.FoodType;
import tourist_vouchers.v17_tourist_vouchers.model.TourPackage;
import tourist_vouchers.v17_tourist_vouchers.model.TourType;
import tourist_vouchers.v17_tourist_vouchers.model.TransportType;
import tourist_vouchers.v17_tourist_vouchers.services.TourPackageService;

public class MainController {
    @FXML private TableView<TourPackage> tblTours;
    @FXML private ComboBox<String> cmbDestination;
    @FXML private ComboBox<FoodType> cmbFoodType;
    @FXML private ComboBox<TourType> cmbTourType;
    @FXML private ComboBox<TransportType> cmbTransportType;
    @FXML private TextField txtDays, txtPrice;
    @FXML private Button btnApplyFilter, btnResetFilter;
    @FXML private MenuItem menuViewTours;
    @FXML private TableColumn<TourPackage, String> priceCol, titleCol, tour_typeCol, transport_typeCol, daysCol, destinationCol, food_typeCol;


    private final TourPackageService tourService = new TourPackageService(); // Сервіс для роботи з турами
    private final ObservableList<TourPackage> toursData = FXCollections.observableArrayList();

    // Ініціалізація контролера
    @FXML
    private void initialize() {
        setupComboBoxes();
        setupTableColumns();
        loadTours();
        loadDestinations();
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
        tblTours.setItems(toursData);
    }

    private void loadDestinations() {
        List<String> destinations = tourService.getAllDestinations();
        cmbDestination.setItems(FXCollections.observableArrayList(destinations));

        cmbDestination.getItems().add(0, ""); // Пустий рядок для відображення всіх пунктів
    }

    // Обробники подій
    @FXML
    private void handleApplyFilter() {
        // Тут буде логіка фільтрації
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
}
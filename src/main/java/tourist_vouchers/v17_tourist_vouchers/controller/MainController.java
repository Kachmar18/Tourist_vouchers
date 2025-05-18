package tourist_vouchers.v17_tourist_vouchers.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tourist_vouchers.v17_tourist_vouchers.model.FoodType;
import tourist_vouchers.v17_tourist_vouchers.model.TourPackage;
import tourist_vouchers.v17_tourist_vouchers.model.TourType;
import tourist_vouchers.v17_tourist_vouchers.model.TransportType;
import tourist_vouchers.v17_tourist_vouchers.services.TourPackageService;


public class MainController {
    private TourPackageService tourService;     // Сервіс для роботи з турами

    @FXML private TableView<TourPackage> tblTours;
    @FXML private ComboBox<String> cmbDestination;
    @FXML private ComboBox<FoodType> cmbFoodType;
    @FXML private ComboBox<TourType> cmbTourType;
    @FXML private ComboBox<TransportType> cmbTransportType;
    @FXML private TextField txtDays;
    @FXML private TextField txtPrice;
    @FXML private Button btnApplyFilter;
    @FXML private Button btnResetFilter;
    @FXML private MenuItem menuViewTours;

    // Колекція для таблиці
    private ObservableList<TourPackage> toursData;

    // Ініціалізація контролера
    @FXML
    private void initialize() {
        // Тут буде ініціалізація сервісів, налаштування комбобоксів, таблиці тощо
    }

    // Обробники подій
    @FXML
    private void handleApplyFilter() {
        // Фільтрація турів за вказаними критеріями
    }

    @FXML
    private void handleResetFilter() {
        // Скидання фільтрів до початкового стану
    }

    @FXML
    private void handleViewTours() {
        // Перегляд усіх доступних турів
    }

    // Допоміжні методи
    private void setupTableColumns() {
        // Налаштування колонок таблиці tblTours
    }

    private void loadAllTours() {
        // Завантаження всіх турів з бази даних
    }

    private void initializeComboBoxes() {
        // Ініціалізація значень для комбобоксів
    }
}
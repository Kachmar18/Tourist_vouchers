package tourist_vouchers.v17_tourist_vouchers.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import tourist_vouchers.v17_tourist_vouchers.model.FoodType;
import tourist_vouchers.v17_tourist_vouchers.model.TourPackage;
import tourist_vouchers.v17_tourist_vouchers.model.TourType;
import tourist_vouchers.v17_tourist_vouchers.model.TransportType;
import tourist_vouchers.v17_tourist_vouchers.services.TourPackageService;
import tourist_vouchers.v17_tourist_vouchers.util.AlertUtil;
import tourist_vouchers.v17_tourist_vouchers.util.LogUtil;
import tourist_vouchers.v17_tourist_vouchers.util.Validator;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EditTourController {
    public TextField txtTitle, txtDestination, txtPrice, txtDays;
    public ComboBox<TransportType> cmbTransportType;
    public ComboBox<FoodType> cmbFoodType;
    public ComboBox<TourType> cmbTourType;
    public Button btnSave, btnCancel;

    private final TourPackageService tourService = new TourPackageService();
    private TourPackage tourForEdit;
    private static final Logger logger = LogUtil.getLogger();

    @FXML
    private void initialize() {
        cmbTransportType.setItems(FXCollections.observableArrayList(TransportType.values()));
        cmbFoodType.setItems(FXCollections.observableArrayList(FoodType.values()));
        cmbTourType.setItems(FXCollections.observableArrayList(TourType.values()));

        if (tourForEdit != null) {
            fillFieldsForEdit();
        }
    }

    public void setTourForEdit(TourPackage tour) {
        this.tourForEdit = tour;
        fillFieldsForEdit();
    }

    private void fillFieldsForEdit() {
        txtTitle.setText(tourForEdit.getTitle());
        txtDestination.setText(tourForEdit.getDestination());
        txtPrice.setText(String.valueOf(tourForEdit.getPrice()));
        txtDays.setText(String.valueOf(tourForEdit.getDays()));
        cmbTransportType.setValue(tourForEdit.getTransport());
        cmbFoodType.setValue(tourForEdit.getFoodType());
        cmbTourType.setValue(tourForEdit.getTourType());
    }

    public void handleSave() {
        String title = txtTitle.getText().trim();
        String destination = txtDestination.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String daysStr = txtDays.getText().trim();
        TransportType transport = cmbTransportType.getValue();
        FoodType food = cmbFoodType.getValue();
        TourType tourType = cmbTourType.getValue();

        logger.info("Спроба збереження туру: " + title);

        if (title.isEmpty() || destination.isEmpty() || priceStr.isEmpty() || daysStr.isEmpty()
                || transport == null || food == null || tourType == null) {
            AlertUtil.showError("Будь ласка, заповніть усі поля.");
            return;
        }

        if (!Validator.isValidName(title)) {
            AlertUtil.showError("Назва туру повинна містити щонайменше 3 символи.");
            return;
        }

        if (!Validator.isValidDestination(destination)) {
            AlertUtil.showError("Місце призначення повинно містити щонайменше 3 символи.");
            return;
        }

        if (!Validator.isValidPrice(priceStr)) {
            AlertUtil.showError("Ціна повинна бути додатнім числом.");
            return;
        }

        if (!Validator.isValidDays(daysStr)) {
            AlertUtil.showError("Кількість днів повинна бути додатнім числом.");
            return;
        }
        
        double price;
        int days;
        try {
            price = Double.parseDouble(priceStr);
            days = Integer.parseInt(daysStr);
        } catch (NumberFormatException e) {
            AlertUtil.showError("Ціна та кількість днів мають бути числовими.");
            logger.log(Level.WARNING, "Неправильний формат чисел: " + priceStr + ", " + daysStr, e);
            return;
        }

        if (price <= 0 || days <= 0) {
            AlertUtil.showError("Ціна та кількість днів мають бути більше 0.");
            return;
        }

        try {
            if (tourForEdit == null) {
                TourPackage newTour = new TourPackage(0, title, destination, price, days, transport, food, tourType);
                tourService.addTour(newTour);
                logger.info("Додано новий тур: " + title);
                AlertUtil.showInfo("Тур успішно додано.");
            } else {
                tourForEdit.setTitle(title);
                tourForEdit.setDestination(destination);
                tourForEdit.setPrice(price);
                tourForEdit.setDays(days);
                tourForEdit.setTransport(transport);
                tourForEdit.setFoodType(food);
                tourForEdit.setTourType(tourType);

                tourService.updateTour(tourForEdit);
                logger.info("Оновлено тур: " + title);
                AlertUtil.showInfo("Тур успішно оновлено.");
            }
            closeWindow();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Критична помилка при збереженні туру", e);
            AlertUtil.showError("Не вдалося зберегти тур. Деталі надіслано адміністратору.");
        }
    }

    public void handleCancel() {
        logger.info("Скасовано редагування туру");
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}

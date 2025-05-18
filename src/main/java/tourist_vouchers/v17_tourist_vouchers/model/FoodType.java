package tourist_vouchers.v17_tourist_vouchers.model;

import javafx.beans.property.SimpleStringProperty;

public enum FoodType {
    ALL_INCLUSIVE("Все включено"),
    BREAKFAST_ONLY("Тільки сніданок"),
    NO_MEALS("Без харчування"),
    FULL_BOARD("Повний пансіон");

    private final String displayName;

    FoodType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SimpleStringProperty displayNameProperty() {
        return new SimpleStringProperty(displayName);
    }

    @Override
    public String toString() {
        return displayName;
    }
}

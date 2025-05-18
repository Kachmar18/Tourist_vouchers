package tourist_vouchers.v17_tourist_vouchers.model;

import javafx.beans.property.SimpleStringProperty;

public enum TourType {
    REST("Відпочинок"),
    EXCURSION("Екскурсії"),
    TREATMENT("Санаторій"),
    SHOPPING("Шопінг"),
    CRUISE("Круїз");

    private final String displayName;

    TourType(String displayName) {
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

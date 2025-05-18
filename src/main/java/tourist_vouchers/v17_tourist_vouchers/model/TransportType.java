package tourist_vouchers.v17_tourist_vouchers.model;

import javafx.beans.property.SimpleStringProperty;

public enum TransportType {
    BUS("Автобус"),
    PLANE("Літак"),
    TRAIN("Поїзд"),
    SHIP("Корабель"),
    CAR("Машина");

    private final String displayName;

    TransportType(String displayName) {
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

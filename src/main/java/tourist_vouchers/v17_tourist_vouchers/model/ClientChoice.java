package tourist_vouchers.v17_tourist_vouchers.model;

import java.time.LocalDate;

public class ClientChoice {
    private int id;
    private TourPackage selectedTour;
    private LocalDate bookingDate;
    private String clientName;

    public ClientChoice(int id, TourPackage selectedTour, LocalDate bookingDate, String clientName) {
        this.id = id;
        this.selectedTour = selectedTour;
        this.bookingDate = bookingDate;
        this.clientName = clientName;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public TourPackage getSelectedTour() {
        return selectedTour;
    }
    public void setSelectedTour(TourPackage selectedTour) {
        this.selectedTour = selectedTour;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}

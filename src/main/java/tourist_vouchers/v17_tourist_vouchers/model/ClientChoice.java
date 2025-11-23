package tourist_vouchers.v17_tourist_vouchers.model;

import java.time.LocalDate;

public class ClientChoice {
    private int id_client;
    private int id_selectedTour;
    private LocalDate bookingDate;
    private String name;
    private int phone;
    private int password;

    public ClientChoice(int id_client, String name, String phone, String password, LocalDate bookingDate, int id_selectedTour) {
        this.id_client = id_client;
        this.name = name;
        this.phone = Integer.parseInt(phone);
        this.password = Integer.parseInt(password);
        this.bookingDate = bookingDate;
        this.id_selectedTour = id_selectedTour;
    }

    public ClientChoice() {

    }

    public int getId_client() {
        return id_client;
    }
    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getId_selectedTour() {
        return id_selectedTour;
    }
    public void setId_selectedTour(int id_selectedTour) {
        this.id_selectedTour = id_selectedTour;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }
    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getPassword() {
        return password;
    }
    public void setPassword(int password) {
        this.password = password;
    }
}

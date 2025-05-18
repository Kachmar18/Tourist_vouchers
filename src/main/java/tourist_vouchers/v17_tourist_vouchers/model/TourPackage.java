package tourist_vouchers.v17_tourist_vouchers.model;

public class TourPackage {
    private int id;
    private String title;
    private String description;
    private double price;
    private int days;
    private TransportType transport;
    private FoodType foodType;
    private TourType tourType;

    public TourPackage(int id, String title, String description, double price, int days, TransportType transport, FoodType foodType, TourType tourType) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.days = days;
        this.transport = transport;
        this.foodType = foodType;
        this.tourType = tourType;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getDays() {
        return days;
    }
    public void setDays(int days) {
        this.days = days;
    }

    public TransportType getTransport() {
        return transport;
    }
    public void setTransport(TransportType transport) {
        this.transport = transport;
    }

    public FoodType getFoodType() {
        return foodType;
    }
    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public TourType getTourType() {
        return tourType;
    }
    public void setTourType(TourType tourType) {
        this.tourType = tourType;
    }
}

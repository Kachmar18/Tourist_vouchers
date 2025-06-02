package tourist_vouchers.v17_tourist_vouchers.services;

import tourist_vouchers.v17_tourist_vouchers.dao.TourPackageDAO;
import tourist_vouchers.v17_tourist_vouchers.model.FoodType;
import tourist_vouchers.v17_tourist_vouchers.model.TourPackage;
import tourist_vouchers.v17_tourist_vouchers.model.TourType;
import tourist_vouchers.v17_tourist_vouchers.model.TransportType;

import java.sql.SQLException;
import java.util.List;

public class TourPackageService {
    private final TourPackageDAO tourPackageDAO;

    public TourPackageService() {
        this.tourPackageDAO = new TourPackageDAO();
    }

    public List<TourPackage> getAllTours() {
        try {
            return tourPackageDAO.getAllTours();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public TourPackage getTourById(int id) {
        try {
            return tourPackageDAO.getTourById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<TourPackage> filterTours(String destination, Double maxPrice, Integer maxDays,
                                         TourType tourType, FoodType foodType, TransportType transportType) {
        return getAllTours().stream()
                .filter(t -> destination == null || destination.isEmpty() || t.getDestination().equals(destination))
                .filter(t -> maxPrice == null || t.getPrice() <= maxPrice)
                .filter(t -> maxDays == null || t.getDays() <= maxDays)
                .filter(t -> tourType == null || t.getTourType() == tourType)
                .filter(t -> foodType == null || t.getFoodType() == foodType)
                .filter(t -> transportType == null || t.getTransport() == transportType)
                .toList();
    }

    public List<String> getAllDestinations() {
        try {
            return tourPackageDAO.getAllDestinations();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // Повертаємо пустий список у разі помилки
        }
    }

    public void addTour(TourPackage tour) {
        try {
            tourPackageDAO.addTour(tour);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTour(TourPackage tour) {
        try {
            tourPackageDAO.updateTour(tour);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не вдалося оновити тур", e);
        }
    }

    public void deleteTour(int id) {
        try {
            tourPackageDAO.deleteTour(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не вдалося видалити тур", e);
        }
    }
}
package tourist_vouchers.v17_tourist_vouchers.services;

import tourist_vouchers.v17_tourist_vouchers.dao.TourPackageDAO;
import tourist_vouchers.v17_tourist_vouchers.model.TourPackage;

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

    public void addTour(TourPackage tour) {
        try {
            tourPackageDAO.addTour(tour);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<TourPackage> filterTours(String destination, Double maxPrice, Integer days) {
        // Реалізація фільтрації
        return getAllTours(); // Тимчасово
    }




    // У класі TourPackageService додаємо
    public List<String> getAllDestinations() {
        try {
            return tourPackageDAO.getAllDestinations();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // Повертаємо пустий список у разі помилки
        }
    }
}
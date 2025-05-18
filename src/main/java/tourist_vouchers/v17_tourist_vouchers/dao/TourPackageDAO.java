package tourist_vouchers.v17_tourist_vouchers.dao;

import tourist_vouchers.v17_tourist_vouchers.model.TourPackage;

import java.util.List;

public interface TourPackageDAO {
    List<TourPackage> getAllTours();
    void addTour(TourPackage tour);
    void updateTour(TourPackage tour);
    void deleteTour(int id);
    TourPackage getTourById(int id);
}
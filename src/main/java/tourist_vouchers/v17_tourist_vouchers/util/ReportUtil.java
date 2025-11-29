package tourist_vouchers.v17_tourist_vouchers.util;

import tourist_vouchers.v17_tourist_vouchers.model.ClientChoice;
import tourist_vouchers.v17_tourist_vouchers.services.TourPackageService;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

public class ReportUtil {
    private static final Logger logger = Logger.getLogger(ReportUtil.class.getName());
    private static final TourPackageService tourService = new TourPackageService();

    public static void exportClientsToCSV(List<ClientChoice> clients, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            writer.print("\uFEFF");
            writer.println("ID клієнта,ПІБ клієнта,Телефон,Дата бронювання,ID туру,Назва туру,Пункт призначення,Ціна (грн),Тривалість (дні),Транспорт,Харчування,Тип туру");

            for (ClientChoice client : clients) {
                // Отримуємо інформацію про тур
                String tourTitle = "Невідомо";
                String destination = "Невідомо";
                String price = "0";
                String days = "0";
                String transport = "Невідомо";
                String foodType = "Невідомо";
                String tourType = "Невідомо";

                try {
                    var tour = tourService.getTourById(client.getId_selectedTour());
                    if (tour != null) {
                        tourTitle = escapeCsv(tour.getTitle());
                        destination = escapeCsv(tour.getDestination());
                        price = String.format("%.2f", tour.getPrice());
                        days = String.valueOf(tour.getDays());
                        transport = tour.getTransport() != null ? tour.getTransport().getDisplayName() : "Невідомо";
                        foodType = tour.getFoodType() != null ? tour.getFoodType().getDisplayName() : "Невідомо";
                        tourType = tour.getTourType() != null ? tour.getTourType().getDisplayName() : "Невідомо";
                    }
                } catch (Exception e) {
                    logger.warning("Не вдалося отримати інформацію про тур ID: " + client.getId_selectedTour());
                }

                String line = String.join(",",
                        String.valueOf(client.getId_client()),
                        escapeCsv(client.getName()),
                        escapeCsv(String.valueOf(client.getPhone())),
                        escapeCsv(client.getBookingDate() != null ? client.getBookingDate().toString() : "Невідомо"),
                        String.valueOf(client.getId_selectedTour()),
                        tourTitle,
                        destination,
                        price,
                        days,
                        escapeCsv(transport),
                        escapeCsv(foodType),
                        escapeCsv(tourType)
                );

                writer.println(line);
            }

            logger.info("CSV звіт успішно створено: " + filePath);

        } catch (IOException e) {
            logger.severe("Помилка при створенні CSV звіту: " + e.getMessage());
            throw new RuntimeException("Не вдалося створити CSV файл", e);
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        // Якщо значення містить кому, лапки або перенос рядка - екрануємо лапками
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            // Подвійні лапки екрануються подвійними лапками
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }

        return value;
    }
}
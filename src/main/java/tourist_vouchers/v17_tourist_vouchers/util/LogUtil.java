package tourist_vouchers.v17_tourist_vouchers.util;

import java.io.IOException;
import java.util.logging.*;

public class LogUtil {
    private static final Logger logger = Logger.getLogger("TourAppLogger");

    static {
        try {
            LogManager.getLogManager().reset();
            logger.setLevel(Level.ALL);

            // Лог у файл
            FileHandler fileHandler = new FileHandler("tour_app.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            // Консоль (опціонально)
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);

        } catch (IOException e) {
            System.err.println("Не вдалося налаштувати логгер: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}

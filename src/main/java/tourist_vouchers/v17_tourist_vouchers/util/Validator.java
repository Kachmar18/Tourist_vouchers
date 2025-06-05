package tourist_vouchers.v17_tourist_vouchers.util;

public class Validator {
    public static boolean isValidName(String name) {
        return name != null && name.length() >= 3;
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{9}");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 4;
    }

    public static boolean isValidDestination(String destination) {
        return destination != null && destination.length() >= 3;
    }

    public static boolean isValidPrice(String priceStr) {
        if (priceStr == null) return false;
        try {
            double price = Double.parseDouble(priceStr.trim());
            return price > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDays(String daysStr) {
        if (daysStr == null) return false;
        try {
            int days = Integer.parseInt(daysStr.trim());
            return days > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

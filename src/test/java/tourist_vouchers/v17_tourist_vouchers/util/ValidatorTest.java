package tourist_vouchers.v17_tourist_vouchers.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void testIsValidName() {
        assertTrue(Validator.isValidName("Alice"));
        assertFalse(Validator.isValidName(null));
        assertFalse(Validator.isValidName("Al"));
    }

    @Test
    void testIsValidPhone() {
        assertTrue(Validator.isValidPhone("123456789"));
        assertFalse(Validator.isValidPhone(null));
        assertFalse(Validator.isValidPhone("12345"));
        assertFalse(Validator.isValidPhone("abcdefghi"));
    }

    @Test
    void testIsValidPassword() {
        assertTrue(Validator.isValidPassword("pass123"));
        assertFalse(Validator.isValidPassword(null));
        assertFalse(Validator.isValidPassword("123"));
    }

    @Test
    void testIsValidDestination() {
        assertTrue(Validator.isValidDestination("Paris"));
        assertFalse(Validator.isValidDestination(null));
        assertFalse(Validator.isValidDestination("Pa"));
    }

    @Test
    void testIsValidPrice() {
        assertTrue(Validator.isValidPrice("100"));
        assertTrue(Validator.isValidPrice(" 50.5 "));
        assertFalse(Validator.isValidPrice(null));
        assertFalse(Validator.isValidPrice("-10"));
        assertFalse(Validator.isValidPrice("abc"));
    }

    @Test
    void testIsValidDays() {
        assertTrue(Validator.isValidDays("5"));
        assertTrue(Validator.isValidDays(" 10 "));
        assertFalse(Validator.isValidDays(null));
        assertFalse(Validator.isValidDays("0"));
        assertFalse(Validator.isValidDays("-3"));
        assertFalse(Validator.isValidDays("abc"));
    }
}

package tourist_vouchers.v17_tourist_vouchers.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    @Test
    void testValidName() {
        assertTrue(Validator.isValidName("Anna"));
        assertFalse(Validator.isValidName("An"));
        assertFalse(Validator.isValidName(null));
    }

    @Test
    void testValidPhone() {
        assertTrue(Validator.isValidPhone("123456789"));
        assertFalse(Validator.isValidPhone("12345"));
        assertFalse(Validator.isValidPhone("abcdefgh"));
        assertFalse(Validator.isValidPhone(null));
    }

    @Test
    void testValidPassword() {
        assertTrue(Validator.isValidPassword("pass"));
        assertFalse(Validator.isValidPassword("123"));
        assertFalse(Validator.isValidPassword(null));
    }

    @Test
    void testValidDestination() {
        assertTrue(Validator.isValidDestination("Kyiv"));
        assertFalse(Validator.isValidDestination("K"));
        assertFalse(Validator.isValidDestination(null));
    }

    @Test
    void testValidPrice() {
        assertTrue(Validator.isValidPrice("100.5"));
        assertFalse(Validator.isValidPrice("-5"));
        assertFalse(Validator.isValidPrice("abc"));
        assertFalse(Validator.isValidPrice(null));
    }

    @Test
    void testValidDays() {
        assertTrue(Validator.isValidDays("10"));
        assertFalse(Validator.isValidDays("0"));
        assertFalse(Validator.isValidDays("-2"));
        assertFalse(Validator.isValidDays("abc"));
        assertFalse(Validator.isValidDays(null));
    }
}
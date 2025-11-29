package tourist_vouchers.v17_tourist_vouchers.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    void testDefaultConstructor() {
        Admin admin = new Admin();
        assertNotNull(admin);
    }

    @Test
    void testFullConstructor() {
        LocalDateTime now = LocalDateTime.now();

        Admin admin = new Admin(
                1,
                2,
                "John Doe",
                "123456789",
                "pass",
                now,
                true
        );

        assertEquals(1, admin.getIdAdmin());
        assertEquals(2, admin.getIdUserType());
        assertEquals("John Doe", admin.getFullName());
        assertEquals("123456789", admin.getPhone());
        assertEquals("pass", admin.getPassword());
        assertEquals(now, admin.getCreatedAt());
        assertTrue(admin.isActive());
    }

    @Test
    void testSettersAndGetters() {
        Admin admin = new Admin();

        LocalDateTime now = LocalDateTime.now();

        admin.setIdAdmin(10);
        admin.setIdUserType(20);
        admin.setFullName("Jane Doe");
        admin.setPhone("987654321");
        admin.setPassword("qwerty");
        admin.setCreatedAt(now);
        admin.setActive(false);

        assertEquals(10, admin.getIdAdmin());
        assertEquals(20, admin.getIdUserType());
        assertEquals("Jane Doe", admin.getFullName());
        assertEquals("987654321", admin.getPhone());
        assertEquals("qwerty", admin.getPassword());
        assertEquals(now, admin.getCreatedAt());
        assertFalse(admin.isActive());
    }
}

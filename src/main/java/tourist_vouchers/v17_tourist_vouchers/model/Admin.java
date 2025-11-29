package tourist_vouchers.v17_tourist_vouchers.model;

import java.time.LocalDateTime;

public class Admin {
    private int idAdmin;
    private int idUserType;
    private String fullName;
    private String phone;
    private String password;
    private LocalDateTime createdAt;
    private boolean isActive;

    public Admin() {}

    public Admin(int idAdmin, int idUserType, String fullName, String phone, String password, LocalDateTime createdAt, boolean isActive) {
        this.idAdmin = idAdmin;
        this.idUserType = idUserType;
        this.fullName = fullName;
        this.phone = phone;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public int getIdAdmin() { return idAdmin; }
    public void setIdAdmin(int idAdmin) { this.idAdmin = idAdmin; }

    public int getIdUserType() { return idUserType; }
    public void setIdUserType(int idUserType) { this.idUserType = idUserType; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
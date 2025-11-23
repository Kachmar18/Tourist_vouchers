package tourist_vouchers.v17_tourist_vouchers.model;

import java.time.LocalDateTime;

public class Client {
    private int idClient;
    private int idUserType;
    private String fullName;
    private String phone;
    private String password;
    private LocalDateTime createdAt;
    private boolean isActive;

    public Client() {}

    public Client(int idClient, int idUserType, String fullName, String phone, String password, LocalDateTime createdAt, boolean isActive) {
        this.idClient = idClient;
        this.idUserType = idUserType;
        this.fullName = fullName;
        this.phone = phone;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }

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
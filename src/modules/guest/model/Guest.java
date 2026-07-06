package modules.guest.model;

import java.time.LocalDateTime;

public class Guest {
    private Integer id;
    private String phone;
    private String gender;
    private LocalDateTime createdAt;

    public Guest() {}

    public Guest(String phone, String gender) {
        this.phone = phone;
        this.gender = gender;
        this.createdAt = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
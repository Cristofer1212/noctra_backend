package modules.event.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event {

    private Integer id;
    private Integer userId;
    private String name;
    private String address;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer capacity;
    private String state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Event() {
    }

    // Constructor completo (sin IDs/Fechas generadas automáticamente)
    public Event(Integer userId, String name, String address, LocalDateTime startDate,
                 LocalDateTime endDate, Integer capacity) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.capacity = capacity;
        this.state = "PENDING"; // Estado por defecto según tu tabla
    }

    // GETTERS Y SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ---------------------------------------------------------------
    // Helpers de UI: agrupar por fecha para el dashboard (Activos/Próximos).
    // Ojo: esto es independiente de "state" (que maneja PENDING/aprobación).
    // Un evento puede estar APPROVED y aun así ser "próximo" si su fecha
    // de inicio todavía no llegó.
    // ---------------------------------------------------------------

    public boolean isActive(LocalDate today) {
        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();
        return !today.isBefore(start) && !today.isAfter(end);
    }

    public boolean isUpcoming(LocalDate today) {
        return startDate.toLocalDate().isAfter(today);
    }
}
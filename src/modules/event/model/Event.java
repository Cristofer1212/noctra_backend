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
    // Helpers de UI: Clasificación por bloques de Horas completas
    // ---------------------------------------------------------------

    public boolean isActive(LocalDateTime now) {
        LocalDateTime ahora = now.withMinute(0).withSecond(0).withNano(0);
        LocalDateTime inicio = startDate.withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fin = endDate.withMinute(0).withSecond(0).withNano(0);

        // Está activo si ya llegó a la hora de inicio y aún no llega a la hora de fin
        return (ahora.isEqual(inicio) || ahora.isAfter(inicio)) && ahora.isBefore(fin);
    }

    public boolean isUpcoming(LocalDateTime now) {
        LocalDateTime ahora = now.withMinute(0).withSecond(0).withNano(0);
        LocalDateTime inicio = startDate.withMinute(0).withSecond(0).withNano(0);

        // Es próximo si la hora de inicio es mayor a la hora actual
        return inicio.isAfter(ahora);
    }

    public boolean isPast(LocalDateTime now) {
        LocalDateTime ahora = now.withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fin = endDate.withMinute(0).withSecond(0).withNano(0);

        // Es pasado si la hora actual ya alcanzó o superó la hora de finalización
        return ahora.isEqual(fin) || ahora.isAfter(fin);
    }
}
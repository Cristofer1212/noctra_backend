package modules.invitation.model;

import java.time.LocalDateTime;

public class Invitation {
  private Integer id;
  private Integer eventId;
  private Integer issuerUserId;
  private Integer guestId;
  private String token;
  private String codeQr;
  private String state;
  private TypeOfGuest typeOfGuest;
  private LocalDateTime createdAt;
  private LocalDateTime sentAt;
  private LocalDateTime readAt;

  // Constructor vacío
  public Invitation() {
  }

  // Constructor con campos obligatorios
  public Invitation(Integer eventId, Integer issuerUserId, Integer guestId, String token, String codeQr,
      TypeOfGuest typeOfGuest) {
    this.eventId = eventId;
    this.issuerUserId = issuerUserId;
    this.guestId = guestId;
    this.token = token;
    this.typeOfGuest = typeOfGuest;
    this.codeQr = codeQr;
    this.state = "SIN_USAR";
    this.createdAt = LocalDateTime.now();
  }

  // Getters y Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getEventId() {
    return eventId;
  }

  public void setEventId(Integer eventId) {
    this.eventId = eventId;
  }

  public Integer getIssuerUserId() {
    return issuerUserId;
  }

  public void setIssuerUserId(Integer issuerUserId) {
    this.issuerUserId = issuerUserId;
  }

  public Integer getGuestId() {
    return guestId;
  }

  public void setGuestId(Integer guestId) {
    this.guestId = guestId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public TypeOfGuest getTypeOfGuest() {
    return this.typeOfGuest;
  }

  public void setTypeOfGuest(TypeOfGuest typeOfGuest) {
    this.typeOfGuest = typeOfGuest;
  }

  public String getCodeQr() {
    return codeQr;
  }

  public void setCodeQr(String codeQr) {
    this.codeQr = codeQr;
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

  public LocalDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(LocalDateTime sentAt) {
    this.sentAt = sentAt;
  }

  public LocalDateTime getReadAt() {
    return readAt;
  }

  public void setReadAt(LocalDateTime readAt) {
    this.readAt = readAt;
  }
}

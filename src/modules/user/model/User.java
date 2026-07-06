package modules.user.model;

import java.time.LocalDateTime;

public class User {
  private Integer id;
  private String dni;
  private String name;
  private String lastName;
  private String phone;
  private String address;
  private String mail;
  private String password; // TO DO --> Hashear PIN
  private String state;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String nickname;

  public User() {
  }

  public User(Integer id, String password) {
    this.id = id;
    // this.mail = mail;
    this.password = password;
  }

  public User(String dni, String name, String lastName, String phone, String address, String mail, String password,
      String nickname) {
    this.dni = dni;
    this.name = name;
    this.lastName = lastName;
    this.phone = phone;
    this.address = address;
    this.mail = mail;
    this.password = password;
    this.state = "Active"; // Estado por defecto
    this.nickname = nickname;
  }

  // GETTERS Y SETTERS
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

    public String getPin() {
      return password;
    }
}

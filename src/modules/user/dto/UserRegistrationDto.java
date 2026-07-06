package modules.user.dto;

public class UserRegistrationDto {

  private String name;
  private String lastName;
  private String dni;
  private String phone;
  private String pin;
  private String nickname;

  public UserRegistrationDto() {
  }

  public UserRegistrationDto(String name, String lastName, String dni, String pin, String nickname) {

    this.name = name;
    this.lastName = lastName;
    this.dni = dni;
    this.pin = pin;
    this.nickname = nickname;
  }

  // GETTERS Y SETTERS
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

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

}

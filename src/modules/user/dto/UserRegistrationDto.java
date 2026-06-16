package modules.user.dto;


public class UserRegistrationDto {

    private String name;
    private String lastName;
    private String dni;

    public UserRegistrationDto() {}

    public UserRegistrationDto(String name, String lastName, String dni) {

        this.name = name;
        this.lastName = lastName;
        this.dni = dni;

    }

    // GETTERS Y SETTERS
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }



}
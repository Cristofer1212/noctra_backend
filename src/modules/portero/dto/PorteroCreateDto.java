package modules.portero.dto;

public class PorteroCreateDto {

    private String dni;
    private String numCelular;


    public PorteroCreateDto(String dni, String numCelularPortero ) {
        this.dni = dni;
        this.numCelular = numCelularPortero;
    }

    public String getDni() {
        return dni;
    }

    public String getNumCelularPortero() {
        return numCelular;
    }


}

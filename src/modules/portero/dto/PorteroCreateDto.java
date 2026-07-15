package modules.portero.dto;

public class PorteroCreateDto {

    private String dni;
    private String numCelular;

    public PorteroCreateDto(String dni, String numCelular) {
        this.dni = dni;
        this.numCelular = numCelular;
    }

    public String getDni() {
        return dni;
    }

    // Corregido para que coincida con el nombre del campo
    public String getNumCelular() {
        return numCelular;
    }
}
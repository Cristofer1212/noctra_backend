package modules.shared.integrations.decolecta;

import com.google.gson.annotations.SerializedName;

public class DecolectaReniecReponseDto {

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("first_last_name")
    private String firstLastName;


    public String getFirstName() { return firstName; }
    public String getFirstLastName() { return firstLastName; }

    public String getNombreCompleto() {
        return firstName + " " + firstLastName;
    }

}

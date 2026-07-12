package modules.invitation.model;

public enum TypeOfGuest {

  GUEST("guest"),
  ADMIN("admin"),
  COLLABORATOR("collaborator");

  private final String value;

  // El constructor de un enum es implicitamente privado
  private TypeOfGuest(String value) {
    this.value = value;
  }

  // Getter para obtener el string asociado (ej. "guest")
  public String getValue() {
    return value;
  }

  // Método de utilidad para converir un String de la DB a este Enum
  public static TypeOfGuest fromString(String text) {

    for (TypeOfGuest b : TypeOfGuest.values()) {

      if (b.value.equalsIgnoreCase(text)) {

        return b;

      }

    }

    throw new IllegalArgumentException("No constant whit text " + text + "found");

  }

}

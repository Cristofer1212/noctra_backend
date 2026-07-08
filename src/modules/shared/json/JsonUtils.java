package modules.shared.json;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class JsonUtils {

  // Configuramos Gson usando GsonBuilder para registrar el adaptador antes de
  // instanciarlo
  private static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
      .create();

  // Método helper para convertir JSON a Objeto
  public static <T> T fromJson(String json, Class<T> classOfT) {
    return gson.fromJson(json, classOfT);
  }

  // Adaptador personalizado para deserializar y serializar LocalDateTime de forma
  // segura
  private static class LocalDateTimeDeserializer
      implements JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {

    // Definimos los patrones de formato que esperas recibir
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      String dateStr = json.getAsString();
      try {
        // 1. Intentamos parsear con fecha y hora completo (ej. "12/10/2026 15:30:00")
        return LocalDateTime.parse(dateStr, DATE_TIME_FORMATTER);
      } catch (DateTimeParseException e) {
        try {
          // 2. Si falla (como en tu caso), es porque solo viene la fecha ("12/10/2026").
          // Lo parseamos como LocalDate y le asignamos automáticamente el inicio del día
          // (00:00:00)
          LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
          return date.atStartOfDay();
        } catch (DateTimeParseException ex) {
          throw new JsonParseException(
              "No se pudo parsear la fecha: " + dateStr + ". Formatos esperados: dd/MM/yyyy o dd/MM/yyyy HH:mm:ss", ex);
        }
      }
    }

    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(src.format(DATE_TIME_FORMATTER));
    }
  }

  // Agrega este método dentro de tu clase JsonUtils (si no lo tienes ya):
  public static String toJson(Object src) {
    return gson.toJson(src);
  }

}

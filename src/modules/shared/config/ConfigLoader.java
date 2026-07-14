package modules.shared.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
  private static final Properties properties = new Properties();

  static {
    try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {

      if (input == null) {
        System.err.println("No se encontró config.properties");
      } else {
        properties.load(input);

        System.out.println("===== PROPIEDADES CARGADAS =====");
        properties.forEach((k, v) ->
                System.out.println(k + " = " + v));
        System.out.println("===============================");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String getProperty(String key) {
    return properties.getProperty(key);
  }
}

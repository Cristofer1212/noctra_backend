package modules.shared.json;

import com.google.gson.Gson;



public class JsonUtils {

    private static final Gson gson = new Gson();


    // Metodo helper para convertir JSON a Objeto (usando Gson)
    public static  <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }





}
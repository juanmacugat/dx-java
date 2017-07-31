package com.mercadopago.core;

import com.google.gson.*;
import com.mercadopago.exceptions.MPException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Mercado Pago SDK
 * Core utils class
 *
 * Created by Eduardo Paoletta on 11/17/16.
 */
public class MPCoreUtils {

    private static final String FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ";

    private static Gson gson = new GsonBuilder()
            .setDateFormat(FORMAT_ISO8601)
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();


    /**
     * Retrieves all fields from a class except the ones from MPBase abstract class and Object class
     *
     * @param type          Java Class type
     * @return
     */
    static Field[] getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> clazz = type; clazz != null; clazz = clazz.getSuperclass()) {
            if (clazz == MPBase.class ||
                    clazz == Object.class) {
                break;
            }
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        Field[] fieldsArray = new Field[fields.size()];
        return fields.toArray(fieldsArray);
    }

    /**
     * Static method that transforms all attributes members of the instance in a JSON Object.
     *
     * @return                  a JSON Object with the attributes members of the instance
     */
    public static <T extends MPBase> JsonObject getJsonFromResource(T resourceObject) {
        return (JsonObject) gson.toJsonTree(resourceObject);
    }

    /**
     * Static method that transforms a Json Object in a MP Resource.
     *
     * @param clazz             Java Class type of the resource
     * @param jsonEntity        JsonObject to be transformed
     * @param <T>
     * @return
     */
    public static <T> T getResourceFromJson(Class clazz, JsonObject jsonEntity) {
        return (T) gson.fromJson(jsonEntity, clazz);
    }

    /**
     * Static method that transform an Input Stream to a String object, returns an empty string if InputStream is null.
     *
     * @param is                    Input Stream to process
     * @return                      a String with the stream content
     * @throws MPException
     */
    public static String inputStreamToString(InputStream is) throws MPException {
        String value = "";
        if (is != null) {
            try {
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                value = result.toString("UTF-8");

            } catch (Exception ex) {
                throw new MPException(ex);
            }
        }
        return value;

    }

    /**
     * Validates if an url is a valid url address
     *
     * @param url               url address to validate
     * @return
     * @throws MPException
     */
    public static boolean validateUrl(String url) {
        String[] schemes = {"https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }

    /**
     * Analizes a JsonElement and determines if its a result of a api search or loadAll method
     *
     * @param jsonElement       the jsonElement to be analized
     * @return
     */
    static JsonArray getArrayFromJsonElement(JsonElement jsonElement) {
        JsonArray jsonArray = null;
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
        } else if (jsonElement.isJsonObject() &&
                ((JsonObject) jsonElement).get("results") != null &&
                ((JsonObject) jsonElement).get("results").isJsonArray()) {
            jsonArray = ((JsonObject) jsonElement).get("results").getAsJsonArray();
        }
        return jsonArray;
    }

}

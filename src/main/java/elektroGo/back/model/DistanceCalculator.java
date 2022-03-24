/**
 * @file DistanceCalculator.java
 * @author Adria Abad
 * @date 22/03/2022
 * @brief Implementacio de la classe DistanceCalculator.
 */

package elektroGo.back.model;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

/**
 * @brief La classe DistanceCalculator representa s'utilitza per calcular la matriu de distancies entre l'origen,
 * el desti i les estacions de carrega
 */
public class DistanceCalculator {

    /**
     * @brief Capcelera de la url de la API de Google Maps
     */
    private final String urlHeader = "https://maps.googleapis.com/maps/api/distancematrix/json";

    /**
     * @brief Contingut de la url que especifica els punts d'origen
     */
    private String urlOrigins = "?origins=";

    /**
     * @brief Contingut de la url que especifica els punts de desti
     */
    private String urlDestinations = "&destinations=";

    /**
     * @brief Contingut de la url que especifica la API KEY
     */
    private String urlKey = "&key=";

    /**
     * @brief Funcio que crea la url de la API de Google Maps amb l'origen, el desti i les estacions de carrega
     * @param ori Punt d'origen de la ruta
     * @param dest Punt de desti de la ruta
     * @param chargers Possibles punts de carrega pels quals s'haura de passar a la ruta
     * @return Retorna la url creada
     */
    private String createUrl(Point ori, Point dest, ArrayList<Point> chargers) {
        urlOrigins += ori.getLatitude().toString() + "%2C" + ori.getLongitude().toString();
        for (Point point : chargers) {
            urlOrigins += "%7C" + point.getLatitude().toString() + "%2C" + point.getLongitude().toString();
        }
        urlDestinations += dest.getLatitude().toString() + "%2C" + dest.getLongitude().toString();
        for (Point point : chargers) {
            urlDestinations += "%7C" + point.getLatitude().toString() + "%2C" + point.getLongitude().toString();
        }
        return urlHeader + urlOrigins + urlDestinations + urlKey;
    }

    /**
     * @brief Funcio que utilitza la API de Google Maps per calcular la matriu de distancies entre l'origen, el desti
     *  i les estacions de carrega
     * @param ori Punt d'origen de la ruta
     * @param dest Punt de desti de la ruta
     * @param chargers Possibles punts de carrega pels quals s'haura de passar a la ruta
     * @return Retorna la matriu de distancies
     */
    public ArrayList<ArrayList<Integer>> calculateRoadDistanceMatrix(Point ori, Point dest, ArrayList<Point> chargers) {
        String url = createUrl(ori, dest, chargers);
        URI uri = URI.create(url);
        HttpGet request = new HttpGet(uri);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONArray rows = new JSONArray();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            String json_string = EntityUtils.toString(response.getEntity());
            JSONObject temp1 = new JSONObject(json_string);
            rows = temp1.getJSONArray("rows");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Parsing the json object to the distance matrix
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        for (int i = 0; i < rows.length(); ++i) {
            JSONArray jsonRow = rows.getJSONObject(i).getJSONArray("elements");
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < jsonRow.length(); ++j) {
                row.add((Integer) jsonRow.getJSONObject(j).getJSONObject("distance").get("value"));
            }
            matrix.add(row);
        }
        return matrix;
    }
}

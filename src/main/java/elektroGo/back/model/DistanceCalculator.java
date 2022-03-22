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

public class DistanceCalculator {
    private final String urlHeader = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private String urlOrigins = "?origins=";
    private String urlDestinations = "&destinations=";
    private String urlKey = "&key=";


    private String createUrl(Point ori, Point dest, ArrayList<Point> chargers) {
        urlOrigins += ori.getLatitude().toString() + "%2C" + ori.getLongitude().toString();
        for (Point point : chargers) {
            urlOrigins += "%7C" + point.getLatitude().toString() + "%2C" + point.getLongitude().toString();
        }
        urlDestinations += dest.getLatitude().toString() + "%2C" + dest.getLongitude().toString();
        for (Point point : chargers) {
            urlDestinations += "%7C" + point.getLatitude().toString() + "%2C" + point.getLongitude().toString();
        }
        urlKey += "";
        return urlHeader + urlOrigins + urlDestinations + urlKey;
    }

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

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
import java.util.List;

class Point {
    String name;
    Double lat;
    Double lon;
}

class Rectangle {
    Point BL = new Point();
    Point BR = new Point();
    Point TL = new Point();
    Point TR = new Point();
}

public class RouteCalculation {
    private ArrayList<Point> chargers;
    private int range; //in kilometers
    private Point origin;
    private Point destination;
    private ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
    private ArrayList<Point> candidates = new ArrayList<>();
    private ArrayList<Point> definitive = new ArrayList<>();
    private Integer minDistance;

    // todo HACER TEST UNITARIOS
    // todo ARREGLAR EL STRUCT DE POINT
    // todo hacer clase model.distanceCalculator que tenga las llamadas http y que
    //  devuelva double si no hay array chargers o que devuelva la matrix de distancias

    public RouteCalculation(int range, Double oriLat, Double oriLon, Double destLat, Double destLon) {
        Point origin = new Point(); origin.lat = oriLat; origin.lon = oriLon;
        Point destination = new Point(); destination.lat = destLat; destination.lon = destLon;
        this.origin = origin;
        this.destination = destination;
        this.range = range;
        getAllChargers();
        selectPossibleChargers();
    }

    // todo EN SU MOMENTO UTILIZAR LOS DATOS DE LA BD
    // post: chargers contiene todos los cargadores de que hay en la base de datos
    private void getAllChargers() {
        Point c1 = new Point(); c1.name = "Terrassa"; c1.lat = 41.659853; c1.lon = 2.015975;
        Point c2 = new Point(); c2.name = "Barcelona"; c2.lat = 41.379805; c2.lon = 2.141224;
        Point c3 = new Point(); c3.name = "Salou"; c3.lat = 41.069389; c3.lon = 1.087369;
        Point c4 = new Point(); c4.name = "Vic"; c4.lat = 41.914522; c4.lon = 2.262188;
        Point c5 = new Point(); c5.name = "Manresa"; c5.lat = 41.733242; c5.lon = 1.845603;
        Point c6 = new Point(); c6.name = "Girona"; c6.lat = 41.973401; c6.lon = 2.820924;
        Point c7 = new Point(); c7.name = "Andorra"; c7.lat = 42.497339; c7.lon = 1.507169;
        Point c8 = new Point(); c8.name = "Lleida"; c8.lat = 41.608993; c8.lon = 0.620819;

        Point[] list = new Point[]{c1, c2, c3, c4, c5, c6, c7, c8};
        chargers = new ArrayList<>(List.of(list));
    }
    
    private double calculateRoadDistanceOriDest(Point ori, Point dest) {
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json";
        url += "?origins=" + ori.lat.toString() + "%2C" + ori.lon.toString();
        url += "&destinations=" + dest.lat.toString() + "%2C" + dest.lon.toString();
        url += "&key=" + "";

        URI uri = URI.create(url);
        HttpGet request = new HttpGet(uri);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String distance = "-1";
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            //HttpEntity entity = response.getEntity();
            String json_string = EntityUtils.toString(response.getEntity());
            JSONObject temp1 = new JSONObject(json_string);
            JSONArray rows = temp1.getJSONArray("rows");
            distance = temp1.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").get("value").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(distance);
    }

    // Returns true if the point is inside the rectangle, returns false if not.
    private boolean inRectangle(Rectangle rectangle, Point point) {
        return point.lat <= rectangle.TR.lat && point.lat >= rectangle.BL.lat &&
                point.lon <= rectangle.BR.lon && point.lon >= rectangle.BL.lon;
    }

    private Rectangle buildRectangle(Point ori, Point dest) {
        Rectangle rectangle = new Rectangle();
        //Offset de 0.065 añadido para asegurar una area minima de busqueda
        rectangle.BL.lat = Math.min(ori.lat, dest.lat) - 0.065; rectangle.BL.lon = Math.min(ori.lon, dest.lon) - 0.065;
        rectangle.BR.lat = Math.min(ori.lat, dest.lat) - 0.065; rectangle.BR.lon = Math.max(ori.lon, dest.lon) + 0.065;
        rectangle.TR.lat = Math.max(ori.lat, dest.lat) + 0.065; rectangle.TR.lon = Math.max(ori.lon, dest.lon) + 0.065;
        rectangle.TL.lat = Math.max(ori.lat, dest.lat) + 0.065; rectangle.TL.lon = Math.min(ori.lon, dest.lon) - 0.065;
        return rectangle;
    }

    // post: candidates contiene los chargers candidatos a pertenecer a la ruta entre origen y destino
    private void selectPossibleChargers() {
        //Set rectangle area where chargers should be found
        //First naive version, improve to an oriented rectangle between points
        Rectangle rectangle = buildRectangle(origin, destination);
        for (Point p : chargers) {
            //System.out.println("Checking point: " + p.name);
            if (inRectangle(rectangle, p)) candidates.add(p);
        }
    }

    private JSONArray calculateRoadDistanceMatrix(Point ori, Point dest, ArrayList<Point> chargers) {
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json";
        url += "?origins=" + ori.lat.toString() + "%2C" + ori.lon.toString();
        for (Point point : chargers) {
            url += "%7C" + point.lat.toString() + "%2C" + point.lon.toString();
        }
        url += "&destinations=" + dest.lat.toString() + "%2C" + dest.lon.toString();
        for (Point point : chargers) {
            url += "%7C" + point.lat.toString() + "%2C" + point.lon.toString();
        }
        url += "&key=" + "";

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
        return rows;
    }

    private void backtrack(int i, int distance, ArrayList<Point> temporal) {
        if (distance > minDistance) return;
        if (matrix.get(i).get(0) > range) {
            // Destination not reachable
            for (int j  = 1; j < matrix.size(); ++j) {
                int actualDist = matrix.get(i).get(j);
                if (j > i && actualDist < range) {
                    temporal.add(candidates.get(j - 1));
                    backtrack(j, distance + actualDist, temporal);
                    temporal.clear();
                }
            }
        }
        else {
            // Destination reachable
            if (distance < minDistance) {
                minDistance = distance;
                definitive = new ArrayList<>(temporal);
            }
        }
    }

    // post: definitive contiene los chargers que forman la ruta (origin y destination not included)
    public void execute() {
        /*
            matrix es una array de arrays. rows = origins, columns = destinations
            matrix[0][0] es la distancia entre origin y destination
            matrix[1][1], matrix[2][2], etc... es distancia 0
            Las distancias se expresan en metros
        */
        range *= 1000;
        JSONArray jsonArray = calculateRoadDistanceMatrix(origin, destination, candidates);
        //Parsing the matrix returned from the API
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONArray jsonRow = jsonArray.getJSONObject(i).getJSONArray("elements");
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < jsonRow.length(); ++j) {
                row.add((Integer) jsonRow.getJSONObject(j).getJSONObject("distance").get("value"));
            }
            matrix.add(row);
        }
        minDistance = Integer.MAX_VALUE;
        ArrayList<Point> temporal = new ArrayList<>();
        backtrack(0, 0, temporal);
    }

    //post: returns the definitive arrayList as json
    public String getResult() {
        return "";
    }

    /*
    public static void main(String[] args) {

        origin = new Point(); origin.name = "Lloret de Mar"; origin.lat = 41.700801; origin.lon = 2.847613;
        destination = new Point(); destination.name = "Amposta"; destination.lat = 40.705865; destination.lon = 0.578646;

        //Point ori = new Point(); ori.name = "Lleida"; ori.lat = 41.612853; ori.lon = 0.634403;
        //Point dest = new Point(); dest.name = "Tarrega"; dest.lat = 41.644542; dest.lon = 1.140799;

        range = 111;

        double roadDistance = calculateRoadDistanceOriDest(origin, destination)/1000;
        System.out.println("Road distance: " + roadDistance + " kilometers");

        int stopsNeeded = (int) (roadDistance/range);
        System.out.println("Stops needed: " + stopsNeeded);

    }
     */
}

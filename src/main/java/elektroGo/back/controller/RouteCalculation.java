package elektroGo.back.controller;

import jdk.jshell.execution.Util;
import org.apache.http.HttpEntity;
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

class Vector {
    Double x;
    Double y;
}

class Rectangle {
    Point BL = new Point();
    Point BR = new Point();
    Point TL = new Point();
    Point TR = new Point();
}

public class RouteCalculation {
    static ArrayList<Point> chargers;
    static int stopsNeeded;
    static int range; //in kilometers


    static double calculateRawDistance(Double x1, Double y1, Double x2, Double y2) {
        double p = Math.PI / 180;
        double a = 0.5 - Math.cos((x2 - x1) * p)/2 +
                Math.cos(x1 * p) * Math.cos(x2 * p) *
                        (1 - Math.cos((y2 - y1) * p))/2;
        return Math.asin(Math.sqrt(a));
    }

    static double calculateRoadDistanceOriDest(Point ori, Point dest) {
        return 259000;
        /*
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json";
        url += "?origins=" + ori.lat.toString() + "%2C" + ori.lon.toString();
        url += "&destinations=" + dest.lat.toString() + "%2C" + dest.lon.toString();
        url += "&key=" + "";

        //String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=41.069389%2C1.087369&destinations=41.700801%2C2.847613&key=API_KEY";
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
        */
    }

    static boolean inRectangle(Rectangle rectangle, Point point) {
        //System.out.println("       " + point.lat + ", " + point.lon);
        if (point.lat > rectangle.TR.lat || point.lat < rectangle.BL.lat ||
            point.lon > rectangle.BR.lon || point.lon < rectangle.BL.lon) return false;
        return true;
    }

    static Rectangle buildRectangle(Point ori, Point dest) {
        Rectangle rectangle = new Rectangle();
        //Offset de 0.065 añadido para asegurar una area minima de busqueda
        rectangle.BL.lat = Math.min(ori.lat, dest.lat) - 0.065; rectangle.BL.lon = Math.min(ori.lon, dest.lon) - 0.065;
        rectangle.BR.lat = Math.min(ori.lat, dest.lat) - 0.065; rectangle.BR.lon = Math.max(ori.lon, dest.lon) + 0.065;
        rectangle.TR.lat = Math.max(ori.lat, dest.lat) + 0.065; rectangle.TR.lon = Math.max(ori.lon, dest.lon) + 0.065;
        rectangle.TL.lat = Math.max(ori.lat, dest.lat) + 0.065; rectangle.TL.lon = Math.min(ori.lon, dest.lon) - 0.065;
        return rectangle;
    }

    static ArrayList<Point> selectPossibleChargers(ArrayList<Point> chargers, Point ori, Point dest) {

        //Director vector, not in use right now
        Vector vector = new Vector(); vector.x = ori.lon - dest.lon; vector.y = ori.lat - dest.lat;
        System.out.println("Vector: x=" + vector.x + " y=" + vector.y);
        Double module = Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
        System.out.println("Vector normalized: x=" + vector.x/module + " y=" + vector.y/module);

        //Set rectangle area where chargers should be found
        //First naive version, improve to an oriented rectangle between points
        Rectangle rectangle = buildRectangle(ori, dest);

        System.out.println("BL = " + rectangle.BL.lat + ", " + rectangle.BL.lon);
        System.out.println("BR = " + rectangle.BR.lat + ", " + rectangle.BR.lon);
        System.out.println("TL = " + rectangle.TL.lat + ", " + rectangle.TL.lon);
        System.out.println("TR = " + rectangle.TR.lat + ", " + rectangle.TR.lon);

        ArrayList<Point> candidates = new ArrayList<>();
        for (Point p : chargers) {
            //System.out.println("Checking point: " + p.name);
            if (inRectangle(rectangle, p)) candidates.add(p);
        }
        return candidates;
    }

    static JSONArray calculateRoadDistanceMatrix(Point ori, Point dest, ArrayList<Point> chargers) {
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

        //String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=41.069389%2C1.087369&destinations=41.700801%2C2.847613&key=API_KEY";
        URI uri = URI.create(url);
        HttpGet request = new HttpGet(uri);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONArray rows = new JSONArray();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            //HttpEntity entity = response.getEntity();
            String json_string = EntityUtils.toString(response.getEntity());
            JSONObject temp1 = new JSONObject(json_string);
            rows = temp1.getJSONArray("rows");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

    static void recursiveRoute(int i, int distance, int minDistance, ArrayList<ArrayList<Integer>> matrix, ArrayList<Point> temporal, ArrayList<Point> definitive, ArrayList<Point> candidates, int range) {
        //matrix[x][0] is always the distance from x to the destination
        if (matrix.get(i).get(0) > range) { // destination not reachable
            int distDone = matrix.get(i).get(i+1);
            temporal.add(candidates.get(i+1));
            recursiveRoute(i+1, distance+distDone, minDistance, matrix, temporal, definitive, candidates, range);
        }
        if (distance < minDistance) {
            minDistance = distance;
            definitive = temporal;
        }
        temporal = new ArrayList<>();
    }

    static ArrayList<Point> getRoute(Point ori, Point dest, ArrayList<Point> candidates, int stopsNeeded, int range) { //in kilometers
        ArrayList<Point> route = new ArrayList<>();
        candidates.remove(4);
        candidates.remove(3);
        candidates.remove(0);

        range *= 1000;

        /*
            matrix es una array de arrays. rows = origins, columns = destinations
            matrix[0][0] es la distancia entre ori y dest
            matrix[1][1], matrix[2][2], etc... es distancia 0
        */
        JSONArray jsonArray = calculateRoadDistanceMatrix(ori, dest, candidates);

        //Parsing the matrix returned from the API
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONArray jsonRow = jsonArray.getJSONObject(0).getJSONArray("elements");
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < jsonRow.length(); ++j) {
                row.add((Integer) jsonRow.getJSONObject(j).getJSONObject("distance").get("value"));
            }
            matrix.add(row);
        }

        //get reachable chargers from ori
        ArrayList<Point> temporal = new ArrayList<>();
        for (int j = 1; j < matrix.get(0).size(); ++j) {
            if (matrix.get(0).get(j) < range) {
                //Reachable
                temporal.add(candidates.get(j));
            }
        }


        ArrayList<Point> definitive = new ArrayList<>();
        recursiveRoute(0,0, Integer.MAX_VALUE, matrix, temporal, definitive, candidates, range);


        System.out.println(matrix);


        return null;
    }

    public static void main(String[] args) {
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


        Point ori = new Point(); ori.name = "Lloret de Mar"; ori.lat = 41.700801; ori.lon = 2.847613;
        Point dest = new Point(); dest.name = "Amposta"; dest.lat = 40.705865; dest.lon = 0.578646;

        /*
        Point ori = new Point(); ori.name = "Lleida"; ori.lat = 41.612853; ori.lon = 0.634403;
        Point dest = new Point(); dest.name = "Tarrega"; dest.lat = 41.644542; dest.lon = 1.140799;
        */

        range = 109;

        double distance = calculateRawDistance(ori.lon, ori.lat, dest.lon, dest.lat);
        System.out.println("Raw Distance: " + String.format("%.02f", distance));

        double roadDistance = calculateRoadDistanceOriDest(ori, dest)/1000;
        System.out.println("(API GOOGLE) Road distance: " + roadDistance + " kilometers");

        stopsNeeded = (int) (roadDistance/range);
        System.out.println("Stops needed: " + stopsNeeded);

        //List of chargers to consider when computing route
        ArrayList<Point> candidates = selectPossibleChargers(chargers, ori, dest);

        /*
            todo pedir a google la matriz de distancia por carretera entre todos los chargers y ori y dest
            Dependiendo del numero de paradas, coger de la matriz las combinacions de origen-chargers-destino
            que sean viables (autonomia y kilometros entre puntos) y de estas coger la mas corta en km

            Por ejemplo: (hacerlo con loop segun n para n >= 2)
            if (paradas==1)
                llamar con Origins=ori i Destinations=candidates
                llamar con Origins=candidates i Destinations=dest

            else if (paradas==2)

            Otra opcion a la matriz:
                Ir paso a paso:
                    - Empezando por ori, calculando raw distancias hacia los chargers
                    y cojer a los que se pueda llegar.
                    (segun el numero de paradas)
                    - Calcular la distancia de estos chargers a todos los otros que se pueda llegar
                    (asi hasta que se complete el numero de paradas)
                    - Acabar calculando la distancia de los puntos de carga anteriores al destino
                    Escojer de todas las opciones de ruta posible, la que requiera menos distancia total.
        */

        ArrayList<Point> route = getRoute(ori, dest, candidates, stopsNeeded, range);

        System.out.println("Done");
    }
}

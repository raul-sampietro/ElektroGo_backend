package elektroGo.back.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    static double calculateRoadDistance() { //in kilometers
        return 195321;
        /* FUNCIONA -> distance contiene la distancia por carretera
        String string = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=41.069389%2C1.087369&destinations=41.700801%2C2.847613&key=API_KEY";
        URI uri = URI.create(string);
        HttpGet request = new HttpGet(uri);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String json_string = EntityUtils.toString(response.getEntity());
            JSONObject temp1 = new JSONObject(json_string);
            String distance = temp1.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").get("value").toString();
            System.out.println("Distance from google= " + distance);
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
    }

    //Pensado para el rectangulo naive
    static boolean inRectangle(Rectangle rectangle, Point point) {
        System.out.println("       " + point.lat + ", " + point.lon);
        if (point.lat > rectangle.TR.lat || point.lat < rectangle.BL.lat ||
            point.lon > rectangle.BR.lon || point.lon < rectangle.BL.lon) return false;
        return true;
    }

    static ArrayList<Point> selectPossibleChargers(ArrayList<Point> chargers, Point ori, Point dest) {

        //Director vector, not in use right now
        Vector vector = new Vector(); vector.x = ori.lon - dest.lon; vector.y = ori.lat - dest.lat;
        System.out.println("Vector: x=" + vector.x + " y=" + vector.y);
        Double module = Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
        System.out.println("Vector normalized: x=" + vector.x/module + " y=" + vector.y/module);

        //Set rectangle area where chargers should be found
        //First naive version, improve to an oriented rectangle between points
        Rectangle rectangle = new Rectangle();
        rectangle.BL.lat = Math.min(ori.lat, dest.lat); rectangle.BL.lon = Math.min(ori.lon, dest.lon);
        rectangle.BR.lat = Math.min(ori.lat, dest.lat); rectangle.BR.lon = Math.max(ori.lon, dest.lon);
        rectangle.TR.lat = Math.max(ori.lat, dest.lat); rectangle.TR.lon = Math.max(ori.lon, dest.lon);
        rectangle.TL.lat = Math.max(ori.lat, dest.lat); rectangle.TL.lon = Math.min(ori.lon, dest.lon);

        System.out.println("BL = " + rectangle.BL.lat + ", " + rectangle.BL.lon);
        System.out.println("BR = " + rectangle.BR.lat + ", " + rectangle.BR.lon);
        System.out.println("TL = " + rectangle.TL.lat + ", " + rectangle.TL.lon);
        System.out.println("TR = " + rectangle.TR.lat + ", " + rectangle.TR.lon);

        ArrayList<Point> candidates = new ArrayList<>();
        for (Point p : chargers) {
            System.out.println("Checking point: " + p.name);
            if (inRectangle(rectangle, p)) candidates.add(p);
        }
        return candidates;
    }

    public static void main(String[] args) {
        Point c1 = new Point(); c1.name = "Estació del Nord"; c1.lat = 41.659853; c1.lon = 2.015975;
        Point c2 = new Point(); c2.name = "Estació de Sans"; c2.lat = 41.379805; c2.lon = 2.141224;
        Point c3 = new Point(); c3.name = "Cambrils"; c3.lat = 41.069389; c3.lon = 1.087369;
        Point c4 = new Point(); c4.name = "Buena opcion"; c4.lat = 41.33185; c4.lon = 1.680507;
        Point c5 = new Point(); c5.name = "Mala opcion"; c5.lat = 41.229957; c5.lon = 1.681126;
        Point c6 = new Point(); c6.name = "Girona"; c6.lat = 41.069389; c6.lon = 1.08736;
        Point c7 = new Point(); c7.name = "Andorra"; c7.lat = 42.497339; c7.lon = 1.507169;
        Point c8 = new Point(); c8.name = "Lleida"; c8.lat = 41.608993; c8.lon = 0.620819;

        Point[] list = new Point[]{c1, c2, c3, c4, c5, c6, c7, c8};
        chargers = new ArrayList<>(List.of(list));

        Point ori = new Point(); ori.name = "Lloret de Mar"; ori.lat = 41.700801; ori.lon = 2.847613;
        Point dest = new Point(); dest.name = "Amposta"; dest.lat = 40.705865; dest.lon = 0.578646;

        range = 150;

        double distance = calculateRawDistance(ori.lon, ori.lat, dest.lon, dest.lat);
        System.out.println("Distance: " + String.format("%.02f", distance));

        stopsNeeded = (int) (distance/range);
        System.out.println("Stops needed: " + stopsNeeded);

        double roadDistance = calculateRoadDistance();
        System.out.println("Road distance: " + roadDistance/1000 + " kilometers");

        //List of chargers to consider when computing route
        ArrayList<Point> candidates = selectPossibleChargers(chargers, ori, dest);

        /*
            todo pedir a google la matriz de distancia por carretera entre todos los chargers y ori y dest
            Dependiendo del numero de paradas, coger de la matriz las combinacions de origen-chargers-destino
            que sean viables (autonomia y kilometros entre puntos) y de estas coger la mas corta en km
        */
    }
}

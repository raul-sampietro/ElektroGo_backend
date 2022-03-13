package elektroGo.back.controller;

class Point {
    String name;
    Double lat;
    Double lon;
}

public class RouteCalculation {
    static Point[] chargers;
    static int stopsNeeded;
    static int range; //in kilometers


    static double calculateDistance(Double x1, Double y1, Double x2, Double y2) {
        //TODO Calcular distancia por carretera
        var p = Math.PI / 180;
        var a = 0.5 - Math.cos((x2 - x1) * p)/2 +
                Math.cos(x1 * p) * Math.cos(x2 * p) *
                        (1 - Math.cos((y2 - y1) * p))/2;
        return 2 * 6371 * Math.asin(Math.sqrt(a)); // Radio tierra = 6371 km
    }

    public static void main(String[] args) {
        Point c1 = new Point(); c1.name = "Estació del Nord"; c1.lat = 41.659853; c1.lon = 2.015975;

        Point c2 = new Point(); c2.name = "Estació de Sans"; c2.lat = 41.379805; c2.lon = 2.141224;

        Point c3 = new Point(); c3.name = "Cambrils"; c3.lat = 41.069389; c3.lon = 1.087369;

        Point c4 = new Point(); c4.name = "Buena opcion"; c4.lat = 41.33185; c4.lon = 1.680507;

        Point c5 = new Point(); c5.name = "Mala opcion"; c5.lat = 41.229957; c5.lon = 1.681126;

        chargers = new Point[]{c1, c2, c3, c4, c5};

        Point ori = new Point(); ori.name = "Lloret de Mar"; ori.lat = 41.700801; ori.lon = 2.847613;

        Point dest = new Point(); dest.name = "Amposta"; dest.lat = 40.705865; dest.lon = 0.578646;

        range = 150;

        double distance = calculateDistance(ori.lat, ori.lon, dest.lat, dest.lon);
        System.out.println("Distance: " + distance);

        stopsNeeded = (int) (distance/range);
        System.out.println("Stops needed: " + stopsNeeded);

        /* TODO Añadir dependencia al .xml
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/distancematrix/json?origins=40.6655101%2C-73.89188969999998&destinations=40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626&key=YOUR_API_KEY")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        */
    }
}

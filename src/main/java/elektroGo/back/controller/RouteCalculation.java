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


    static double calculateRawDistance(Double x1, Double y1, Double x2, Double y2) {
        double p = Math.PI / 180;
        double a = 0.5 - Math.cos((x2 - x1) * p)/2 +
                Math.cos(x1 * p) * Math.cos(x2 * p) *
                        (1 - Math.cos((y2 - y1) * p))/2;
        return 2 * 6371 * Math.asin(Math.sqrt(a)); // Radio tierra = 6371 km
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

    static Point[] selectPossibleChargers(Point[] chargers, Point ori, Point dest) {


        return null;
    }

    public static void main(String[] args) {
        Point c1 = new Point(); c1.name = "Estació del Nord"; c1.lat = 41.659853; c1.lon = 2.015975;
        Point c2 = new Point(); c2.name = "Estació de Sans"; c2.lat = 41.379805; c2.lon = 2.141224;
        Point c3 = new Point(); c3.name = "Cambrils"; c3.lat = 41.069389; c3.lon = 1.087369;
        Point c4 = new Point(); c4.name = "Buena opcion"; c4.lat = 41.33185; c4.lon = 1.680507;
        Point c5 = new Point(); c5.name = "Mala opcion"; c5.lat = 41.229957; c5.lon = 1.681126;
        Point c6 = new Point(); c6.name = "Girona"; c6.lat = 41.069389; c6.lon = 1.08736;
        Point c7 = new Point(); c7.name = "Andorra"; c7.lat = 42.497339; c7.lon = 1.507169;
        Point c8 = new Point(); c8.name = "lleida"; c8.lat = 41.608993; c8.lon = 0.620819;

        chargers = new Point[]{c1, c2, c3, c4, c5, c6, c7, c8};
        Point ori = new Point(); ori.name = "Lloret de Mar"; ori.lat = 41.700801; ori.lon = 2.847613;
        Point dest = new Point(); dest.name = "Amposta"; dest.lat = 40.705865; dest.lon = 0.578646;

        range = 150;
        double distance = calculateRawDistance(ori.lat, ori.lon, dest.lat, dest.lon);
        System.out.println("Distance: " + distance);

        stopsNeeded = (int) (distance/range);
        System.out.println("Stops needed: " + stopsNeeded);

        double roadDistance = calculateRoadDistance();
        System.out.println("Road distance: " + roadDistance);

        Point[] candidates = selectPossibleChargers(chargers, ori, dest);

    }
}

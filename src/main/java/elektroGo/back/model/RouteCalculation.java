package elektroGo.back.model;

import java.util.ArrayList;
import java.util.List;

class Rectangle {
    Point BL;
    Point BR;
    Point TL;
    Point TR;
}

public class RouteCalculation {
    private ArrayList<Point> chargers;
    private final int range; //in kilometers
    private final Point origin;
    private final Point destination;
    private ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
    private ArrayList<Point> candidates = new ArrayList<>();
    private ArrayList<Point> definitive = new ArrayList<>();
    private Integer minDistance;
    private final DistanceCalculator distanceCalculator = new DistanceCalculator();

    // todo HACER TEST UNITARIOS

    public RouteCalculation(int range, Double oriLat, Double oriLon, Double destLat, Double destLon) {
        Point origin = new Point(oriLat, oriLat);
        Point destination = new Point(destLat, destLon);
        this.origin = origin;
        this.destination = destination;
        this.range = range * 1000;
        getAllChargers();
        selectPossibleChargers();
    }

    // todo EN SU MOMENTO UTILIZAR LOS DATOS DE LA BD
    // post: chargers contiene todos los cargadores de que hay en la base de datos
    private void getAllChargers() {
        Point c1 = new Point("Terrassa", 41.659853, 2.015975);
        Point c2 = new Point("Barcelona", 41.379805, 2.141224);
        Point c3 = new Point("Salou", 41.069389, 1.087369);
        Point c4 = new Point("Vic", 41.914522, 2.262188);
        Point c5 = new Point("Manresa", 41.733242, 1.845603);
        Point c6 = new Point("Girona", 41.973401, 2.820924);
        Point c7 = new Point("Andorra", 42.497339, 1.507169);
        Point c8 = new Point("Lleida", 41.608993, 0.620819);

        Point[] list = new Point[]{c1, c2, c3, c4, c5, c6, c7, c8};
        chargers = new ArrayList<>(List.of(list));
    }
    
    // Returns true if the point is inside the rectangle, returns false if not.
    private boolean inRectangle(Rectangle rectangle, Point point) {
        return point.getLatitude() <= rectangle.TR.getLatitude() && point.getLatitude() >= rectangle.BL.getLatitude() &&
                point.getLongitude() <= rectangle.BR.getLongitude() && point.getLongitude() >= rectangle.BL.getLongitude();
    }

    private Rectangle buildRectangle(Point ori, Point dest) {
        Rectangle rectangle = new Rectangle();
        //Offset de 0.065 añadido para asegurar una area minima de busqueda
        rectangle.BL = new Point(Math.min(ori.getLatitude(), dest.getLatitude()) - 0.065, Math.min(ori.getLongitude(), dest.getLongitude()) - 0.065);
        rectangle.BR = new Point(Math.min(ori.getLatitude(), dest.getLatitude()) - 0.065, Math.min(ori.getLongitude(), dest.getLongitude()) + 0.065);
        rectangle.TR = new Point(Math.min(ori.getLatitude(), dest.getLatitude()) + 0.065, Math.min(ori.getLongitude(), dest.getLongitude()) + 0.065);
        rectangle.TL = new Point(Math.min(ori.getLatitude(), dest.getLatitude()) + 0.065, Math.min(ori.getLongitude(), dest.getLongitude()) - 0.065);
        return rectangle;
    }

    // post: candidates contiene los chargers candidatos a pertenecer a la ruta entre origen y destino
    private void selectPossibleChargers() {
        //Set rectangle area where chargers should be found
        Rectangle rectangle = buildRectangle(origin, destination);
        for (Point p : chargers) {
            if (inRectangle(rectangle, p)) candidates.add(p);
        }
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
        matrix = distanceCalculator.calculateRoadDistanceMatrix(origin, destination, candidates);
        minDistance = Integer.MAX_VALUE;
        ArrayList<Point> temporal = new ArrayList<>();
        backtrack(0, 0, temporal);
    }

    //post: returns the definitive arrayList
    public ArrayList<Point> getResult() {
        return definitive;
    }

}

package elektroGo.back;

import elektroGo.back.model.Point;
import elektroGo.back.model.RouteCalculation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class RouteCalculationTest {

    @Test
    public void routeWithoutStops() {
        // For the given database of charging stations
        int range = 150;
        Double oriLat = 41.671230;
        Double oriLon = 1.271296;

        Double destLat = 41.929573;
        Double destLon = 2.249060;

        String expected = "";

        RouteCalculation routeCalculation = new RouteCalculation(range, oriLat, oriLon, destLat, destLon);
        if (routeCalculation.checkAPIKey()) {
            routeCalculation.execute();
            ArrayList<Double> actualList = routeCalculation.getResult();
            String actual = "";
            for (int i = 0; i < actualList.size() - 1; i+= 2) {
                actual += actualList.get(i) + " " + actualList.get(i+1);
            }
            assertEquals(expected, actual);
        }
        else assertNull(null);
    }

    @Test
    public void routeWithOneStop() {
        // For the given database of charging stations
        int range = 85;
        Double oriLat = 41.671230;
        Double oriLon = 1.271296;

        Double destLat = 41.929573;
        Double destLon = 2.249060;

        Point expected1 = new Point(41.733242, 1.845603);
        Point[] expectedList = new Point[]{expected1};
        String expected = "";
        for (Point p : expectedList) {
            expected += p.getLatitude() + " " + p.getLongitude();
        }

        RouteCalculation routeCalculation = new RouteCalculation(range, oriLat, oriLon, destLat, destLon);
        if (routeCalculation.checkAPIKey()) {
            routeCalculation.execute();
            ArrayList<Double> actualList = routeCalculation.getResult();
            String actual = "";
            for (int i = 0; i < actualList.size() - 1; i+= 2) {
                actual += actualList.get(i) + " " + actualList.get(i+1);
            }
            assertEquals(expected, actual);
        }
        else assertNull(null);
    }

    @Test
    public void routeWithSeveralStops() {
        // For the given database of charging stations
        int range = 150;
        Double oriLat = 41.700801;
        Double oriLon = 2.847613;

        Double destLat = 40.705865;
        Double destLon = 0.578646;

        Point expected1 = new Point(41.379805, 2.141224);
        Point expected2 = new Point(41.069389, 1.087369);
        Point[] expectedList = new Point[]{expected1, expected2};
        String expected = "";
        for (Point p : expectedList) {
            expected += p.getLatitude() + " " + p.getLongitude();
        }

        RouteCalculation routeCalculation = new RouteCalculation(range, oriLat, oriLon, destLat, destLon);
        if (routeCalculation.checkAPIKey()) {
            routeCalculation.execute();
            ArrayList<Double> actualList = routeCalculation.getResult();
            String actual = "";
            for (int i = 0; i < actualList.size() - 1; i+= 2) {
                actual += actualList.get(i) + " " + actualList.get(i+1);
            }
            assertEquals(expected, actual);
        }
        else assertNull(null);
    }
}
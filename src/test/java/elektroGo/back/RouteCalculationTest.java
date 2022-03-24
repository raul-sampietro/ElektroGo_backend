package elektroGo.back;

import elektroGo.back.model.Point;
import elektroGo.back.model.RouteCalculation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
public class RouteCalculationTest {

    @Test
    public void routeWithoutStops() {

    }

    @Test
    public void routeWithOneStop() {

    }

    @Test
    public void routeWithSeveralStops() {
        // For the given database of charging stations
        int range = 150;
        Double oriLat =  41.700801;
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
        routeCalculation.execute();
        Point[] actualList = routeCalculation.getResult();
        String actual = "";
        for (Point p : actualList) {
            actual += p.getLatitude() + " " + p.getLongitude();
        }
        assertEquals(expected, actual);
    }
}
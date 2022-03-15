package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.Finders.FinderChargingStations;
import elektroGo.back.data.Gateways.GatewayChargingStations;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class ChargingStationsTest {
    @Test
    public void createChargingStationsTest() throws SQLException {
        BigDecimal latitude = BigDecimal.valueOf(41.389256);
        BigDecimal longitude = BigDecimal.valueOf(2.113442);
        GatewayChargingStations gCS = new GatewayChargingStations(2, latitude, longitude,3);
        Database d = Database.getInstance();
        gCS.insert();
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        GatewayChargingStations gCSTest = fCS.findByID(2);
        String res = gCSTest.getId() + " " + gCSTest.getLatitude() + " " + gCSTest.getLongitude() + " " + gCSTest.getNumberOfChargers();
        d.executeSQLUpdate("delete from CHARGINGSTATIONS where id = 2;");
        assertEquals("2 41.389256 2.113442 3", res);
    }

    private GatewayChargingStations insertTestChargingStation() throws SQLException {
        BigDecimal latitude = BigDecimal.valueOf(41.389256);
        BigDecimal longitude = BigDecimal.valueOf(2.113442);
        GatewayChargingStations gCS = new GatewayChargingStations(2, latitude, longitude,3);
        gCS.insert();
        return gCS;
    }

    @Test
    public void updateChargingStationsTest() throws SQLException {
        insertTestChargingStation();
        Database d = Database.getInstance();
        try {
            FinderChargingStations fCS = FinderChargingStations.getInstance();
            GatewayChargingStations gCS = fCS.findByID(2);
            gCS.setNumberOfChargers(5);
            gCS.update();
            gCS = fCS.findByID(2);
            String res = gCS.getId() + " " + gCS.getLatitude() + " " + gCS.getLongitude() + " " + gCS.getNumberOfChargers();
            assertEquals("2 41.389256 2.113442 5", res);
        }
        catch (SQLException s) {
            s.printStackTrace();
        }
        d.executeSQLUpdate("delete from CHARGINGSTATIONS where id = 2;");
    }

    @Test
    public void deleteChargingStationsTest() throws SQLException {
        GatewayChargingStations gCS =  insertTestChargingStation();
        Database d = Database.getInstance();
        gCS.remove();
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        GatewayChargingStations gVEmpty = fCS.findByID(2);
        assertNull(gVEmpty);
    }

    @Test
    public void readVehicleTest() throws SQLException {
        Database d = Database.getInstance();
        insertTestChargingStation();
        FinderChargingStations fV = FinderChargingStations.getInstance();
        GatewayChargingStations gCS = fV.findByID(2);
        String res =  gCS.getId() + " " + gCS.getLatitude() + " " + gCS.getLongitude() + " " + gCS.getNumberOfChargers();
        d.executeSQLUpdate("delete from CHARGINGSTATIONS where id = 2;");
        assertEquals("2 41.389256 2.113442 3", res);
    }

}

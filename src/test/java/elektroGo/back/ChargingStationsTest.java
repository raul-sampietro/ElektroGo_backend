package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.Finders.FinderChargingStations;
import elektroGo.back.data.Finders.FinderVehicle;
import elektroGo.back.data.Gateways.GatewayChargingStations;
import elektroGo.back.data.Gateways.GatewayVehicle;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private GatewayVehicle insertTestVehicle() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle(99821736, "testBrand", "testModel", "7869",
                666, LocalDate.of(2010, 3, 22), 3, "idTest",
                "Test");
        gV.insert();
        return gV;
    }
}

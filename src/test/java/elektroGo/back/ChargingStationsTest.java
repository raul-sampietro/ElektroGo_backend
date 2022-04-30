package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderChargingStations;
import elektroGo.back.data.gateways.GatewayChargingStations;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class ChargingStationsTest {

    @Test
    public void createChargingStationsTest() throws SQLException {
        BigDecimal latitude = BigDecimal.valueOf(41.389256);
        BigDecimal longitude = BigDecimal.valueOf(2.113442);
        GatewayChargingStations gCS = new GatewayChargingStations(999, "UPC", "públic", "ràpid", "Tesla", latitude, longitude, "parking UPC",
                15.0, "AC", "ID", "5", "Cotxe");
        Database d = Database.getInstance();
        gCS.insert();
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        GatewayChargingStations gCSTest = fCS.findByID(999);
        String res = gCSTest.getId() + " " + gCSTest.getPromotor_gestor() + " " + gCSTest.getAcces() + " " + gCSTest.getTipusVelocitat() + " "
                + gCSTest.getTipusConnexio() + " " + gCSTest.getLatitude() + " " + gCSTest.getLongitude() + " " + gCSTest.getDesignacioDescriptiva() + " "
                + gCSTest.getKw() + " " + gCSTest.getAcDc() + " " + gCSTest.getIdePdr() + " " + gCSTest.getNumberOfChargers() + " " + gCSTest.getTipusVehicle();
        d.executeSQLUpdate("delete from CHARGINGSTATIONS where id = 999;");
        assertEquals("999 UPC públic ràpid Tesla 41.38925600 2.11344200 parking UPC 15.0 AC ID 5 Cotxe", res);
    }

    private GatewayChargingStations insertTestChargingStation() throws SQLException {
        BigDecimal latitude = BigDecimal.valueOf(41.389256);
        BigDecimal longitude = BigDecimal.valueOf(2.113442);
        GatewayChargingStations gCS = new GatewayChargingStations(999, "UPC", "públic", "ràpid", "Tesla", latitude, longitude, "parking UPC",
                15.0, "AC", "ID", "5", "Cotxe");
        gCS.insert();
        return gCS;
    }

    @Test
    public void updateChargingStationsTest() throws SQLException {
        insertTestChargingStation();
        Database d = Database.getInstance();
        try {
            FinderChargingStations fCS = FinderChargingStations.getInstance();
            GatewayChargingStations gCS = fCS.findByID(999);
            gCS.setNumberOfChargers("4");
            gCS.setPromotor_gestor("FIB");
            gCS.setAcces("Privat");
            gCS.update();
            gCS = fCS.findByID(999);
            String res = gCS.getId() + " " + gCS.getPromotor_gestor() + " " + gCS.getAcces() + " " + gCS.getTipusVelocitat() + " "
                    + gCS.getTipusConnexio() + " " + gCS.getLatitude() + " " + gCS.getLongitude() + " " + gCS.getDesignacioDescriptiva() + " "
                    + gCS.getKw() + " " + gCS.getAcDc() + " " + gCS.getIdePdr() + " " + gCS.getNumberOfChargers() + " " + gCS.getTipusVehicle();
            assertEquals("999 FIB Privat ràpid Tesla 41.38925600 2.11344200 parking UPC 15.0 AC ID 4 Cotxe", res);
        }
        catch (SQLException s) {
            s.printStackTrace();
        }
        d.executeSQLUpdate("delete from CHARGINGSTATIONS where id = 999;");

    }

    @Test
    public void deleteChargingStationsTest() throws SQLException {
        GatewayChargingStations gCS =  insertTestChargingStation();
        Database d = Database.getInstance();
        gCS.remove();
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        GatewayChargingStations gVEmpty = fCS.findByID(999);
        assertNull(gVEmpty);
    }

    @Test
    public void readVehicleTest() throws SQLException {
        Database d = Database.getInstance();
        insertTestChargingStation();
        FinderChargingStations fV = FinderChargingStations.getInstance();
        GatewayChargingStations gCS = fV.findByID(999);
        String res =  gCS.getId() + " " + gCS.getPromotor_gestor() + " " + gCS.getAcces() + " " + gCS.getTipusVelocitat() + " "
                + gCS.getTipusConnexio() + " " + gCS.getLatitude() + " " + gCS.getLongitude() + " " + gCS.getDesignacioDescriptiva() + " "
                + gCS.getKw() + " " + gCS.getAcDc() + " " + gCS.getIdePdr() + " " + gCS.getNumberOfChargers() + " " + gCS.getTipusVehicle();
        d.executeSQLUpdate("delete from CHARGINGSTATIONS where id = 999;");
        assertEquals("999 UPC públic ràpid Tesla 41.38925600 2.11344200 parking UPC 15.0 AC ID 5 Cotxe", res);
    }

    @Test
    public void FindByCoordinates() throws SQLException {
        BigDecimal latitude = BigDecimal.valueOf(41.389256);
        BigDecimal longitude = BigDecimal.valueOf(2.113442);
        GatewayChargingStations gCS = new GatewayChargingStations(999, "UPC", "públic", "ràpid", "Tesla", latitude, longitude, "parking UPC",
                15.0, "AC", "ID", "5", "Cotxe");
        Database d = Database.getInstance();
        gCS.insert();
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        BigDecimal latitude1 = BigDecimal.valueOf(48.389256);
        BigDecimal longitude1 = BigDecimal.valueOf(6.113442);
        BigDecimal latitude2 = BigDecimal.valueOf(39.389256);
        BigDecimal longitude2 = BigDecimal.valueOf(1.113442);
        ArrayList<GatewayChargingStations> gCSTest = fCS.findByCoordinates(latitude1, longitude1, latitude2, longitude2);
        String res = null;
        for (GatewayChargingStations test: gCSTest) {
            res = test.getId() + " " + test.getPromotor_gestor() + " " + test.getAcces() + " " + test.getTipusVelocitat() + " "
                    + test.getTipusConnexio() + " " + test.getLatitude() + " " + test.getLongitude() + " " + test.getDesignacioDescriptiva() + " "
                    + test.getKw() + " " + test.getAcDc() + " " + test.getIdePdr() + " " + test.getNumberOfChargers() + " " + test.getTipusVehicle();
        }
        d.executeSQLUpdate("delete from CHARGINGSTATIONS where id = 999;");
        assertEquals("999 UPC públic ràpid Tesla 41.38925600 2.11344200 parking UPC 15.0 AC ID 5 Cotxe", res);
    }

}

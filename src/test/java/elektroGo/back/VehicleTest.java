package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.Finders.FinderVehicle;
import elektroGo.back.data.Gateways.GatewayVehicle;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VehicleTest {

    private GatewayVehicle insertTestVehicleComplet() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle("testBrand", "testModel", "7869",
                666, 2010, 3, "idTest");
        gV.insert();
        return gV;
    }

    @Test
    public void createVehicleTest() throws SQLException {
        Database d = Database.getInstance();
        GatewayVehicle gV =  insertTestVehicleComplet();
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gVTest = fV.findByNumberPlate(gV.getNumberPlate());
        String res = gVTest.getBrand() + " " + gVTest.getModel() + " " + gVTest.getNumberPlate() +
                " "+ gVTest.getDrivingRange() + " " + gVTest.getFabricationYear() + " " + gVTest.getImageId();
        gV.remove();
        assertEquals("testBrand testModel 7869 666 2010 idTest", res);
    }

    private GatewayVehicle insertTestVehicle() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle("testBrand", "testModel", "7869",
                666, 2010, 3);
        gV.insert();
        return gV;
    }

    @Test
    public void createVehicleTest2() throws SQLException {
        Database d = Database.getInstance();
        GatewayVehicle gV =  insertTestVehicle();
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gVTest = fV.findByNumberPlate(gV.getNumberPlate());
        String res = gVTest.getBrand() + " " + gVTest.getModel() + " " + gVTest.getNumberPlate() +
                " "+ gVTest.getDrivingRange() + " " + gVTest.getFabricationYear() + " " + gVTest.getImageId();
        gV.remove();
        assertEquals("testBrand testModel 7869 666 2010 null", res);
    }


    @Test
    public void updateVehicle() throws SQLException {
        GatewayVehicle gVIns = insertTestVehicleComplet();
        try {
            FinderVehicle fV = FinderVehicle.getInstance();
            GatewayVehicle gV = fV.findByNumberPlate(gVIns.getNumberPlate());
            gV.setDrivingRange(300);
            gV.update();
            gV = fV.findByNumberPlate(gVIns.getNumberPlate());
            String res = gV.getBrand() + " " + gV.getModel() + " " + gV.getNumberPlate() +
                    " " + gV.getDrivingRange() + " " + gV.getFabricationYear() + " " + gV.getImageId();
            assertEquals("testBrand testModel 7869 300 2010 idTest", res);
        }
        catch (SQLException s) {
            s.printStackTrace();
        }
        gVIns.remove();
    }

    @Test
    public void deleteVehicleTest() throws SQLException {
        GatewayVehicle gV =  insertTestVehicle();
        Database d = Database.getInstance();
        gV.remove();
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gVEmpty = fV.findByNumberPlate("7869");
        assertNull(gVEmpty);
    }
}

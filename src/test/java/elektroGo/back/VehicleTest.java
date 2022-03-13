package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.Finders.FinderVehicle;
import elektroGo.back.data.Gateways.GatewayVehicle;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VehicleTest {

    @Test
    public void createVehicleTest1() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle(99821736,"testModel","5755","Test");
        Database d = Database.getInstance();
        gV.insert();
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gVTest = fV.findByID(99821736);
        String res = gVTest.getId() + " " + gVTest.getModel() + " " + gVTest.getNumberPlate() + " " + gVTest.getuserName();
        d.executeSQLUpdate("delete from VEHICLE where id = 99821736;");
        assertEquals("99821736 testModel 5755 Test", res);
    }

    private GatewayVehicle insertTestVehicle() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle(99821736, "testBrand", "testModel", "7869",
                666, LocalDate.of(2010, 3, 22), 3, "idTest",
                "Test");
        gV.insert();
        return gV;
    }

    @Test
    public void createVehicleTest2() throws SQLException {
        Database d = Database.getInstance();
        insertTestVehicle();
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gVTest = fV.findByID(99821736);
        String res = gVTest.getId() + " " + gVTest.getBrand() + " " + gVTest.getModel() + " " + gVTest.getNumberPlate() +
                " "+ gVTest.getDrivingRange() + " " + gVTest.getFabricationYear() + " " + gVTest.getImageId() + " " + gVTest.getuserName();
        d.executeSQLUpdate("delete from VEHICLE where id = 99821736;");
        assertEquals("99821736 testBrand testModel 7869 666 2010-03-22 idTest Test", res);
    }

    @Test
    public void updateVehicle() throws SQLException {
        insertTestVehicle();
        Database d = Database.getInstance();
        try {
            FinderVehicle fV = FinderVehicle.getInstance();
            GatewayVehicle gV = fV.findByID(99821736);
            gV.setDrivingRange(300);
            gV.update();
            gV = fV.findByID(99821736);
            String res = gV.getId() + " " + gV.getBrand() + " " + gV.getModel() + " " + gV.getNumberPlate() +
                    " " + gV.getDrivingRange() + " " + gV.getFabricationYear() + " " + gV.getImageId() + " " + gV.getuserName();
            assertEquals("99821736 testBrand testModel 7869 300 2010-03-22 idTest Test", res);
        }
        catch (SQLException s) {
            s.printStackTrace();
        }
        d.executeSQLUpdate("delete from VEHICLE where id = 99821736;");
    }

    @Test
    public void deleteVehicleTest() throws SQLException {
        GatewayVehicle gV =  insertTestVehicle();
        Database d = Database.getInstance();
        gV.remove();
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gVEmpty = fV.findByID(99821736);
        assertNull(gVEmpty);
    }

    @Test
    public void readVehicleTest() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("insert into VEHICLE values(22,null,'testModel','5755',null,null,null,null,'Test');");
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gVTest = fV.findByID(22);
        String res = gVTest.getId() + " " + gVTest.getModel() + " " + gVTest.getNumberPlate() + " " + gVTest.getuserName();
        d.executeSQLUpdate("delete from VEHICLE where id = 22;");
        assertEquals("22 testModel 5755 Test", res);
    }
}

package elektroGo.back;


import elektroGo.back.data.finders.FinderVehicle;
import elektroGo.back.data.gateways.GatewayVehicle;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VehicleTest {

    FinderVehicle fV;
    GatewayVehicle gV;
    GatewayVehicle gVC;


    @BeforeEach
    private void initialize() throws SQLException {
        fV = FinderVehicle.getInstance();
        gV = insertTestVehicle();
        gVC = insertTestVehicleComplet();
    }

    @AfterEach
    private void remove() throws SQLException {
        if (gV != null) gV.remove();
        gVC.remove();
    }

    private GatewayVehicle insertTestVehicleComplet() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle("testBrand", "testModel", "7869",
                666, 2010, 3, "idTest");
        gV.insert();
        return gV;
    }

    private GatewayVehicle insertTestVehicle() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle("testBrand", "testModel", "7870",
                666, 2010, 3);
        gV.insert();
        return gV;
    }

    @Test
    public void createVehicleTest() throws SQLException {
        GatewayVehicle gVTest = fV.findByNumberPlate(gVC.getNumberPlate());
        assertEquals(gVC.json(), gVTest.json());
    }

    @Test
    public void createVehicleTest2() throws SQLException {
        GatewayVehicle gVTest = fV.findByNumberPlate(gV.getNumberPlate());
        assertEquals(gV.json(), gVTest.json());
    }


    @Test
    public void updateVehicle() throws SQLException {
        gV.setDrivingRange(300);
        gV.update();
        GatewayVehicle gVTest = fV.findByNumberPlate(gV.getNumberPlate());
        assertEquals(gV.json(), gVTest.json());
    }

    @Test
    public void deleteVehicleTest() throws SQLException {
        gV.remove();
        GatewayVehicle gVEmpty = fV.findByNumberPlate(gV.getNumberPlate());
        gV = null;
        assertNull(gVEmpty);
    }
}

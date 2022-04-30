package elektroGo.back;

import elektroGo.back.data.finders.FinderDriverVehicle;
import elektroGo.back.data.finders.FinderVehicle;
import elektroGo.back.data.gateways.GatewayDriverVehicle;
import elektroGo.back.data.gateways.GatewayVehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DriverVehicleTest {

    boolean insertVehicle;
    boolean insertVehicle2;

    @BeforeEach
    private void initialize() {
        insertVehicle = false;
        insertVehicle2 = false;
    }

    private void insertVehicle() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle("testBrand", "testModel", "VTestDriverVehicleTest",
                666, 2010, 3);
        gV.insert();
        insertVehicle = true;
    }

    private void insertVehicle2() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle("testBrand", "testModel", "VTestDriverVehicleTest2",
                666, 2010, 3);
        gV.insert();
        insertVehicle2 = true;
    }

    @AfterEach
    private void removeVehicles() throws SQLException {
        FinderVehicle fV = FinderVehicle.getInstance();
        if (insertVehicle) {
            GatewayVehicle gV = fV.findByNumberPlate("VTestDriverVehicleTest");
            gV.remove();
        }
        if (insertVehicle2) {
            GatewayVehicle gV = fV.findByNumberPlate("VTestDriverVehicleTest2");
            gV.remove();
        }
    }

    private GatewayDriverVehicle insertDriverVehicle() throws SQLException {
        FinderVehicle fV = FinderVehicle.getInstance();
        if (fV.findByNumberPlate("TestV") == null) insertVehicle();
        GatewayDriverVehicle gDV = new GatewayDriverVehicle("VTestDriverVehicleTest","TestDriverVehicle");
        gDV.insert();
        return gDV;
    }

    private ArrayList<GatewayDriverVehicle> insertDriverVehicle2() throws SQLException {
        FinderVehicle fV = FinderVehicle.getInstance();
        if (fV.findByNumberPlate("VTestDriverVehicleTest") == null) insertVehicle();
        if (fV.findByNumberPlate("VTestDriverVehicleTest2") == null) insertVehicle2();
        GatewayDriverVehicle gDV = new GatewayDriverVehicle("VTestDriverVehicleTest","TestDriverVehicle");
        GatewayDriverVehicle gDV2 = new GatewayDriverVehicle("VTestDriverVehicleTest2","TestDriverVehicle");
        gDV2.insert();
        gDV.insert();
        ArrayList<GatewayDriverVehicle> aL= new ArrayList<>();
        aL.add(gDV);
        aL.add(gDV2);
        return aL;
    }

    @Test
    public void createAndRemoveDriverVehicle() throws SQLException {
        GatewayDriverVehicle gDV =  insertDriverVehicle();
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        GatewayDriverVehicle gDVTest = fDV.findByNumberPlateDriver(gDV.getUserDriver(), gDV.getnPVehicle());
        assertNotNull(gDVTest);
        assertEquals(gDV.json(), gDVTest.json());
        assertEquals(gDV.json(), gDVTest.json());
        gDV.remove();
    }

    @Test
    public void createAndRemove2DriverVehicle() throws SQLException {
        ArrayList<GatewayDriverVehicle> aL =  insertDriverVehicle2();
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        assertEquals(1, fDV.findByNumberPlateV("VTestDriverVehicleTest").size());
        assertEquals(1, fDV.findByNumberPlateV("VTestDriverVehicleTest2").size());
        assertEquals(2, fDV.findByUserDriver("TestDriverVehicle").size());
        for (GatewayDriverVehicle gDV : aL) gDV.remove();

    }

}

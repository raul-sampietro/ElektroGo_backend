package elektroGo.back;

import elektroGo.back.data.finders.FinderDriverVehicle;
import elektroGo.back.data.finders.FinderVehicle;
import elektroGo.back.data.gateways.GatewayDriverVehicle;
import elektroGo.back.data.gateways.GatewayVehicle;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DriverVehicleTest {

    private void insertVehicle() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle("testBrand", "testModel", "TestV",
                666, 2010, 3);
        gV.insert();
    }

    private void insertVehicle2() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle("testBrand", "testModel", "TestV2",
                666, 2010, 3);
        gV.insert();
    }

    private GatewayDriverVehicle insertDriverVehicle() throws SQLException {
        FinderVehicle fV = FinderVehicle.getInstance();
        if (fV.findByNumberPlate("TestV") == null) insertVehicle();
        GatewayDriverVehicle gDV = new GatewayDriverVehicle("TestV","Test");
        gDV.insert();
        return gDV;
    }

    private ArrayList<GatewayDriverVehicle> insertDriverVehicle2() throws SQLException {
        FinderVehicle fV = FinderVehicle.getInstance();
        if (fV.findByNumberPlate("TestV") == null) insertVehicle();
        if (fV.findByNumberPlate("TestV2") == null) insertVehicle2();
        GatewayDriverVehicle gDV = new GatewayDriverVehicle("TestV","Test");
        GatewayDriverVehicle gDV2 = new GatewayDriverVehicle("TestV2","Test");
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
        assertEquals(gDV.getnPVehicle(), gDVTest.getnPVehicle());
        assertEquals(gDV.getUserDriver(), gDVTest.getUserDriver());
        gDV.remove();
    }

    @Test
    public void createAndRemove2DriverVehicle() throws SQLException {
        ArrayList<GatewayDriverVehicle> aL =  insertDriverVehicle2();
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        assertEquals(1, fDV.findByNumberPlateV("TestV").size());
        assertEquals(1, fDV.findByNumberPlateV("TestV2").size());
        assertEquals(2, fDV.findByUserDriver("Test").size());
        for (GatewayDriverVehicle gDV : aL) gDV.remove();

    }

}

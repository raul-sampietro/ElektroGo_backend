package elektroGo.back;

import elektroGo.back.data.finders.FinderDriverVehicle;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.finders.FinderVehicle;
import elektroGo.back.data.gateways.GatewayDriver;
import elektroGo.back.data.gateways.GatewayDriverVehicle;
import elektroGo.back.data.gateways.GatewayUser;
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

    FinderVehicle fV;
    FinderDriverVehicle fDV;
    GatewayVehicle gV1;
    GatewayVehicle gV2;

    GatewayDriverVehicle gDV1;
    ArrayList<GatewayDriverVehicle> AGDV2;
    GatewayUser gU1;
    GatewayDriver gD;


    @BeforeEach
    private void initialize() throws SQLException {
        fV = FinderVehicle.getInstance();
        fDV = FinderDriverVehicle.getInstance();
        createUsers();
        gV1 = insertVehicle();
        gV2 = insertVehicle2();
        gDV1 = insertDriverVehicle();
        AGDV2 = insertDriverVehicle2();

    }

    private void createUsers() throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        gU1 = fU.findByUsername("VTestDriverVehicleTest");
        if (gU1 == null) {
            gU1 = createUser("TestDriverVehicle");
            gD = new GatewayDriver(gU1.getUsername(),false);
            gU1.insert();
            gD.insert();
        }
    }

    private GatewayUser createUser(String username) {
        return new GatewayUser(username, "Test", username, "test@test.test",username, username, username+"family", "/test");
    }

    private GatewayVehicle insertVehicle() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle("testBrand", "testModel", "VTestDriverVehicleTest",
                666, 2010, 3);
        gV.insert();
        return gV;
    }

    private GatewayVehicle insertVehicle2() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle("testBrand", "testModel", "VTestDriverVehicleTest2",
                666, 2010, 3);
        gV.insert();
        return gV;
    }

    @AfterEach
    private void removeVehicles() throws SQLException {
        if (gDV1 != null ) gDV1.remove();
        if (AGDV2 != null) for(GatewayDriverVehicle gd : AGDV2) gd.remove();
        if (gU1 != null) gU1.remove();
        if (gV1 != null) gV1.remove();
        if (gV2 != null) gV2.remove();
        if (gD != null) gD.remove();
    }

    private GatewayDriverVehicle insertDriverVehicle() throws SQLException {
        GatewayDriverVehicle gDV = new GatewayDriverVehicle("VTestDriverVehicleTest","TestDriverVehicle");
        gDV.insert();
        return gDV;
    }

    private ArrayList<GatewayDriverVehicle> insertDriverVehicle2() throws SQLException {
        GatewayDriverVehicle gDV2 = new GatewayDriverVehicle("VTestDriverVehicleTest2","TestDriverVehicle");
        gDV2.insert();
        ArrayList<GatewayDriverVehicle> aL= new ArrayList<>();
        aL.add(gDV1);
        aL.add(gDV2);
        return aL;
    }

    @Test
    public void createAndRemoveDriverVehicle() throws SQLException {
        GatewayDriverVehicle gDVTest = fDV.findByNumberPlateDriver(gDV1.getUserDriver(), gDV1.getnPVehicle());
        assertNotNull(gDVTest);
        assertEquals(gDV1.json(), gDVTest.json());
        assertEquals(gDV1.json(), gDVTest.json());
    }

    @Test
    public void createAndRemove2DriverVehicle() throws SQLException {
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        assertEquals(1, fDV.findByNumberPlateV("VTestDriverVehicleTest").size());
        assertEquals(1, fDV.findByNumberPlateV("VTestDriverVehicleTest2").size());
        assertEquals(2, fDV.findByUserDriver("TestDriverVehicle").size());
    }

}

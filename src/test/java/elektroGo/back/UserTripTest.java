package elektroGo.back;

import elektroGo.back.data.finders.*;
import elektroGo.back.data.gateways.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTripTest {

    FinderUser fU;
    FinderDriver fD;
    GatewayUser gU;
    GatewayUser gU2;
    GatewayDriver gD;
    FinderVehicle fV;
    GatewayVehicle gV;
    FinderTrip fT;
    GatewayTrip gT;
    FinderUserTrip fUT;
    GatewayUserTrip gUT;
    GatewayUserTrip gUT2;

    //BEF & AFTER
    @BeforeEach
    public void initialize() throws SQLException {
        fU = FinderUser.getInstance();
        fD = FinderDriver.getInstance();
        gU = insertTest();
        gU2 = insertTest2();
        gD = new GatewayDriver("UserTestClass");
        gD.insert();
        fV = FinderVehicle.getInstance();
        gV = insertTestVehicleComplet();
        fT = FinderTrip.getInstance();
        gT = insertTestTrip();
        fUT = FinderUserTrip.getInstance();
        gT = fT.findByUser("UserTestClass",LocalDate.of(2001, 2, 12),new Time(12*60*60*1000));
        gUT = insertTestUserTrip(gT.getId());
        gUT2 = insertTestUserTrip2(gT.getId());
    }

    @AfterEach
    public void removeGateways() throws SQLException {
        if(fU.findByUsername(gU.getUsername()) != null)gU.remove();
        if(fU.findByUsername(gU2.getUsername()) != null)gU2.remove();
        if (gV != null) gV.remove();
    }

    //FUNCTIONS
    private GatewayUser insertTest() throws SQLException {
        gU = new GatewayUser("0","p","UserTestClass","t@gmail.com","T","T", "f", "/t");
        gU.insert();
        return gU;
    }
    private GatewayUser insertTest2() throws SQLException {
        gU2 = new GatewayUser("0","p","SecondTestClass","t@gmail.com","T","T", "f", "/t");
        gU2.insert();
        return gU2;
    }
    private GatewayTrip insertTestTrip() throws SQLException {
        GatewayTrip gT = new GatewayTrip(LocalDate.of(2001, 2, 12), new Time(12*60*60*1000),
                5,0, "null", "null",LocalDate.of(2001, 2,10),
                "1221","bcn" ,"gir",new BigDecimal("41.3000"),new BigDecimal("41.0000"), new BigDecimal("41.3000"),new BigDecimal("41.0000"), "UserTestClass");
        gT.insert();
        return gT;
    }
    private GatewayVehicle insertTestVehicleComplet() throws SQLException {
        GatewayVehicle gV = new GatewayVehicle("test", "test", "1221",
                600, 2010, 6, "id");
        gV.insert();
        return gV;
    }

    private GatewayUserTrip insertTestUserTrip(Integer id) throws SQLException {
        GatewayUserTrip gTt = new GatewayUserTrip(id, "UserTestClass");
        gTt.insert();
        return gTt;
    }

    private GatewayUserTrip insertTestUserTrip2(Integer id) throws SQLException {
        GatewayUserTrip gTt2 = new GatewayUserTrip(id, "SecondTestClass");
        gTt2.insert();
        return gTt2;
    }

    //TEST
    @Test
    public void createUserTrip() throws SQLException {
        GatewayUserTrip gTTest = fUT.findByTripUser(gT.getId(),"UserTestClass");
        String res = gTTest.getId() + " " + gTTest.getUsername();
        assertEquals(gT.getId() + " UserTestClass", res);
    }

    @Test
    public void FindByDrive() throws SQLException {
        ArrayList<GatewayUserTrip> array = fUT.findByDriver("UserTestClass");
        String res = array.get(0).getId() + " " +array.get(0).getUsername();
        assertEquals(gT.getId() + " UserTestClass",res);

        ArrayList<GatewayUserTrip> array2 = fUT.findByDriver("SecondTestClass");
        assertEquals(array2, new ArrayList<>());

    }

    @Test
    public void deleteTripTest() throws SQLException {
        gUT.remove();
        ArrayList<GatewayUserTrip> array = fUT.findByTrip(gT.getId());
        assertFalse(array.contains(gUT));
    }

}
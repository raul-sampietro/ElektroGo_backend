/**
 * @file DriverTest.java
 * @author Gerard Castell
 * @date 12/03/2023
 * @brief Implementació dels Tests DriverTest
 */
package elektroGo.back;
import elektroGo.back.data.finders.FinderDriver;
import elektroGo.back.data.finders.FinderTrip;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.finders.FinderVehicle;
import elektroGo.back.data.gateways.GatewayDriver;
import elektroGo.back.data.gateways.GatewayTrip;
import elektroGo.back.data.gateways.GatewayUser;
import elektroGo.back.data.gateways.GatewayVehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @brief La clase Trip Test es l'encarreaada de fer Testing amb totes les classes que funcionen per els Trips
 */
@SpringBootTest
public class TripTest {

    FinderUser fU;
    FinderDriver fD;
    GatewayUser gU;
    GatewayDriver gD;
    FinderVehicle fV;
    GatewayVehicle gV;
    FinderTrip fT;
    GatewayTrip gT;

    //BEF & AFTER
    @BeforeEach
    public void initialize() throws SQLException {
        fU = FinderUser.getInstance();
        fD = FinderDriver.getInstance();
        gU = insertTest();
        gD = new GatewayDriver("UserTestClass");
        gD.insert();
        fV = FinderVehicle.getInstance();
        gV = insertTestVehicleComplet();
        fT = FinderTrip.getInstance();
        gT = insertTestTrip();
    }

    @AfterEach
    public void removeGateways() throws SQLException {
        if(fU.findByUsername(gU.getUsername()) != null)gU.remove();
        if (gV != null) gV.remove();
    }

    //FUNCTIONS
    private GatewayUser insertTest() throws SQLException {
        gU = new GatewayUser("0","p","UserTestClass","t@gmail.com","T","T", "f", "/t");
        gU.insert();
        return gU;
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

    //TEST
    @Test
    public void createTripTest1() throws SQLException {
        GatewayTrip gTTest = fT.findByUser("UserTestClass",LocalDate.of(2001, 2, 12),new Time(12*60*60*1000));
        String res = gTTest.getUsername()+ " " + gTTest.getVehicleNumberPlate();
        assertEquals("UserTestClass 1221", res);
    }


    @Test
    public void updateTrip() throws SQLException {
            GatewayTrip gTest = fT.findByUser("UserTestClass",LocalDate.of(2001, 2, 12),new Time(12*60*60*1000));
            gTest.setRestrictions("NoMascotes");
            gTest.update();
            System.out.println(gTest.json());
            gTest = fT.findByUser("UserTestClass",LocalDate.of(2001, 2, 12),new Time(12*60*60*1000));
            System.out.println(gTest.json());
            String res =gTest.getVehicleNumberPlate() + " " + gTest.getRestrictions();
            assertEquals("1221 NoMascotes", res);
    }

    @Test
    public void deleteTripTest() throws SQLException {
        gT.remove();
        GatewayTrip gTtemplate = fT.findByUser("UserTestClass",LocalDate.of(2001, 2, 12),new Time(12*60*60*1000));
        assertNull(gTtemplate);
    }


}
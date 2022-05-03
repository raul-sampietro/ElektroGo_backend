/**
 * @file DriverTest.java
 * @author Gerard Castell
 * @date 12/03/2023
 * @brief Implementació dels Tests DriverTest
 */
package elektroGo.back;
import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderTrip;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayTrip;
import elektroGo.back.data.gateways.GatewayUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @brief La clase Trip Test es l'encarreaada de fer Testing amb totes les classes que funcionen per els Trips
 */
@SpringBootTest
public class TripTest {
    @Test
    public void createTripTest1() throws SQLException {
        GatewayTrip gT = new GatewayTrip(/*"2",*/ LocalDate.of(2001, 2, 12), new Time(12,0,0),
                5,0, "null","null", LocalDate.of(2001, 2,10),
                "1234aas","bcn", "gir",new BigDecimal("41.3000"),new BigDecimal("41.0000"),new BigDecimal("41.3000"),new BigDecimal("41.0000"), "Test2");
        Database d = Database.getInstance();
        gT.insert();
        FinderTrip fT = FinderTrip.getInstance();
        GatewayTrip gTTest = fT.findByUser("Test2",LocalDate.of(2001, 2, 12),new Time(12,0,0));
        String res = gTTest.getUsername()+ " " + gTTest.getVehicleNumberPlate();
        d.executeSQLUpdate("delete from TRIP where username = 'Test2';");
        assertEquals("Test2 1234aas", res);
    }

    private GatewayTrip insertTestTrip() throws SQLException {
        GatewayTrip gT = new GatewayTrip(LocalDate.of(2001, 2, 12), new Time(12,0,0),
                5,0, "null", "null",LocalDate.of(2001, 2,10),
                "1234aas","bcn" ,"gir",new BigDecimal("41.3000"),new BigDecimal("41.0000"), new BigDecimal("41.3000"),new BigDecimal("41.0000"), "Test2");
        gT.insert();
        return gT;
    }


    @Test
    public void updateTrip() throws SQLException {
        insertTestTrip();
        Database d = Database.getInstance();
        try {
            FinderTrip fT = FinderTrip.getInstance();
            GatewayTrip gT = fT.findByUser("Test2",LocalDate.of(2001, 2, 12),new Time(12,0,0));;
            gT.setRestrictions("NoMascotes");
            gT.update();
            gT = fT.findByUser("Test2",LocalDate.of(2001, 2, 12),new Time(12,0,0));;
            String res =gT.getVehicleNumberPlate() + " " + gT.getRestrictions();
            assertEquals("1234aas NoMascotes", res);
        }
        catch (SQLException s) {
            s.printStackTrace();
        }
        d.executeSQLUpdate("delete from TRIP where username = 'Test2';");
    }

    @Test
    public void deleteTripTest() throws SQLException {
        GatewayTrip gT =  insertTestTrip();
        gT.remove();
        FinderTrip fT = FinderTrip.getInstance();
        GatewayTrip gTtemplate = fT.findByUser("Test2",LocalDate.of(2001, 2, 12),new Time(12,0,0));
        assertNull(gTtemplate);
    }


}
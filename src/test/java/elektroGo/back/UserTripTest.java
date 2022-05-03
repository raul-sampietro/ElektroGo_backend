package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderUserTrip;
import elektroGo.back.data.gateways.GatewayUserTrip;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTripTest {

    @Test
    public void createUserTrip() throws SQLException {
        GatewayUserTrip gU = new GatewayUserTrip(40, "FirstUser");
        Database d = Database.getInstance();
        gU.insert();
        FinderUserTrip fT = FinderUserTrip.getInstance();
        GatewayUserTrip gTTest = fT.findByTripUser(40,"FirstUser");
        String res = gTTest.getId() + " " + gTTest.getUsername();
        d.executeSQLUpdate("delete from USERTRIP where id = 40;");
        assertEquals("40 FirstUser", res);
    }

    private GatewayUserTrip insertTestTrip() throws SQLException {
        GatewayUserTrip gT = new GatewayUserTrip(40, "FirstUser");
        gT.insert();
        return gT;
    }
    private void insertTestTrip2() throws SQLException {
        GatewayUserTrip gT = new GatewayUserTrip(40, "Test4");
        gT.insert();
    }

    /*
    public void updateTrip(){
        //UN UPDATE NO TE SENTIT JA QUE LES DUES SON CLAUS PRIMARIES
    }*/
    @Test
    public void FindByDrive() throws SQLException {
        insertTestTrip();
        insertTestTrip2();
        Database d = Database.getInstance();
        FinderUserTrip fT = FinderUserTrip.getInstance();
        ArrayList<GatewayUserTrip> gUT = fT.findByDriver("Test4");
        String res = gUT.get(0).getId() + " " +gUT.get(0).getUsername();
        d.executeSQLUpdate("delete from USERTRIP where id = 40;");
        assertEquals("40 Test4",res);

    }

    @Test
    public void deleteTripTest() throws SQLException {
        GatewayUserTrip gT =  insertTestTrip();
        gT.remove();
        FinderUserTrip fT = FinderUserTrip.getInstance();
        ArrayList<GatewayUserTrip> gUT = fT.findByTrip(40);
        assertEquals(gUT,new ArrayList<>());
    }

}
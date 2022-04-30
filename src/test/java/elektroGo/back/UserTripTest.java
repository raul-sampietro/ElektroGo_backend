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
        GatewayUserTrip gU = new GatewayUserTrip(1, "gerard");
        Database d = Database.getInstance();
        gU.insert();
        FinderUserTrip fT = FinderUserTrip.getInstance();
        GatewayUserTrip gTTest = fT.findByTripUser(1,"gerard");
        String res = gTTest.getId() + " " + gTTest.getUsername();
        d.executeSQLUpdate("delete from USERTRIP where id = 1;");
        assertEquals("1 gerard", res);
    }

    private GatewayUserTrip insertTestTrip() throws SQLException {
        GatewayUserTrip gT = new GatewayUserTrip(1, "gerard");
        gT.insert();
        return gT;
    }
    private void insertTestTrip2() throws SQLException {
        GatewayUserTrip gT = new GatewayUserTrip(1, "Test");
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
        ArrayList<GatewayUserTrip> gUT = fT.findByDriver("Test");
        String res = gUT.get(0).getId() + " " +gUT.get(0).getUsername();
        d.executeSQLUpdate("delete from USERTRIP where id = 1;");
        assertEquals("1 Test",res);

    }

    @Test
    public void deleteTripTest() throws SQLException {
        GatewayUserTrip gT =  insertTestTrip();
        gT.remove();
        FinderUserTrip fT = FinderUserTrip.getInstance();
        ArrayList<GatewayUserTrip> gUT = fT.findByTrip(1);
        assertEquals(gUT,new ArrayList<>());
    }

}
package elektroGo.back;

import elektroGo.back.data.finders.FinderRating;
import elektroGo.back.data.gateways.GatewayRating;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

@SpringBootTest
public class RatingTest {

    //Some inserts that will be used
    private GatewayRating insertGatewayRating1() throws SQLException {
        GatewayRating gR = new GatewayRating("Test","Test2",7,"Bon test la veritat");
        gR.insert();
        return gR;
    }

    private GatewayRating insertGatewayRating2() throws SQLException {
        GatewayRating gR = new GatewayRating("Test2","Test",7,"Tu tambe ets bon test");
        gR.insert();
        return gR;
    }

    //Test the Gateway
    @Test
    public void createRatingTest() throws SQLException {
        GatewayRating gR = insertGatewayRating1();
        FinderRating fR = FinderRating.getInstance();
        GatewayRating gRTest = fR.findByPrimaryKey(gR.getUserWhoRates(), gR.getRatedUser());
        gR.remove();
        assertEquals(gR.json(), gRTest.json());
    }

    @Test
    public void updateRatingTest() throws SQLException {
        GatewayRating gR = insertGatewayRating1();
        FinderRating fR = FinderRating.getInstance();
        gR.setPoints(3);
        gR.setComment("Ja no em cau be, me l'ha liat");
        gR.update();
        GatewayRating gRTest = fR.findByPrimaryKey(gR.getUserWhoRates(), gR.getRatedUser());
        assertEquals(gR.json(), gRTest.json());
        gR.remove();
    }

    @Test
    public void updateRatingTest2() throws SQLException {
        GatewayRating gR = insertGatewayRating1();
        GatewayRating gR2 = insertGatewayRating2();
        FinderRating fR = FinderRating.getInstance();
        gR.setPoints(3);
        gR.setComment("Ja no em cau be, me l'ha liat");
        gR.update();
        GatewayRating gRTest = fR.findByPrimaryKey(gR.getUserWhoRates(), gR.getRatedUser());
        assertEquals(gR.json(), gRTest.json());

        //We ensure that other records did not get affected
        GatewayRating gRTest2 = fR.findByPrimaryKey(gR2.getUserWhoRates(), gR2.getRatedUser());
        assertEquals(gR2.json(), gRTest2.json());
        gR.remove();
        gR2.remove();
    }

    @Test
    public void removeRatingTest() throws  SQLException {
        GatewayRating gR = insertGatewayRating1();
        FinderRating fR = FinderRating.getInstance();
        gR.remove();
        GatewayRating gRTest = fR.findByPrimaryKey(gR.getUserWhoRates(), gR.getRatedUser());
        assertNull(gRTest);
    }

    //Test the Finder (we'll assume that at this point findByPK is correctly working because we used it in the last tests)

    @Test
    public void findByRatedUserTest() throws SQLException {
        GatewayRating gR = insertGatewayRating1();
        GatewayRating gR2 = insertGatewayRating2();
        FinderRating fR = FinderRating.getInstance();
        ArrayList<GatewayRating> aL =  fR.findByRatedUser(gR.getRatedUser());
        assertEquals(1,aL.size());
        assertEquals(aL.get(0).json(), gR.json());
        gR.remove();
        gR2.remove();
    }

    @Test
    public void findByUserWhoRatesTest() throws SQLException {
        GatewayRating gR = insertGatewayRating1();
        GatewayRating gR2 = insertGatewayRating2();
        FinderRating fR = FinderRating.getInstance();
        ArrayList<GatewayRating> aL =  fR.findByRatedUser(gR2.getUserWhoRates());
        assertEquals(1,aL.size());
        assertEquals(aL.get(0).json(), gR.json());
        gR.remove();
        gR2.remove();
    }
}

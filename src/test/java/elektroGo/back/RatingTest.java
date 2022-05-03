package elektroGo.back;

import elektroGo.back.data.finders.FinderRating;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayRating;
import elektroGo.back.data.gateways.GatewayUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;

@SpringBootTest
public class RatingTest {

    FinderRating fR;
    GatewayRating gR1;
    GatewayRating gR2;
    GatewayRating gR3;
    GatewayUser gU1;
    GatewayUser gU2;
    GatewayUser gU3;
    GatewayUser gU4;

    @BeforeEach
    public void Initialize() throws SQLException {
        fR = FinderRating.getInstance();
        createUsers();
        gR1 = insertGatewayRating1();
        gR2 = insertGatewayRating2();
        gR3 = insertGatewayRating3();
    }

    @AfterEach
    public void Remove() throws SQLException {
        if (gR1 != null ) gR1.remove();
        if (gR2 != null) gR2.remove();
        if (gR3 != null) gR3.remove();
        if (gU1 != null) gU1.remove();
        if (gU2 != null) gU2.remove();
        if (gU3 != null) gU3.remove();
        if (gU4 != null) gU4.remove();
    }

    private GatewayUser createUser(String username) {
        return new GatewayUser(username, "Test", username, "test@test.test",username, username, username+"family", "/test");
    }
    private void createUsers() throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        gU1 = fU.findByUsername("TestRating1");
        gU2 = fU.findByUsername("TestRating2");
        gU3 = fU.findByUsername("TestRating3");
        gU4 = fU.findByUsername("TestRating4");
        if (gU1 == null) {
            gU1 = createUser("TestRating1");
            gU1.insert();
        }
        if (gU2 == null) {
            gU2 = createUser("TestRating2");
            gU2.insert();
        }
        if (gU3 == null) {
            gU3 = createUser("TestRating3");
            gU3.insert();
        }
        if (gU4 == null) {
            gU4 = createUser("TestRating4");
            gU4.insert();
        }
    }

    //Some inserts that will be used
    private GatewayRating insertGatewayRating1() throws SQLException {
        GatewayRating gR = new GatewayRating("TestRating1","TestRating2",7,"Bon test la veritat");
        gR.insert();
        return gR;
    }

    private GatewayRating insertGatewayRating2() throws SQLException {
        GatewayRating gR = new GatewayRating("TestRating2","TestRating3",6,"Tu tambe ets bon test");
        gR.insert();
        return gR;
    }

    private GatewayRating insertGatewayRating3() throws SQLException {
        GatewayRating gR = new GatewayRating("TestRating4","TestRating3",5,"Cuidado amb aquest");
        gR.insert();
        return gR;
    }




    //Test the Gateway
    @Test
    public void createRatingTest() throws SQLException {
        GatewayRating gRTest = fR.findByPrimaryKey(gR1.getUserWhoRates(), gR1.getRatedUser());
        assertEquals(gR1.json(), gRTest.json());
    }

    @Test
    public void updateRatingTest() throws SQLException {
        gR1.setPoints(3);
        gR1.setComment("Ja no em cau be, me l'ha liat");
        gR1.update();
        GatewayRating gRTest = fR.findByPrimaryKey(gR1.getUserWhoRates(), gR1.getRatedUser());
        assertEquals(gR1.json(), gRTest.json());
    }

    @Test
    public void updateRatingTest2() throws SQLException {
        gR1.setPoints(3);
        gR1.setComment("Ja no em cau be, me l'ha liat");
        gR1.update();
        GatewayRating gRTest = fR.findByPrimaryKey(gR1.getUserWhoRates(), gR1.getRatedUser());
        assertEquals(gR1.json(), gRTest.json());

        //We ensure that other records did not get affected
        GatewayRating gRTest2 = fR.findByPrimaryKey(gR2.getUserWhoRates(), gR2.getRatedUser());
        assertEquals(gR2.json(), gRTest2.json());
    }

    @Test
    public void removeRatingTest() throws  SQLException {
        gR1.remove();
        GatewayRating gRTest = fR.findByPrimaryKey(gR1.getUserWhoRates(), gR1.getRatedUser());
        gR1 = null;
        assertNull(gRTest);
    }

    //Test the Finder (we'll assume that at this point findByPK is correctly working because we used it in the last tests)

    @Test
    public void findByRatedUserTest() throws SQLException {
        ArrayList<GatewayRating> aL =  fR.findByRatedUser(gR1.getRatedUser());
        assertEquals(1,aL.size());
        assertEquals(aL.get(0).json(), gR1.json());
        gR1.remove();
        gR2.remove();
    }

    @Test
    public void findByUserWhoRatesTest() throws SQLException {
        ArrayList<GatewayRating> aL =  fR.findByRatedUser(gR2.getUserWhoRates());
        assertEquals(1,aL.size());
        assertEquals(aL.get(0).json(), gR1.json());
    }

    @Test
    public void findAvgRateUserTest() throws SQLException {
        double d = fR.findUserRateAvg(gR2.getRatedUser());
        assertEquals( (double)(gR2.getPoints()+gR3.getPoints())/2, d );
    }
}

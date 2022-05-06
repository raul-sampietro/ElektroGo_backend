package elektroGo.back;

import elektroGo.back.data.finders.FinderReport;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayReport;
import elektroGo.back.data.gateways.GatewayUser;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReportTest {

    FinderReport fR;
    GatewayReport gR1;
    GatewayReport gR2;
    GatewayReport gR3;
    GatewayUser gU1;
    GatewayUser gU2;
    GatewayUser gU3;

    @BeforeEach
    public void initialize() throws SQLException {
        fR = FinderReport.getInstance();
        FinderUser fU = FinderUser.getInstance();
        insertUsers();
        gR1 = insertTestReportComplet();
        gR2 = insertTestReportComplet2();
        gR3 = insertTestReportComplet3();
    }

    private GatewayUser createUser(String username) {
        return new GatewayUser(username, "Test", username, "test@test.test",username, username, username+"family", "/test");
    }

    private void insertUsers() throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        gU1 = fU.findByUsername("TestReport1");
        gU2 = fU.findByUsername("TestReport2");
        gU3 = fU.findByUsername("TestReport3");
        if (gU1 == null) {
            gU1 = createUser("TestReport1");
            gU1.insert();
        }
        if (gU2 == null) {
            gU2 = createUser("TestReport2");
            gU2.insert();
        }
        if (gU3 == null) {
            gU3 = createUser("TestReport3");
            gU3.insert();
        }
    }

    @AfterEach
    public void removeGateways() throws SQLException {
        if (gR1 != null ) gR1.remove();
        if (gR2 != null) gR2.remove();
        if (gR3 != null) gR3.remove();
        if (gU1 != null) gU1.remove();
        if (gU2 != null) gU2.remove();
        if (gU3 != null) gU3.remove();
    }

    private GatewayReport insertTestReportComplet() throws SQLException {
        GatewayReport gRAct = new GatewayReport("TestReport1","TestReport2","Em cau malament");
        gRAct.insert();
        return gRAct;
    }

    private GatewayReport insertTestReportComplet2() throws SQLException {
        GatewayReport gRAct = new GatewayReport("TestReport2","TestReport1","M'ha deixat tirat");
        gRAct.insert();
        return gRAct;
    }

    private GatewayReport insertTestReportComplet3() throws SQLException {
        GatewayReport gRAct = new GatewayReport("TestReport3","TestReport2","M'ha deixat tirat");
        gRAct.insert();
        return gRAct;
    }


    @Test
    public void createReportTest() throws SQLException {
        GatewayReport gRTest = fR.findByPrimaryKey(gR1.getUserWhoReports(), gR1.getReportedUser());
        assertEquals(gR1.json(), gRTest.json());
    }

    @Test
    public void updateReportTest() throws SQLException {
        gR1.setReason("No em cau malament, em cau MOLT malament");
        gR1.update();
        GatewayReport gRTest = fR.findByPrimaryKey(gR1.getUserWhoReports(), gR1.getReportedUser());
        assertEquals(gR1.json(), gRTest.json());
        assertEquals("No em cau malament, em cau MOLT malament", gRTest.getReason());
    }

    @Test
    public void updateReportTest2() throws SQLException {
        gR1.setReason("No em cau malament, em cau MOLT malament");
        gR1.update();
        GatewayReport gRTest = fR.findByPrimaryKey(gR1.getUserWhoReports(), gR1.getReportedUser());
        GatewayReport gRTest2 = fR.findByPrimaryKey(gR2.getUserWhoReports(), gR2.getReportedUser());
        assertEquals(gR1.json(), gRTest.json());
        assertEquals("No em cau malament, em cau MOLT malament", gRTest.getReason());
        assertEquals(gR2.json(), gRTest2.json());
    }

    //Test the finder (we'll assume that at this point findByPK is correctly working because we used it in the last tests)

    @Test
    public void findByUserWhoReportsTest() throws SQLException {
        ArrayList<GatewayReport> aL = fR.findByUserWhoReports(gR3.getUserWhoReports());
        assertEquals(1,aL.size());
        assertEquals(gR3.json(),aL.get(0).json());
        gR3.remove();
    }

    @Test
    public void findByReportedUserTest() throws SQLException {
        ArrayList<GatewayReport> aL = fR.findByReportedUser(gR3.getReportedUser());
        assertEquals(2,aL.size());
        if (aL.get(0).getUserWhoReports().equals(gR1.getUserWhoReports())) {
            assertEquals(gR1.json(),aL.get(0).json());
            assertEquals(gR3.json(),aL.get(1).json());
        }
        else {
            assertEquals(gR3.json(),aL.get(0).json());
            assertEquals(gR1.json(),aL.get(1).json());
        }
        gR3.remove();
    }


}

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
    GatewayReport gR;
    GatewayReport gR2;
    GatewayUser gU;
    boolean gUInserted;

    @BeforeEach
    public void initialize() throws SQLException {
        gUInserted = false;
        fR = FinderReport.getInstance();
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername("Test") == null) insertTest();
        gR = insertTestReportComplet();
        gR2 = insertTestReportComplet2();
    }

    @AfterEach
    public void removeGateways() throws SQLException {
        gR.remove();
        gR2.remove();
        if (gUInserted) gU.remove();
    }

    private void insertTest() throws SQLException {
        gU = new GatewayUser("9999999991","prov2","Test","test@gmail.com","Test","Test", "fnTest", "/test");
        gU.insert();
        gUInserted = true;
    }
    private GatewayReport insertTestReportComplet() throws SQLException {
        GatewayReport gRAct = new GatewayReport("Test","Test2","Em cau malament");
        gRAct.insert();
        return gRAct;
    }

    private GatewayReport insertTestReportComplet2() throws SQLException {
        GatewayReport gRAct = new GatewayReport("Test2","Test","M'ha deixat tirat");
        gRAct.insert();
        return gRAct;
    }

    private GatewayReport insertTestReportComplet3() throws SQLException {
        GatewayReport gRAct = new GatewayReport("Test3","Test2","M'ha deixat tirat");
        gRAct.insert();
        return gRAct;
    }


    @Test
    public void createReportTest() throws SQLException {
        GatewayReport gRTest = fR.findByPrimaryKey(gR.getUserWhoReports(), gR.getReportedUser());
        assertEquals(gR.json(), gRTest.json());
    }

    @Test
    public void updateReportTest() throws SQLException {
        gR.setReason("No em cau malament, em cau MOLT malament");
        gR.update();
        GatewayReport gRTest = fR.findByPrimaryKey(gR.getUserWhoReports(), gR.getReportedUser());
        assertEquals(gR.json(), gRTest.json());
        assertEquals("No em cau malament, em cau MOLT malament", gRTest.getReason());
    }

    @Test
    public void updateReportTest2() throws SQLException {
        gR.setReason("No em cau malament, em cau MOLT malament");
        gR.update();
        GatewayReport gRTest = fR.findByPrimaryKey(gR.getUserWhoReports(), gR.getReportedUser());
        GatewayReport gRTest2 = fR.findByPrimaryKey(gR2.getUserWhoReports(), gR2.getReportedUser());
        assertEquals(gR.json(), gRTest.json());
        assertEquals("No em cau malament, em cau MOLT malament", gRTest.getReason());
        assertEquals(gR2.json(), gRTest2.json());
    }

    //Test the finder (we'll assume that at this point findByPK is correctly working because we used it in the last tests)

    @Test
    public void findByUserWhoReportsTest() throws SQLException {
        GatewayReport gR3 = insertTestReportComplet3();
        ArrayList<GatewayReport> aL = fR.findByUserWhoReports(gR3.getUserWhoReports());
        assertEquals(1,aL.size());
        assertEquals(gR3.json(),aL.get(0).json());
        gR3.remove();
    }

    @Test
    public void findByReportedUserTest() throws SQLException {
        GatewayReport gR3 = insertTestReportComplet3();
        ArrayList<GatewayReport> aL = fR.findByReportedUser(gR3.getReportedUser());
        assertEquals(2,aL.size());
        if (aL.get(0).getUserWhoReports().equals(gR.getUserWhoReports())) {
            assertEquals(gR.json(),aL.get(0).json());
            assertEquals(gR3.json(),aL.get(1).json());
        }
        else {
            assertEquals(gR3.json(),aL.get(0).json());
            assertEquals(gR.json(),aL.get(1).json());
        }
        gR3.remove();
    }


}

package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.Finders.FinderUser;
import elektroGo.back.data.Finders.FinderVehicle;
import elektroGo.back.data.Gateways.GatewayUser;
import elektroGo.back.data.Gateways.GatewayVehicle;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {
    @Test
    public void createUserTest1() throws SQLException {
        GatewayUser gV = new GatewayUser("TestUser","test@mail.com","5755");
        Database d = Database.getInstance();
        gV.insert();
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gUTest = fU.findByUserName("TestUser");
        String res = gUTest.getUserName() + " " + gUTest.getMail() + " " + gUTest.getPassword();
        d.executeSQLUpdate("delete from USERS where userName = 'TestUser';");
        assertEquals("TestUser test@mail.com 5755", res);
    }

    private GatewayUser insertTestUser() throws SQLException {
        GatewayUser gU = new GatewayUser("TestUser","test@mail.com","5755");
        gU.insert();
        return gU;
    }


    @Test
    public void updateUser() throws SQLException {
        insertTestUser();
        Database d = Database.getInstance();
        try {
            FinderUser fU = FinderUser.getInstance();
            GatewayUser gU = fU.findByUserName("TestUser");
            gU.setPassword("NewPassword");
            gU.update();
            gU = fU.findByUserName("TestUser");
            String res = gU.getUserName() + " " + gU.getMail() + " " + gU.getPassword();
            assertEquals("TestUser test@mail.com NewPassword", res);
        }
        catch (SQLException s) {
            s.printStackTrace();
        }
        d.executeSQLUpdate("delete from USERS where userName = 'TestUser';");
    }

    @Test
    public void deleteUserTest() throws SQLException {
        GatewayUser gU =  insertTestUser();
        Database d = Database.getInstance();
        gU.remove();
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gUtemplate = fU.findByUserName("TestUser");
        assertNull(gUtemplate);
        assertNull(null);
    }

    @Test
    public void readUserTest() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("insert into USERS values('UserTesting','mailTesting','testingPassword');");
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gUTest = fU.findByUserName("UserTesting");
        String res = gUTest.getUserName() + " " + gUTest.getMail() + " " + gUTest.getPassword();
        d.executeSQLUpdate("delete from USERS where userName = 'UserTesting';");
        assertEquals("UserTesting mailTesting testingPassword", res);
    }
}
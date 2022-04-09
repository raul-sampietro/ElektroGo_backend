/**
 * @file DriverTest.java
 * @author Gerard Castell
 * @date 12/03/2023
 * @brief Implementació dels Tests DriverTest
 */
package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @brief La clase Driver Test es l'encarreaada de fer Testing amb totes les classes que funcionen per els Drivers
 */
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
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
        GatewayUser gV = new GatewayUser("id1","prov1","Test", "test@mail.com", "name1","gN1","fN1","url1");
        Database d = Database.getInstance();
        gV.insert();
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gUTest = fU.findByUsername("Test");
        String res = gUTest.getUsername() + " " + gUTest.getEmail() + " " + gUTest.getName();
        d.executeSQLUpdate("delete from USERS where userName = 'Test';");
        assertEquals("Test test@mail.com name1", res);
    }

    private GatewayUser insertTestUser() throws SQLException {
        GatewayUser gU = new GatewayUser("id1","prov1","Test", "test@mail.com", "name1","gN1","fN1","url1");
        gU.insert();
        return gU;
    }


    @Test
    public void updateUser() throws SQLException {
        insertTestUser();
        Database d = Database.getInstance();
        try {
            FinderUser fU = FinderUser.getInstance();
            GatewayUser gU = fU.findByUsername("Test");
            gU.setName("name32");
            gU.update();
            gU = fU.findByUsername("Test");
            String res = gU.getName();
            assertEquals("name32", res);
        }
        catch (SQLException s) {
            s.printStackTrace();
        }
        d.executeSQLUpdate("delete from USERS where userName = 'Test';");
    }

    @Test
    public void deleteUserTest() throws SQLException {
        GatewayUser gU =  insertTestUser();
        gU.remove();
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gUtemplate = fU.findByUsername("Test");
        assertNull(gUtemplate);
    }

}
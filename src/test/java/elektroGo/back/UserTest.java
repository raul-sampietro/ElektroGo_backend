/**
 * @file DriverTest.java
 * @author Gerard Castell
 * @date 12/03/2023
 * @brief Implementació dels Tests DriverTest
 */
package elektroGo.back;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @brief La clase Driver Test es l'encarreaada de fer Testing amb totes les classes que funcionen per els Drivers
 */
@SpringBootTest
public class UserTest {

    FinderUser fU;
    GatewayUser gU;

    //AFTER AND BEFORE
    @BeforeEach
    public void initialize() throws SQLException {
        fU = FinderUser.getInstance();
        gU = insertTest();
    }

    @AfterEach
    public void removeGateways() throws SQLException {
        if(fU.findByUsername(gU.getUsername()) != null)gU.remove();
    }

    //FUNCTIONS
    private GatewayUser insertTest() throws SQLException {
        gU = new GatewayUser("0","p","UserTestClass","t@gmail.com","T","T", "f", "/t");
        gU.insert();
        return gU;
    }

    //TEST
    @Test
    public void createUserTest1() throws SQLException {
        GatewayUser gUR = fU.findByUsername("UserTestClass");
        assertEquals(gU.json(), gUR.json());
    }

    @Test
    public void updateUser() throws SQLException {
        GatewayUser gUpdate = fU.findByUsername("UserTestClass");
        gUpdate.setName("name32");
        gUpdate.update();
        gUpdate = fU.findByUsername("UserTestClass");
        String res = gUpdate.getName();
        assertEquals("name32", res);
    }

    @Test
    public void deleteUserTest() throws SQLException {
        gU.remove();
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gUtemplate = fU.findByUsername("UserTestClass");
        assertNull(gUtemplate);
    }

    @Test
    public void getUserByIdTest() throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        GatewayUser testG = fU.findById(gU.getId(), gU.getProvider());
        assertEquals(gU.getUsername(), testG.getUsername(), "Incorrect username get");
    }
}
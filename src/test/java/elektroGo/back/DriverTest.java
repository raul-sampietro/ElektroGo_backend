/**
 * @file DriverTest.java
 * @author Gerard Castell
 * @date 12/03/2023
 * @brief Implementació dels Tests DriverTest
 */


package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderDriver;

import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayDriver;

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
public class DriverTest {


    FinderUser fU;
    FinderDriver fD;
    GatewayUser gU;
    GatewayDriver gV;

    //BEFORE AND AFTER
    @BeforeEach
    public void initialize() throws SQLException {
        fU = FinderUser.getInstance();
        fD = FinderDriver.getInstance();
        gU = insertTest();
        gV = new GatewayDriver("UserTestClass",false);
        gV.insert();
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
    public void createDriverTest1() throws SQLException {
        GatewayDriver gUTest = fD.findByUserName("UserTestClass");
        String res = gUTest.getUsername();
        assertEquals("UserTestClass", res);
    }

    @Test
    public void deleteDriverTest() throws SQLException {
        gV.remove();
        GatewayDriver gUtemplate = fD.findByUserName("UserTestClass");
        assertNull(gUtemplate);
    }
}
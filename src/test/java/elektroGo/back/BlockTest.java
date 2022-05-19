/**
 * @file BlockTest.java
 * @author Gerard Castell
 * @date 19/05/2022
 * @brief Implementació dels Tests DriverTest
 */


package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderBlock;
import elektroGo.back.data.finders.FinderDriver;

import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayBlock;
import elektroGo.back.data.gateways.GatewayDriver;

import elektroGo.back.data.gateways.GatewayUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;


/**
 * @brief La clase Driver Test es l'encarreaada de fer Testing amb totes les classes que funcionen per els Blocks
 */
@SpringBootTest
public class BlockTest {


    FinderUser fU;
    FinderBlock fD;
    GatewayUser gU;
    GatewayUser gB;
    GatewayBlock gV;

    //BEFORE AND AFTER
    @BeforeEach
    public void initialize() throws SQLException {
        fU = FinderUser.getInstance();
        fD = FinderBlock.getInstance();
        gU = insertTest1();
        gB = insertTest1();
        gV = new GatewayBlock("UserBlocking","BlockUser");
        gV.insert();
    }

    @AfterEach
    public void removeGateways() throws SQLException {
        if(fU.findByUsername(gU.getUsername()) != null)gU.remove();
        if(fU.findByUsername(gB.getUsername()) != null)gB.remove();
    }

    //FUNCTIONS
    private GatewayUser insertTest1() throws SQLException {
        GatewayUser g1 = new GatewayUser("0","p","UserBlocking","t@gmail.com","T","T", "f", "/t");
        g1.insert();
        return g1;
    }
    private GatewayUser insertTest2() throws SQLException {
        GatewayUser g2 = new GatewayUser("0","p","BlockUser","t@gmail.com","T","T", "f", "/t");
        g2.insert();
        return g2;
    }

    //TEST
    @Test
    public void createDriverTest1() throws SQLException {
        GatewayBlock gUTest = fD.findByPrimaryKey("UserBlocking","BlockUser");
        String res = gUTest.getUserBlocking() + " "+  gUTest.getBlockUser();
        assertEquals("UserBlocking BlockUser", res);
    }

    @Test
    public void deleteDriverTest() throws SQLException {
        gV.remove();
        GatewayBlock gUtemplate = fD.findByPrimaryKey("UserBlocking","BlockUser");
        assertNull(gUtemplate);
    }
}
package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.Finders.FinderDriver;

import elektroGo.back.data.Gateways.GatewayDriver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DriverTest {
    @Test
    public void createDriverTest1() throws SQLException {
        GatewayDriver gV = new GatewayDriver("Marc");
        Database d = Database.getInstance();
        gV.insert();
        FinderDriver fU = FinderDriver.getInstance();
        GatewayDriver gUTest = fU.findByUserName("Marc");
        String res = gUTest.getUserName();
        d.executeSQLUpdate("delete from DRIVER where userName = 'Marc';");
        assertEquals("Marc", res);
    }

    private GatewayDriver insertDriverUser() throws SQLException {
        GatewayDriver gU = new GatewayDriver("Marc");
        gU.insert();
        return gU;
    }


    @Test
    public void deleteDriverTest() throws SQLException {
        GatewayDriver gU = insertDriverUser();
        Database d = Database.getInstance();
        gU.remove();
        FinderDriver fU = FinderDriver.getInstance();
        GatewayDriver gUtemplate = fU.findByUserName("Marc");
        assertNull(gUtemplate);
        assertNull(null);
    }


    @Test
    public void readDriverTest() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("insert into DRIVER values('gerard');");
        FinderDriver fU = FinderDriver.getInstance();
        GatewayDriver gUTest = fU.findByUserName("gerard");
        String res = gUTest.getUserName();
        d.executeSQLUpdate("delete from DRIVER where userName = 'gerard';");
        assertEquals("gerard", res);
    }
}
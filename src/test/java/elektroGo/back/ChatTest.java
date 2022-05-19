/**
 * @file ChatTest.java
 * @author Adria Abad
 * @date 29/04/2023
 * @brief Implementació dels Tests ChatTest
 */


package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderChats;
import elektroGo.back.data.finders.FinderDriver;
import elektroGo.back.data.finders.FinderVehicle;
import elektroGo.back.data.gateways.GatewayChats;
import elektroGo.back.data.gateways.GatewayDriver;
import elektroGo.back.data.gateways.GatewayVehicle;
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * @brief La clase ChatTest es l'encarreaada de fer Testing amb totes les classes que funcionen pel Chat
 */
@SpringBootTest
public class ChatTest {

    CustomLogger logger = CustomLogger.getInstance();

    private GatewayChats insertChat() throws SQLException {
        GatewayChats gC = new GatewayChats("Test", "Test2", "message", "timestamp");
        gC.insert();
        return gC;
    }

    @Test
    public void createChatTest() throws SQLException {
        Database d = Database.getInstance();
        GatewayChats gC =  insertChat();
        FinderChats fC = FinderChats.getInstance();
        logger.log(gC.getSender() + " " + gC.getReceiver() + " " + gC.getMessage(), logType.TRACE);
        GatewayChats gCTest = fC.findByMessage(gC.getSender(), gC.getReceiver(), gC.getMessage());
        String res = gCTest.getSender() + " " + gCTest.getReceiver() + " " + gCTest.getMessage() + " " + gCTest.getSentAt();
        gC.remove();
        assertEquals("Test Test2 message timestamp", res);
    }

    @Test
    public void deleteChatTest() throws SQLException {
        GatewayChats gC = insertChat();
        Database d = Database.getInstance();
        gC.remove();
        FinderChats fC = FinderChats.getInstance();
        GatewayChats gCEmpty = fC.findByMessage("Test", "Test2", "message");
        assertNull(gCEmpty);
    }

}
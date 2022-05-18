package elektroGo.back.model;

import elektroGo.back.data.finders.FinderChats;
import elektroGo.back.data.finders.FinderDeletedChats;
import elektroGo.back.data.gateways.GatewayChats;
import elektroGo.back.data.gateways.GatewayDeletedChats;

import java.sql.SQLException;
import java.util.ArrayList;

public class DeletedChats {

    public ArrayList<String> getDeletedChatsFromUser(String username) throws SQLException {
        FinderDeletedChats fDCS = FinderDeletedChats.getInstance();
        return fDCS.findByUserDeleting(username);
    }

    public void deleteMessagesIfNeeded(String userA, String userB) throws SQLException {
        FinderDeletedChats fDCS = FinderDeletedChats.getInstance();
        ArrayList<GatewayDeletedChats> deletedChats = fDCS.findByMutualDelete(userA, userB);
        if (deletedChats.size() == 2) {
            FinderChats fC = FinderChats.getInstance();
            ArrayList<GatewayChats> aL = fC.findByConversation(userA, userB);
            for (GatewayChats gC : aL) {
                gC.remove();
            }
            for (GatewayDeletedChats gDC : deletedChats) {
                gDC.remove();
            }
        }
    }
}

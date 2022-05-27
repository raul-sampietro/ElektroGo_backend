package elektroGo.back.model;

import elektroGo.back.data.finders.FinderBlock;
import elektroGo.back.data.finders.FinderChats;
import elektroGo.back.data.finders.FinderDeletedChats;
import elektroGo.back.data.gateways.GatewayBlock;
import elektroGo.back.data.gateways.GatewayChats;
import elektroGo.back.data.gateways.GatewayDeletedChats;

import java.sql.SQLException;
import java.util.ArrayList;

public class ManagementChat {

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

    public void messageSent(String sender, String receiver) throws SQLException {
        FinderDeletedChats fDCS = FinderDeletedChats.getInstance();
        ArrayList<String> deletedUsers = fDCS.findByUserDeleting(receiver);
        if (deletedUsers.contains(sender)) {
            // L'usuari que rep el missatge tenia el xat eliminat
            // Ha de tornar a apereixer el xat a l'usuari
            GatewayDeletedChats gDC = new GatewayDeletedChats(receiver, sender);
            gDC.remove();
        }
        deletedUsers = fDCS.findByUserDeleting(sender);
        if (deletedUsers.contains(receiver)) {
            // L'usuari que envia el missatge tenia el xat eliminat
            // Ha de tornar a apereixer el xat a l'usuari
            GatewayDeletedChats gDC = new GatewayDeletedChats(sender, receiver);
            gDC.remove();
        }
    }

    public boolean usersNotBlocked(String sender, String receiver) throws SQLException {
        FinderBlock fR = FinderBlock.getInstance();
        GatewayBlock gB1 = fR.findByPrimaryKey(sender, receiver);
        GatewayBlock gB2 = fR.findByPrimaryKey(receiver, sender);
        if (gB1 != null || gB2 != null) return false;
        else return true;
    }
}

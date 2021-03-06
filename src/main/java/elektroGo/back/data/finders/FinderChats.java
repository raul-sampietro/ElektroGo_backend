/**
 * @file FinderChats.java
 * @author Adria Abad
 * @date 13/04/2022
 * @brief Implementacio del Finder de Chats.
 */

package elektroGo.back.data.finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.gateways.GatewayChats;

import java.sql.*;
import java.util.ArrayList;

/**
 * @brief La classe FinderChats es l'encarregada de conectar-se amb la BD y retornar GW Chats.
 */
public class FinderChats {

    /**
     * @brief FinderChats, es un singleton
     */
    private static FinderChats singletonObject;

    /**
     * @brief Creadora de la classe FinderChats
     */
    private FinderChats() {}

    /**
     * @brief Funcio que retorna una instancia Singleton de FinderChats
     * @return Es retorna un singletonObject per treballar amb aquesta classe
     */
    public static FinderChats getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderChats();
        }
        return singletonObject;
    }

    /**
     * @brief Funció que agafa tots els Chats de la BD i els posa a un Array
     * @return Es retorna un array de GatewayChats amb tota la info dels Chats
     */
    public ArrayList<GatewayChats> findAll() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayChats> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM CHATS;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(this.createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Funcio que agafa un missatge en concret de la base de dades
     * @param sender nom de l'usuari que envia el missatge
     * @param receiver nom de l'usuari que rep el missatge
     * @param message contingut del missatge
     * @return Es retorna el GatewayChats amb la informacio del missatge indicat
     */
    public GatewayChats findByMessage(String sender, String receiver, String message) throws SQLException {
        GatewayChats gC = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM CHATS WHERE sender = ? AND receiver = ? AND message = ?;");
        ps.setString(1, sender);
        ps.setString(2, receiver);
        ps.setString(3, message);
        ResultSet r = ps.executeQuery();
        if (r.next()) gC = createGateway(r);
        return gC;
    }


    /**
     * @brief Funció que agafa tots els missatges entre els dos usuaris indicats
     * @param userA nom de l'usuari A
     * @param userB nom de l'usuari B
     * @return Es retorna un array de GatewayChats amb tota la info dels missatges ordenats per data
     */
    public ArrayList<GatewayChats> findByConversation(String userA, String userB) throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("" +
                "SELECT * FROM CHATS WHERE sender = ? and receiver = ?" +
                "UNION " +
                "SELECT * FROM CHATS WHERE sender = ? and receiver = ?" +
                "ORDER BY sentAt;");
        pS.setString(1,userA);
        pS.setString(2,userB);
        pS.setString(3,userB);
        pS.setString(4,userA);
        ResultSet r = pS.executeQuery();
        ArrayList<GatewayChats> aL = new ArrayList<>();
        while (r.next()) {
            aL.add(this.createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Funció que agafa tots els chats de l'usuari
     * @param user nom de l'usuari
     * @return Es retorna un llistat de chats
     */
    public ArrayList<String> findByUser(String user) throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("" +
                "SELECT sender as users FROM CHATS WHERE receiver = ? " +
                "UNION " +
                "SELECT receiver as users FROM CHATS WHERE sender = ? GROUP BY users;");
        pS.setString(1,user);
        pS.setString(2,user);
        ResultSet r = pS.executeQuery();
        ArrayList<String> chats = new ArrayList<>();
        while (r.next()) {
            String userB = r.getString(1);
            chats.add(userB);
        }
        return chats;
    }


    /**
     * @brief Crear un gateway amb els parametres passats
     * @param r ResultSet que contindra tots el parametres per poder crear el Gateway (sender, receiver, message, createdAt)
     * @return Retorna un GatewayChat amb els parametres donats
     */
    private GatewayChats createGateway(ResultSet r) throws SQLException {
        String sender = r.getString(1);
        String receiver = r.getString(2);
        String message = r.getString(3);
        String sentAt = r.getString(4);
        return new GatewayChats(sender, receiver, message, sentAt);
    }

}

/**
 * @file FinderDeletedChats.java
 * @author Adria Abad
 * @date 18/05/2022
 * @brief Implementacio del Finder de DeletedChats.
 */

package elektroGo.back.data.finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.gateways.GatewayDeletedChats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe FinderDeletedChats es l'encarregada de conectar-se amb la BD y retornar GW DeletedChats.
 */
public class FinderDeletedChats {

    /**
     * @brief FinderDeletedChats, es un singleton
     */
    private static FinderDeletedChats singletonObject;

    /**
     * @brief Creadora de la classe FinderDeletedChats
     */
    private FinderDeletedChats() {}

    /**
     * @brief Funcio que retorna una instancia Singleton de FinderDeletedChats
     * @return Es retorna un singletonObject per treballar amb aquesta classe
     */
    public static FinderDeletedChats getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderDeletedChats();
        }
        return singletonObject;
    }

    /**
     * @brief Funció que agafa tots els xats eliminats d'un usuari
     * @param userA nom de l'usuari A
     * @return Es retorna un array de GatewayDeletedChats amb tota la info dels xats eliminats
     */
    public ArrayList<String> findByUserDeleting(String userA) throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("" +
                "SELECT userB FROM DELETEDCHATS WHERE UserA = ?;");
        pS.setString(1,userA);
        ResultSet r = pS.executeQuery();
        ArrayList<String> deletedChats = new ArrayList<>();
        while (r.next()) {
            String userB = r.getString(1);
            deletedChats.add(userB);
        }
        return deletedChats;
    }


    public ArrayList<GatewayDeletedChats> findByMutualDelete(String userA, String userB) throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("" +
                "SELECT * FROM DELETEDCHATS WHERE UserA = ? AND UserB = ?" +
                "UNION " +
                "SELECT * FROM DELETEDCHATS WHERE userA = ? AND UserB = ?; ");
        pS.setString(1,userA);
        pS.setString(2, userB);
        pS.setString(3,userB);
        pS.setString(4, userA);
        ResultSet r = pS.executeQuery();
        ArrayList<GatewayDeletedChats> deletedChats = new ArrayList<>();
        while (r.next()) {
            deletedChats.add(this.createGateway(r));
        }
        return deletedChats;
    }

    /**
     * @brief Crear un gateway amb els parametres passats
     * @param r ResultSet que contindra tots el parametres per poder crear el Gateway (userA, userB)
     * @return Retorna un GatewayChat amb els parametres donats
     */
    private GatewayDeletedChats createGateway(ResultSet r) throws SQLException {
        String userA = r.getString(1);
        String userB = r.getString(2);
        return new GatewayDeletedChats(userA, userB);
    }

}

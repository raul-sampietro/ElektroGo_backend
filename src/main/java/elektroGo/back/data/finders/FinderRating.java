/**
 * @file FinderRating.java
 * @author Daniel Pulido
 * @date 12/04/2022
 * @brief Implementacio del Finder de Rating.
 */

package elektroGo.back.data.finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.gateways.GatewayRating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe FinderRating es l'encarregada de conectar-se amb la BD y retornar GW Rating.
 */
public class FinderRating {
    /**
     * @brief FinderRating, es un singleton
     */
    private static FinderRating singletonObject;

    /**
     * @brief Creadora de la clase FinderRating
     */
    private FinderRating() {}

    /**
     * @brief Funció que retorna una instancia Singleton de FinderRating
     * @return Es retorna un singletonObject per treballar amb aquesta classe
     */
    public static FinderRating getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderRating();
        }
        return singletonObject;
    }

    /**
     * @brief Crear un gateway amb els parametres passats
     * @param r ResultSet que contindra tots el parametres per poder crear el GatewayRating
     * @return Retorna un GatewayRating amb els parametres donats
     */
    private GatewayRating createGateway(ResultSet r) throws SQLException {
        String userWhoRates = r.getString(1);
        String ratedUser = r.getString(2);
        int points = r.getInt(3);
        String comment = r.getString(4);
        return new GatewayRating(userWhoRates,ratedUser,points,comment);
    }

    /**
     * @brief Funció que agafa tots els Ratings de la BD i els posa a un Array
     * @return Es retorna un array de GatewayRating amb tota la info dels Ratings
     */
    public ArrayList<GatewayRating> findAll() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayRating> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM RATING;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Funció que agafa tots els Ratings de la BD en els quals l'usuari valorador te com username "userWhoRates" i els retorna
     * @param userWhoRates Usuari valorador
     * @return Es retorna un array de GatewayRating que conte els ratings on l'usuari valorador te com username "userWhoRates"
     */
    public ArrayList<GatewayRating> findByUserWhoRates(String userWhoRates) throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayRating> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM RATING WHERE userWhoRates = ?;");
        pS.setString(1,userWhoRates);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Funció que agafa tots els Ratings de la BD en els quals l'usuari valorat te com username "ratedUser" i els retorna
     * @param ratedUser Usuari valorat
     * @return Es retorna un array de GatewayRating que conte els ratings on l'usuari valorat te com username "ratedUser"
     */
    public ArrayList<GatewayRating> findByRatedUser(String ratedUser) throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayRating> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM RATING WHERE ratedUser = ?;");
        pS.setString(1,ratedUser);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Funció que agafa el Rating de la BD identificat pels parametres de a continuacio
     * @param userWhoRates Usuari que fa la valoracio
     * @param ratedUser Usuari valorat
     * @return Es retorna un GatewayRating que conte el rating identificat pels parametres especificats
     */
    public GatewayRating findByPrimaryKey(String userWhoRates, String ratedUser) throws SQLException {
        GatewayRating gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM RATING WHERE userWhoRates = ? and ratedUser = ?;");
        pS.setString(1,userWhoRates);
        pS.setString(2, ratedUser);
        ResultSet r = pS.executeQuery();
        if (r.next()) gV = createGateway(r);
        return gV;
    }


}


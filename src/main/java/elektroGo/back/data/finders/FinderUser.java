/**
 * @file FinderUser.java
 * @author Gerard Castell
 * @date 10/03/2023
 * @brief Implementació de la classe FinderUser
 */

package elektroGo.back.data.finders;

import elektroGo.back.data.gateways.GatewayUser;
import elektroGo.back.data.Database;


import java.sql.*;
import java.util.ArrayList;

/**
 * @brief La classe FinderUser es l'encarregada de conectar-se amb la BD y retornar GW User.
 */
public class FinderUser {

    //PRIVATE
    /**
     * @brief Singleton del FinderUser
     */
    private static FinderUser singletonObject;

    /**
     * @brief Creadorà de la clase FinderUser
     */
    private FinderUser() {}

    //PUBLIC
    /**
     * @brief Funció que retorna una instancia Singleton de FinderUser
     * @return Es retorna un singletonObject per treballar amb aquesta classe
     */
    public static FinderUser getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderUser();
        }
        return singletonObject;
    }

    /**
     * @brief Funció que agafa tots els users de la BD i els posa a un Array
     * @return Es retorna un array de GatewayUsers amb tota la info dels Users
     */
    public ArrayList<GatewayUser> findAll() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayUser> gusers = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERS;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            gusers.add(createGateway(r));
        }
        return gusers;
    }

    /**
     * @brief Funció que agafa un user de la BD i el retorna
     * @param username Usuari del qual volem agafar la info
     * @return Es retorna un GatewayUser amb tota la info del User
     */
    public GatewayUser findByUsername(String username) throws SQLException {
        GatewayUser gU = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERS WHERE username = ?;");
        pS.setString(1, username);
        ResultSet r = pS.executeQuery();
        if (r.next()) gU = createGateway(r);
        return gU;
    }

    /**
     * @brief Funció que crea un GatewayUser i el retorna
     * @param r que és un ResultSet amb la info d'un User
     * @return Es retorna un GatewayUser amb tota la info del User creat.
     */
    private GatewayUser createGateway(ResultSet r) throws SQLException {
        String id = r.getString(1);
        String provider = r.getString(2);
        String  username = r.getString(3);
        String email = r.getString(4);
        String name = r.getString(5);
        String givenName = r.getString(6);
        String familyName = r.getString(7);
        String imageUrl = r.getString(8);

        return new GatewayUser(id, provider, username, email,name,givenName,familyName,imageUrl);
    }

}
/**
 * @file FinderUser.java
 * @author Gerard Castell
 * @date 10/03/2023
 * @brief Implementació de la classe FinderUser
 */


package elektroGo.back.data.Finders;

import elektroGo.back.data.Gateways.GatewayUser;
import elektroGo.back.data.Gateways.GatewayVehicle;
import elektroGo.back.data.Database;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @brief La classe Finder User es l'encarregada de conectarse amb la BD y retornar GW User.
 */
public class FinderUser {

    //PRIVATE
    /**
     * @brief FinderUser, es un singleton
     */
    private static FinderUser singletonObject;

    /**
     * @brief Creadorà de la clase FinderUser
     * @pre -
     * @post -
     */
    private FinderUser() {}

    //PUBLIC
    /**
     * @brief Funció que retorna una instancia Singleton de Finder User
     * @pre -
     * @post -
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
     * @pre -
     * @post -
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
     * @param userName Usuari del qual volem agafar la info
     * @pre -
     * @post -
     * @return Es retorna un GatewayUser amb tota la info del User
     */
    public GatewayUser findByUserName(String userName) throws SQLException {
        GatewayUser gU = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERS WHERE userName = ?;");
        pS.setString(1, userName);
        ResultSet r = pS.executeQuery();
        if (r.next()) gU = createGateway(r);
        return gU;
    }

    /**
     * @brief Funció que crea un GatewayUser i el retorna
     * @param r que és un ResultSet amb la info d'un User
     * @pre -
     * @post -
     * @return Es retorna un GatewayUser amb tota la info del User creat.
     */
    private GatewayUser createGateway(ResultSet r) throws SQLException {
        String userName = r.getString(1);
        String mail = r.getString(2);
        String password = r.getString(3);
        return new GatewayUser(userName, mail, password);
    }

}
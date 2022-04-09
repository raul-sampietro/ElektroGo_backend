/**
 * @file FinderDriver.java
 * @author Gerard Castell
 * @date 10/03/2023
 * @brief Implementació de la classe FinderDriver
 */


package elektroGo.back.data.finders;

import elektroGo.back.data.gateways.GatewayDriver;
import elektroGo.back.data.Database;


import java.sql.*;
import java.util.ArrayList;

/**
 * @brief La classe FinderDriver es l'encarregada de conectar-se amb la BD y retornar GW Driver.
 */

public class FinderDriver {

    //PRIVATE
    /**
     * @brief FinderDriver, es un singleton
     */
    private static FinderDriver singletonObject;

    /**
     * @brief Creadora de la clase FinderDriver
     */
    private FinderDriver() {
    }

    //PUBLIC
    /**
     * @brief Funció que retorna una instancia Singleton de Finder Driver
     * @return Es retorna un singletonObject per treballar amb aquesta clase
     */
    public static FinderDriver getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderDriver();
        }
        return singletonObject;
    }

    /**
     * @brief Funció que agafa tots els drivers de la BD i els posa a un Array
     * @return Es retorna un array de GatewayDrivers amb tota la info dels Drivers
     */
    public ArrayList<GatewayDriver> findAll() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayDriver> gdriver = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM DRIVER;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            gdriver.add(createGateway(r));
        }
        return gdriver;
    }

    /**
     * @brief Funció que agafa un drivers de la BD i el retorna
     * @param userName Usuari del qual volem agafar la info
     * @return Es retorna un GatewayDriver amb tota la info del Driver
     */
    public GatewayDriver findByUserName(String userName) throws SQLException {
        GatewayDriver gU = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM DRIVER WHERE userName = ?;");
        pS.setString(1, userName);
        ResultSet r = pS.executeQuery();
        if (r.next()) gU = createGateway(r);
        return gU;
    }

    /**
     * @brief Funció que crea un GatewayDriver i el retorna
     * @param r que es un ResultSet amb la info de un Driver
     * @return Es retorna un GatewayDriver amb tota la info del Driver creat.
     */
    private GatewayDriver createGateway(ResultSet r) throws SQLException {
        String userName = r.getString(1);
        return new GatewayDriver(userName);
    }

}
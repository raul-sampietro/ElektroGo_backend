/**
 * @file FinderCanceledTrip.java
 * @author Gerard Castell
 * @date 22/05/2023
 * @brief Implementació de la classe FinderDriver
 */


package elektroGo.back.data.finders;

import elektroGo.back.data.gateways.GatewayCanceledTrip;
import elektroGo.back.data.gateways.GatewayDriver;
import elektroGo.back.data.Database;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @brief La classe Finder es l'encarregada de conectar-se amb la BD y retornar GW CanceledTrip.
 */

public class FinderCanceledTrip {

    //PRIVATE
    /**
     * @brief Finder, es un singleton
     */
    private static FinderCanceledTrip singletonObject;

    /**
     * @brief Creadora de la clase
     */
    private FinderCanceledTrip() {
    }

    //PUBLIC
    /**
     * @brief Funció que retorna una instancia Singleton de Finde
     * @return Es retorna un singletonObject per treballar amb aquesta clase
     */
    public static FinderCanceledTrip getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderCanceledTrip();
        }
        return singletonObject;
    }

    /**
     * @brief Funció que agafa tots els canceled trip de la BD i els posa a un Array
     * @return Es retorna un array de GatewayCT amb tota la info
     */
    public ArrayList<GatewayCanceledTrip> findAll() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayCanceledTrip> gdriver = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM CANCELEDTRIP;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            gdriver.add(createGateway(r));
        }
        return gdriver;
    }

    /**
     * @brief Funció que agafa un drivers de la BD i el retorna
     * @param id id trip
     * @return Es retorna un GatewaCT amb tota la info del canceled Trip
     */
    public GatewayCanceledTrip findByID(Integer id) throws SQLException {
        GatewayCanceledTrip gU = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM CANCELEDTRIP WHERE id = ?;");
        pS.setInt(1, id);
        ResultSet r = pS.executeQuery();
        if (r.next()) gU = createGateway(r);
        return gU;
    }

    /**
     * @brief Funció que crea un GatewaCT i el retorna
     * @param r que es un ResultSet amb la info de un CT
     * @return Es retorna un GatewayCT amb tota la info del CT creat.
     */
    private GatewayCanceledTrip createGateway(ResultSet r) throws SQLException {
        Integer id = r.getInt(1);
        LocalDate dayCanceled = r.getDate(2).toLocalDate();
        String reason = r.getString(3);
        return new GatewayCanceledTrip(id,dayCanceled,reason);
    }

}
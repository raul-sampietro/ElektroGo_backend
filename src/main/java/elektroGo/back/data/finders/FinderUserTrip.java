/**
 * @file FinderUserTrip.java
 * @author Gerard Castell
 * @date 28/04/2022
 * @brief Implementacio del Finder de UserTrip
 */

package elektroGo.back.data.finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.gateways.GatewayUserTrip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe FinderDriverVehicle es l'encarregada de conectar-se amb la BD y retornar GW DriverVehicle
 */
public class FinderUserTrip {

    /**
     * @brief Singleton del Finder
     */
    private static FinderUserTrip singletonObject;

    /**
     * @brief Creadora de la clase Finder
     */
    private FinderUserTrip() {}

    /**
     * @brief Funcio que retorna una instancia Singleton de Finder
     * @return Es retorna un singletonObject per treballar amb aquesta classe
     */
    public static FinderUserTrip getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderUserTrip();
        }
        return singletonObject;
    }

    /**
     * @brief Crear un gateway amb els parametres passats
     * @param r ResultSet que contindra tots el parametres per poder crear el Gateway
     * @return Retorna un GatewayDriverVehicle amb els parametres donats
     */
    private GatewayUserTrip createGateway(ResultSet r) throws SQLException {
        Integer id = r.getInt(1);
        String username = r.getString(2);
        return new GatewayUserTrip(id,username);
    }

    /**
     * @brief Retorna totes les instancies que hi ha a la base de dades
     * @return Retorna un array amb totes les instancies
     */
    public ArrayList<GatewayUserTrip> findAll() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayUserTrip> aUT = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERTRIP;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aUT.add(createGateway(r));
        }
        return aUT;
    }

    /**
     * @brief Retorna la instacia de UserTrip seguint els identificadors donats
     * @param id identificador de trip
     * @param username identificador de user
     * @return Retorna un GatewayTripUser amb tota la info del UserTrip
     */
    public GatewayUserTrip findByTripUser(Integer id, String username) throws SQLException {
        GatewayUserTrip gUT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERTRIP WHERE id = ? and username = ?;");
        pS.setInt(1,id);
        pS.setString(2,username);
        ResultSet r = pS.executeQuery();
        if (r.next()) gUT = createGateway(r);
        return gUT;
    }

    /**
     * @brief Funcio que busca tots els trips a partir d'un usuari
     * @param username identificador de l'usuari
     * @return Retorna un array de GW amb info UserTrip
     */
    public ArrayList<GatewayUserTrip> findTripByUser(String username) throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERTRIP WHERE username = ?;");
        pS.setString(1,username);
        ResultSet r = pS.executeQuery();
        ArrayList<GatewayUserTrip> aL = new ArrayList<>();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Funcio que busca tots els users a partir d'un Trip
     * @param id identificador del Trip
     * @return Retorna un array de GatewayUserTrip amb tota la info
     */
    public ArrayList<GatewayUserTrip> findByTrip(Integer id) throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayUserTrip> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERTRIP WHERE id = ?;");
        pS.setInt(1,id);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Funcio que busca a partir d'un conductor
     * @return Retorna un array de GatewayUserTrip amb tota la info
     */
    public ArrayList<GatewayUserTrip> findDrivers() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayUserTrip> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT u.id,u.username" +
                " FROM USERTRIP u" +
                " INNER JOIN TRIP t " +
                "ON u.username = t.username WHERE u.id = t.id;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    public ArrayList<GatewayUserTrip> findByDriver(String username) throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayUserTrip> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT u.id,u.username" +
                " FROM USERTRIP u" +
                " INNER JOIN TRIP t " +
                "ON u.username = t.username WHERE u.id = t.id and u.username=?;");
        pS.setString(1,username);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }




}
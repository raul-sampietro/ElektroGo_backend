/**
 * @file FinderUser.java
 * @author Gerard Castell
 * @date 15/04/2022
 * @brief Implementació de la classe FinderTrip
 */
package elektroGo.back.data.finders;

import elektroGo.back.data.gateways.GatewayChargingStations;
import elektroGo.back.data.gateways.GatewayTrip;
import elektroGo.back.data.Database;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


/**
 * @brief La classe FinderTrip es l'encarregada de conectar-se amb la BD y retornar GW Trip.
 */
public class FinderTrip {

    //PRIVATE
    /**
     * @brief Singleton del FinderTrip
     */
    private static FinderTrip singletonObject;

    /**
     * @brief Creadorà de la clase FinderTrip
     */
    private FinderTrip() {}

    //PUBLIC
    /**
     * @brief Funció que retorna una instancia Singleton de FinderTrip
     * @return Es retorna un singletonObject per treballar amb aquesta classe
     */
    public static FinderTrip getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderTrip();
        }
        return singletonObject;
    }

    /**
     * @brief Funció que agafa tots els trips de la BD i els posa a un Array
     * @return Es retorna un array de GatewayTrips amb tota la info dels Trips
     */
    public ArrayList<GatewayTrip> findAll() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayTrip> gtrip = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM TRIP;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            gtrip.add(createGateway(r));
        }
        return gtrip;
    }

    /**
     * @brief Funció que agafa un trip de la BD i el retorna
     * @param id del qual volem agafar la info
     * @return Es retorna un GatewayTrip amb tota la info del Trip
     */
    public GatewayTrip findById(String id) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM TRIP WHERE id = ?;");
        pS.setString(1, id);
        ResultSet r = pS.executeQuery();
        if (r.next()) gT = createGateway(r);
        return gT;
    }

    /**
     * @brief Funció que agafa tots els trips de la BD i els posa a un Array
     * @return Es retorna un array de GatewayTrips amb tota la info dels Trips
     */
    public ArrayList<GatewayTrip> findByCoordinates(BigDecimal lat1, BigDecimal long1, BigDecimal lat2, BigDecimal long2) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayTrip> corT = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM TRIP WHERE LatitudeOrigin < ? and latitudeOrigin > ? and longitudeOrigin < ? and LongitudeOrigin > ?;");
        pS.setBigDecimal(1,lat1);
        pS.setBigDecimal(2,lat2);
        pS.setBigDecimal(3,long1);
        pS.setBigDecimal(4,long2);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            corT.add(createGateway(r));
        }
        return corT;
    }

    /**
     * @brief Funció que crea un GatewayTrip i el retorna
     * @param r que és un ResultSet amb la info d'un Trip
     * @return Es retorna un GatewayTrip amb tota la info del Trip creat.
     */
    private GatewayTrip createGateway(ResultSet r) throws SQLException {
        String id = r.getString(1);
        LocalDate startDate = r.getDate(2).toLocalDate();
        Time startTime = r.getTime(3);
        Integer oferredSeats = r.getInt(4);
        Integer ocupiedSeats = r.getInt(5);
        String restrictions = r.getString(6);
        String details = r.getString(7);
        LocalDate cancelDate = r.getDate(8).toLocalDate();
        String npVehicle = r.getString(9);
        String origin = r.getString(10);
        String destination = r.getString(11);
        BigDecimal latitudeOrigin = r.getBigDecimal(12);
        BigDecimal longitudeOrigin = r.getBigDecimal(13);
        BigDecimal latitudeDestination = r.getBigDecimal(14);
        BigDecimal longitudeDestination = r.getBigDecimal(15);
        return new GatewayTrip(id,startDate,startTime, oferredSeats ,ocupiedSeats,restrictions,details, cancelDate,
                npVehicle, origin, destination,latitudeOrigin, longitudeOrigin, latitudeDestination, longitudeDestination);
    }

}
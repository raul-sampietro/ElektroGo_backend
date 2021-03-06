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
     * @brief Funció que agafa tots els trips de la BD i els posa a un Array
     * @return Es retorna un array de GatewayTrips amb tota la info dels Trips
     */
    public ArrayList<GatewayTrip> findOrdered() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayTrip> gtrip = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("select t.id, t.startDate, t.startTime, " +
                "t.offeredSeats,t.occupiedSeats,t.restrictions, t.details,t.CancelDate," +
                "t.vehicleNumberPlate,t.origin,t.destination,t.LatitudeOrigin," +
                "t.LongitudeOrigin,t.LatitudeDestination,t.LongitudeDestination,t.username," +
                "AVG(r.points) as avgpoints " +
                "from TRIP t " +
                "LEFT OUTER JOIN RATING  r " +
                "ON t.username = r.ratedUser " +
                "WHERE t.startDate > CURDATE() or ( t.startDate = CURDATE()  and  t.startTime >= CURTIME() )"+
                "group by t.id " +
                "order by avgpoints desc;");
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
    public GatewayTrip findById(Integer id) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM TRIP WHERE id = ?;");
        pS.setInt(1, id);
        ResultSet r = pS.executeQuery();
        if (r.next()) gT = createGateway(r);
        return gT;
    }
    public GatewayTrip findByUser(String username, LocalDate startDate, Time startTime) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM TRIP WHERE username= ? AND startDate = ? AND startTime = ?;");
        pS.setString(1, username);
        pS.setDate(2, Date.valueOf(startDate));
        pS.setTime(3, startTime);
        ResultSet r = pS.executeQuery();
        if (r.next()) gT = createGateway(r);
        return gT;
    }

    /**
     * @brief Funció que agafa tots els trips de la BD i els posa a un Array
     * @return Es retorna un array de GatewayTrips amb tota la info dels Trips
     */
    public ArrayList<GatewayTrip> findByCoordinates(BigDecimal lat1,BigDecimal lat2,BigDecimal long1 ,BigDecimal long2) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayTrip> corT = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM TRIP WHERE LatitudeOrigin BETWEEN ? AND ? and longitudeOrigin BETWEEN ? AND ? and startDate > CURDATE() or ( startDate = CURDATE()  and  startTime >= CURTIME() ) ;");
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

    public ArrayList<GatewayTrip> findByNot(BigDecimal latO1,BigDecimal latO2,BigDecimal longO1 ,BigDecimal longO2,BigDecimal lat1,BigDecimal lat2,BigDecimal long1 ,BigDecimal long2) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayTrip> corT = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement(("select t.id, t.startDate, t.startTime, " +
                        "t.offeredSeats,t.occupiedSeats,t.restrictions, t.details,t.CancelDate," +
                        "t.vehicleNumberPlate,t.origin,t.destination,t.LatitudeOrigin," +
                        "t.LongitudeOrigin,t.LatitudeDestination,t.LongitudeDestination,t.username," +
                        "AVG(r.points) as avgpoints " +
                        "from TRIP t " +
                        "LEFT OUTER JOIN RATING  r " +
                        "ON t.username = r.ratedUser " +
                        "WHERE LatitudeOrigin BETWEEN ? AND ? and longitudeOrigin BETWEEN ? AND ? and LatitudeDestination BETWEEN ? AND ? and LongitudeDestination BETWEEN ? AND ?"
                        +"group by t.id " +
                        "order by avgpoints desc;"));
        pS.setBigDecimal(1,latO1);
        pS.setBigDecimal(2,latO2);
        pS.setBigDecimal(3,longO1);
        pS.setBigDecimal(4,longO2);
        pS.setBigDecimal(5,lat1);
        pS.setBigDecimal(6,lat2);
        pS.setBigDecimal(7,long1);
        pS.setBigDecimal(8,long2);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            corT.add(createGateway(r));
        }
        return corT;
    }

    public ArrayList<GatewayTrip> findByMin(BigDecimal latO1,BigDecimal latO2,BigDecimal longO1 ,BigDecimal longO2,BigDecimal lat1,BigDecimal lat2,BigDecimal long1 ,BigDecimal long2, Time min) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayTrip> corT = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement(("select t.id, t.startDate, t.startTime, " +
                "t.offeredSeats,t.occupiedSeats,t.restrictions, t.details,t.CancelDate," +
                "t.vehicleNumberPlate,t.origin,t.destination,t.LatitudeOrigin," +
                "t.LongitudeOrigin,t.LatitudeDestination,t.LongitudeDestination,t.username," +
                "AVG(r.points) as avgpoints " +
                "from TRIP t " +
                "LEFT OUTER JOIN RATING  r " +
                "ON t.username = r.ratedUser " +
                "WHERE LatitudeOrigin BETWEEN ? AND ? and longitudeOrigin BETWEEN ? AND ? and LatitudeDestination BETWEEN ? AND ? and LongitudeDestination BETWEEN ? AND ? and startTime >= ?"
                +"group by t.id " +
                "order by avgpoints desc;"));
        pS.setBigDecimal(1,latO1);
        pS.setBigDecimal(2,latO2);
        pS.setBigDecimal(3,longO1);
        pS.setBigDecimal(4,longO2);
        pS.setBigDecimal(5,lat1);
        pS.setBigDecimal(6,lat2);
        pS.setBigDecimal(7,long1);
        pS.setBigDecimal(8,long2);
        pS.setTime(9,min);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            corT.add(createGateway(r));
        }
        return corT;
    }

    public ArrayList<GatewayTrip> findByMax(BigDecimal latO1,BigDecimal latO2,BigDecimal longO1 ,BigDecimal longO2,BigDecimal lat1,BigDecimal lat2,BigDecimal long1 ,BigDecimal long2, Time max) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayTrip> corT = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement(("select t.id, t.startDate, t.startTime, " +
                "t.offeredSeats,t.occupiedSeats,t.restrictions, t.details,t.CancelDate," +
                "t.vehicleNumberPlate,t.origin,t.destination,t.LatitudeOrigin," +
                "t.LongitudeOrigin,t.LatitudeDestination,t.LongitudeDestination,t.username," +
                "AVG(r.points) as avgpoints " +
                "from TRIP t " +
                "LEFT OUTER JOIN RATING  r " +
                "ON t.username = r.ratedUser " +
                "WHERE LatitudeOrigin BETWEEN ? AND ? and longitudeOrigin BETWEEN ? AND ? and LatitudeDestination BETWEEN ? AND ? and LongitudeDestination BETWEEN ? AND ? and startTime <= ?"
                +"group by t.id " +
                "order by avgpoints desc;"));
        pS.setBigDecimal(1,latO1);
        pS.setBigDecimal(2,latO2);
        pS.setBigDecimal(3,longO1);
        pS.setBigDecimal(4,longO2);
        pS.setBigDecimal(5,lat1);
        pS.setBigDecimal(6,lat2);
        pS.setBigDecimal(7,long1);
        pS.setBigDecimal(8,long2);
        pS.setTime(9,max);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            corT.add(createGateway(r));
        }
        return corT;
    }

    public ArrayList<GatewayTrip> findByMaxMin(BigDecimal latO1,BigDecimal latO2,BigDecimal longO1 ,BigDecimal longO2,BigDecimal lat1,BigDecimal lat2,BigDecimal long1 ,BigDecimal long2, Time max, Time min) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayTrip> corT = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement(("select t.id, t.startDate, t.startTime, " +
                "t.offeredSeats,t.occupiedSeats,t.restrictions, t.details,t.CancelDate," +
                "t.vehicleNumberPlate,t.origin,t.destination,t.LatitudeOrigin," +
                "t.LongitudeOrigin,t.LatitudeDestination,t.LongitudeDestination,t.username," +
                "AVG(r.points) as avgpoints " +
                "from TRIP t " +
                "LEFT OUTER JOIN RATING  r " +
                "ON t.username = r.ratedUser " +
                "WHERE LatitudeOrigin BETWEEN ? AND ? and longitudeOrigin BETWEEN ? AND ? and LatitudeDestination BETWEEN ? AND ? and LongitudeDestination BETWEEN ? AND ? and startTime BETWEEN ? AND ?"
                +"group by t.id " +
                "order by avgpoints desc;"));
        pS.setBigDecimal(1,latO1);
        pS.setBigDecimal(2,latO2);
        pS.setBigDecimal(3,longO1);
        pS.setBigDecimal(4,longO2);
        pS.setBigDecimal(5,lat1);
        pS.setBigDecimal(6,lat2);
        pS.setBigDecimal(7,long1);
        pS.setBigDecimal(8,long2);
        pS.setTime(10,max);
        pS.setTime(9,min);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            corT.add(createGateway(r));
        }
        return corT;
    }
    public ArrayList<GatewayTrip> findByDat(BigDecimal latO1,BigDecimal latO2,BigDecimal longO1 ,BigDecimal longO2,BigDecimal lat1,BigDecimal lat2,BigDecimal long1 ,BigDecimal long2, LocalDate dat) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayTrip> corT = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement(("select t.id, t.startDate, t.startTime, " +
                "t.offeredSeats,t.occupiedSeats,t.restrictions, t.details,t.CancelDate," +
                "t.vehicleNumberPlate,t.origin,t.destination,t.LatitudeOrigin," +
                "t.LongitudeOrigin,t.LatitudeDestination,t.LongitudeDestination,t.username," +
                "AVG(r.points) as avgpoints " +
                "from TRIP t " +
                "LEFT OUTER JOIN RATING  r " +
                "ON t.username = r.ratedUser " +
                "WHERE LatitudeOrigin BETWEEN ? AND ? and longitudeOrigin BETWEEN ? AND ? and LatitudeDestination BETWEEN ? AND ? and LongitudeDestination BETWEEN ? AND ? and startDate = ?"
                +"group by t.id " +
                "order by avgpoints desc;"));
        pS.setBigDecimal(1,latO1);
        pS.setBigDecimal(2,latO2);
        pS.setBigDecimal(3,longO1);
        pS.setBigDecimal(4,longO2);
        pS.setBigDecimal(5,lat1);
        pS.setBigDecimal(6,lat2);
        pS.setBigDecimal(7,long1);
        pS.setBigDecimal(8,long2);
        pS.setDate(9, Date.valueOf(dat));
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            corT.add(createGateway(r));
        }
        return corT;
    }

    public ArrayList<GatewayTrip> findByDatMin(BigDecimal latO1,BigDecimal latO2,BigDecimal longO1 ,BigDecimal longO2,BigDecimal lat1,BigDecimal lat2,BigDecimal long1 ,BigDecimal long2, LocalDate dat,Time min) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayTrip> corT = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement(("select t.id, t.startDate, t.startTime, " +
                "t.offeredSeats,t.occupiedSeats,t.restrictions, t.details,t.CancelDate," +
                "t.vehicleNumberPlate,t.origin,t.destination,t.LatitudeOrigin," +
                "t.LongitudeOrigin,t.LatitudeDestination,t.LongitudeDestination,t.username," +
                "AVG(r.points) as avgpoints " +
                "from TRIP t " +
                "LEFT OUTER JOIN RATING  r " +
                "ON t.username = r.ratedUser " +
                "WHERE LatitudeOrigin BETWEEN ? AND ? and longitudeOrigin BETWEEN ? AND ? and LatitudeDestination BETWEEN ? AND ? and LongitudeDestination BETWEEN ? AND ? and startDate = ? and startTime >= ?"
                +"group by t.id " +
                "order by avgpoints desc;"));
        pS.setBigDecimal(1,latO1);
        pS.setBigDecimal(2,latO2);
        pS.setBigDecimal(3,longO1);
        pS.setBigDecimal(4,longO2);
        pS.setBigDecimal(5,lat1);
        pS.setBigDecimal(6,lat2);
        pS.setBigDecimal(7,long1);
        pS.setBigDecimal(8,long2);
        pS.setDate(9, Date.valueOf(dat));
        pS.setTime(10,min);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            corT.add(createGateway(r));
        }
        return corT;
    }

    public ArrayList<GatewayTrip> findByDatMax(BigDecimal latO1,BigDecimal latO2,BigDecimal longO1 ,BigDecimal longO2,BigDecimal lat1,BigDecimal lat2,BigDecimal long1 ,BigDecimal long2, LocalDate dat,Time max) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayTrip> corT = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement(("select t.id, t.startDate, t.startTime, " +
                "t.offeredSeats,t.occupiedSeats,t.restrictions, t.details,t.CancelDate," +
                "t.vehicleNumberPlate,t.origin,t.destination,t.LatitudeOrigin," +
                "t.LongitudeOrigin,t.LatitudeDestination,t.LongitudeDestination,t.username," +
                "AVG(r.points) as avgpoints " +
                "from TRIP t " +
                "LEFT OUTER JOIN RATING  r " +
                "ON t.username = r.ratedUser " +
                "WHERE LatitudeOrigin BETWEEN ? AND ? and longitudeOrigin BETWEEN ? AND ? and LatitudeDestination BETWEEN ? AND ? and LongitudeDestination BETWEEN ? AND ? and startDate = ? and startTime <= ?"
                +"group by t.id " +
                "order by avgpoints desc;"));
        pS.setBigDecimal(1,latO1);
        pS.setBigDecimal(2,latO2);
        pS.setBigDecimal(3,longO1);
        pS.setBigDecimal(4,longO2);
        pS.setBigDecimal(5,lat1);
        pS.setBigDecimal(6,lat2);
        pS.setBigDecimal(7,long1);
        pS.setBigDecimal(8,long2);
        pS.setDate(9, Date.valueOf(dat));
        pS.setTime(10,max);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            corT.add(createGateway(r));
        }
        return corT;
    }

    public ArrayList<GatewayTrip> findByDatMaxMin(BigDecimal latO1,BigDecimal latO2,BigDecimal longO1 ,BigDecimal longO2,BigDecimal lat1,BigDecimal lat2,BigDecimal long1 ,BigDecimal long2, LocalDate dat,Time max,Time min) throws SQLException {
        GatewayTrip gT = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayTrip> corT = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement(("select t.id, t.startDate, t.startTime, " +
                "t.offeredSeats,t.occupiedSeats,t.restrictions, t.details,t.CancelDate," +
                "t.vehicleNumberPlate,t.origin,t.destination,t.LatitudeOrigin," +
                "t.LongitudeOrigin,t.LatitudeDestination,t.LongitudeDestination,t.username," +
                "AVG(r.points) as avgpoints " +
                "from TRIP t " +
                "LEFT OUTER JOIN RATING  r " +
                "ON t.username = r.ratedUser " +
                "WHERE LatitudeOrigin BETWEEN ? AND ? and longitudeOrigin BETWEEN ? AND ? and LatitudeDestination BETWEEN ? AND ? and LongitudeDestination BETWEEN ? AND ? and startDate = ? and startTime <= ? and startTime >= ?"
                +"group by t.id " +
                "order by avgpoints desc;"));
        pS.setBigDecimal(1,latO1);
        pS.setBigDecimal(2,latO2);
        pS.setBigDecimal(3,longO1);
        pS.setBigDecimal(4,longO2);
        pS.setBigDecimal(5,lat1);
        pS.setBigDecimal(6,lat2);
        pS.setBigDecimal(7,long1);
        pS.setBigDecimal(8,long2);
        pS.setDate(9, Date.valueOf(dat));
        pS.setTime(10,max);
        pS.setTime(11,min);
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
        Integer id = r.getInt(1);
        LocalDate startDate = r.getDate(2).toLocalDate();
        Time startTime = r.getTime(3);
        Integer offeredSeats = r.getInt(4);
        Integer occupiedSeats = r.getInt(5);
        String restrictions = r.getString(6);
        String details = r.getString(7);
        LocalDate CancelDate = r.getDate(8).toLocalDate();
        String vehicleNumberPlate = r.getString(9);
        String origin = r.getString(10);
        String destination = r.getString(11);
        BigDecimal latitudeOrigin = r.getBigDecimal(12);
        BigDecimal longitudeOrigin = r.getBigDecimal(13);
        BigDecimal latitudeDestination = r.getBigDecimal(14);
        BigDecimal longitudeDestination = r.getBigDecimal(15);
        String username = r.getString(16);
        return new GatewayTrip(id,startDate,startTime, offeredSeats ,occupiedSeats,restrictions,details, CancelDate,
                vehicleNumberPlate, origin, destination,latitudeOrigin, longitudeOrigin, latitudeDestination, longitudeDestination, username);
    }

}
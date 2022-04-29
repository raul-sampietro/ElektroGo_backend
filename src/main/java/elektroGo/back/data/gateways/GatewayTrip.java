/**
 * @file GatewayUser.java
 * @author Gerard Castell
 * @date 15/04/2022
 * @brief Implementació de la classe GatewayUser
 */
package elektroGo.back.data.gateways;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import elektroGo.back.data.Database;
import java.sql.Time;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

/**
 * @brief La classe GatewayTrip implementa el Gateway Trip el qual te els atributs de Trip i fa insert/update/delete a la BD
 */
public class GatewayTrip implements Gateway{

    /**
     * @brief Username del User
     * id varchar(100),
     * 	startDate Date,
     * 	startTime Time,
     * 	offeredSeats integer,
     * 	occupiedSeats integer,
     * 	restrictions text,
     * 	CancelDate Date,
     * 	vehicleNumberPlate varchar(100),
     * 	Latitude varchar(100),
     * 	Longitude varchar(100),
     */
    private Integer id;
    private LocalDate startDate;
    private Time startTime;
    private Integer offeredSeats;
    private Integer occupiedSeats;
    private String restrictions;
    private String details;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private LocalDate CancelDate;
    private String vehicleNumberPlate;
    private String origin;
    private String destination;
    private BigDecimal LatitudeOrigin;
    private BigDecimal LongitudeOrigin;
    private BigDecimal LatitudeDestination;
    private BigDecimal LongitudeDestination;
    private String username;

    public GatewayTrip(){}
    /**
     * @brief Creadora de la Clase GatewayTrip amb tots els seus respectius parametres
     * @param id per identificar el viatge
     * @param CancelDate data del ultim dia de cancelacio
     * @param latitudeO longitud inici
     * @param longitudeO longitud inici
     * @param vehicleNumberPlate matricula del vehicle que fara el viatge
     * @param occupiedSeats seients ocupats
     * @param offeredSeats seients oferts
     * @param restrictions restriccions del viatge posades pel conductor
     * @param startDate data del viatge
     * @param startTime temps sortida viatge
     * @param destination destinacio final
     * @param origin origen
     * @param details detall ruta
     * @param longitudeD longitud desti
     * @param latitudeD latitude desti
     * @post Es crea un nou GWTrip amb els valors indicats
     */

    public GatewayTrip(Integer id, LocalDate startDate, Time startTime, Integer offeredSeats, Integer occupiedSeats, String restrictions, String details, LocalDate CancelDate, String vehicleNumberPlate,String origin, String destination, BigDecimal latitudeO, BigDecimal longitudeO,BigDecimal latitudeD, BigDecimal longitudeD,String username) {
        this.id = id;
        this.startDate = startDate;
        this.startTime = startTime;
        this.offeredSeats = offeredSeats;
        this.occupiedSeats = occupiedSeats;
        this.restrictions = restrictions;
        this.details = details;
        this.CancelDate = CancelDate;
        this.vehicleNumberPlate= vehicleNumberPlate;
        this.origin = origin;
        this.destination = destination;
        this.LatitudeOrigin = latitudeO;
        this.LongitudeOrigin = longitudeO;
        this.LatitudeDestination = latitudeD;
        this.LongitudeDestination = longitudeD;
        this.username = username;
    }

    public GatewayTrip(LocalDate startDate, Time startTime, Integer offeredSeats, Integer occupiedSeats, String restrictions, String details, LocalDate CancelDate, String vehicleNumberPlate,String origin, String destination, BigDecimal latitudeO, BigDecimal longitudeO,BigDecimal latitudeD, BigDecimal longitudeD, String username) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.offeredSeats = offeredSeats;
        this.occupiedSeats = occupiedSeats;
        this.restrictions = restrictions;
        this.details = details;
        this.CancelDate = CancelDate;
        this.vehicleNumberPlate = vehicleNumberPlate;
        this.origin = origin;
        this.destination = destination;
        this.LatitudeOrigin = latitudeO;
        this.LongitudeOrigin = longitudeO;
        this.LatitudeDestination = latitudeD;
        this.LongitudeDestination = longitudeD;
        this.username = username;
    }


    //Getters and Setters

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public LocalDate getStartDate() {return startDate;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}
    public Time getStartTime() {return startTime;}
    public void setStartTime(Time startTime) {this.startTime = startTime;}
    public Integer getOfferedSeats() {return offeredSeats;}
    public void setOfferedSeats(Integer offeredSeats) {this.offeredSeats = offeredSeats;}
    public Integer getOccupiedSeats() {return occupiedSeats;}
    public void setOccupiedSeats(Integer occupiedSeats) {this.occupiedSeats = occupiedSeats;}
    public String getRestrictions() {return restrictions;}
    public void setRestrictions(String restrictions) {this.restrictions = restrictions;}
    public String getDetails() {return details;}
    public void setDetails(String details) {this.details = details;}
    public LocalDate getCancelDate() {return CancelDate;}
    public void setCancelDate(LocalDate CancelDate) {this.CancelDate = CancelDate;}
    public String getVehicleNumberPlate() {return vehicleNumberPlate;}
    public void setVehicleNumberPlate(String vehicleNumberPlate) {this.vehicleNumberPlate = vehicleNumberPlate;}
    public String getOrigin() {return origin;}
    public void setOrigin(String origin) {this.origin = origin;}
    public String getDestination() {return destination;}
    public void setDestination(String destination) {this.destination = destination;}
    public BigDecimal getLatitudeOrigin() {return LatitudeOrigin;}
    public void setLatitudeOrigin(BigDecimal latitudeOrigin) {LatitudeOrigin = latitudeOrigin;}
    public BigDecimal getLongitudeOrigin() {return LongitudeOrigin;}
    public void setLongitudeOrigin(BigDecimal longitudeOrigin) {LongitudeOrigin = longitudeOrigin;}
    public BigDecimal getLatitudeDestination() {return LatitudeDestination;}
    public void setLatitudeDestination(BigDecimal latitudeDestination) {LatitudeDestination = latitudeDestination;}
    public BigDecimal getLongitudeDestination() {return LongitudeDestination;}
    public void setLongitudeDestination(BigDecimal longitudeDestination) {LongitudeDestination = longitudeDestination;}


    //SQL operations

    /**
     * @brief Coloca tota la info requerida  al PreparedStatement
     * @param pS PreparedStatement al que volem col·locar la info
     * @pre s'ens passa un pS
     * @post el pS queda assignat amb la info requerida
     */
    public void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setDate(1, Date.valueOf(startDate));
        pS.setTime(2,startTime);
        pS.setInt(3,offeredSeats);
        pS.setInt(4,occupiedSeats);
        pS.setString(5,restrictions);
        pS.setString(6,details);
        pS.setDate(7, Date.valueOf(CancelDate));
        pS.setString(8,vehicleNumberPlate);
        pS.setString(9,origin);
        pS.setString(10,destination);
        pS.setString(11, String.valueOf(LatitudeOrigin));
        pS.setString(12, String.valueOf(LongitudeOrigin));
        pS.setString(13, String.valueOf(LatitudeOrigin));
        pS.setString(14, String.valueOf(LongitudeOrigin));
        pS.setString(15,username);
    }

    public void setFullPreparedStatementUpdate(PreparedStatement pS) throws SQLException {
        pS.setDate(1, Date.valueOf(startDate));
        pS.setTime(2,startTime);
        pS.setInt(3,offeredSeats);
        pS.setInt(4,occupiedSeats);
        pS.setString(5,restrictions);
        pS.setString(6,details);
        pS.setDate(7, Date.valueOf(CancelDate));
        pS.setString(8,vehicleNumberPlate);
        pS.setString(9,origin);
        pS.setString(10,destination);
        pS.setString(11, String.valueOf(LatitudeOrigin));
        pS.setString(12, String.valueOf(LongitudeOrigin));
        pS.setString(13, String.valueOf(LatitudeDestination));
        pS.setString(14, String.valueOf(LongitudeDestination));
        pS.setString(16,username);
        pS.setInt(15,id);
    }


    /**
     * @brief Funció inserta a la BD un Trip
     * @post A la BD queda agefit el Trip
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO TRIP(startDate,startTime,offeredSeats,occupiedSeats,restrictions, details,CancelDate,vehicleNumberPlate,origin,destination,LatitudeOrigin,LongitudeOrigin,LatitudeDestination,LongitudeDestination,username) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Funció fa un update a un Trip de la BD
     * @post Es fa un Update del Trip
     */
    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE TRIP SET startDate = ?, startTime = ?, offeredSeats = ?," +
                "occupiedSeats = ?, restrictions = ?, details = ?, CancelDate = ?, vehicleNumberPlate = ?, origin = ?, destination = ?," +
                " LatitudeOrigin = ?, LongitudeOrigin = ?, LatitudeDestination = ?, LongitudeDestination = ?, username = ? WHERE id = ?");
        setFullPreparedStatementUpdate(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Funció elimina un Trip de la DB
     * @post A la BD queda eliminat el Trip
     */
    public void remove() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("DELETE FROM TRIP WHERE id='" + id + "';");
    }

    /**
     * @brief Funció converteix en un String json un Gateway
     * @post El GW esta en format String json
     * @return es retorna el String Json amb la info del GW
     */
    public String json() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = "";
        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
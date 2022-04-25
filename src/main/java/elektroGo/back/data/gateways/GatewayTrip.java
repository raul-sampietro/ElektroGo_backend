/**
 * @file GatewayUser.java
 * @author Gerard Castell
 * @date 15/04/2022
 * @brief Implementació de la classe GatewayUser
 */
package elektroGo.back.data.gateways;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     * 	oferredSeats integer,
     * 	ocupiedSeats integer,
     * 	restrictions text,
     * 	CancelDate Date,
     * 	nPVehicle varchar(100),
     * 	Latitude varchar(100),
     * 	Longitude varchar(100),
     */
    private String id;
    private LocalDate startDate;
    private Time startTime;
    private Integer oferredSeats;
    private Integer ocupiedSeats;
    private String restrictions;
    private String details;
    private LocalDate cancelDate;
    private String npVehicle;
    private String origin;
    private String destination;
    private BigDecimal LatitudeOrigin;
    private BigDecimal LongitudeOrigin;
    private BigDecimal LatitudeDestination;
    private BigDecimal LongitudeDestination;


    /**
     * @brief Creadora de la Clase GatewayTrip amb tots els seus respectius parametres
     * @param id per identificar el viatge
     * @param cancelDate data del ultim dia de cancelacio
     * @param latitudeO longitud inici
     * @param longitudeO longitud inici
     * @param npVehicle matricula del vehicle que fara el viatge
     * @param ocupiedSeats seients ocupats
     * @param oferredSeats seients oferts
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
    public GatewayTrip(String id, LocalDate startDate, Time startTime, Integer oferredSeats, Integer ocupiedSeats, String restrictions, String details, LocalDate cancelDate, String npVehicle,String origin, String destination, BigDecimal latitudeO, BigDecimal longitudeO,BigDecimal latitudeD, BigDecimal longitudeD) {
        this.id = id;
        this.startDate = startDate;
        this.startTime = startTime;
        this.oferredSeats = oferredSeats;
        this.ocupiedSeats = ocupiedSeats;
        this.restrictions = restrictions;
        this.details = details;
        this.cancelDate = cancelDate;
        this.npVehicle = npVehicle;
        this.origin = origin;
        this.destination = destination;
        this.LatitudeOrigin = latitudeO;
        this.LongitudeOrigin = longitudeO;
        this.LatitudeDestination = latitudeD;
        this.LongitudeDestination = longitudeD;
    }

    public GatewayTrip(LocalDate startDate, Time startTime, Integer oferredSeats, Integer ocupiedSeats, String restrictions, String details, LocalDate cancelDate, String npVehicle,String origin, String destination, BigDecimal latitudeO, BigDecimal longitudeO,BigDecimal latitudeD, BigDecimal longitudeD) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.oferredSeats = oferredSeats;
        this.ocupiedSeats = ocupiedSeats;
        this.restrictions = restrictions;
        this.details = details;
        this.cancelDate = cancelDate;
        this.npVehicle = npVehicle;
        this.origin = origin;
        this.destination = destination;
        this.LatitudeOrigin = latitudeO;
        this.LongitudeOrigin = longitudeO;
        this.LatitudeDestination = latitudeD;
        this.LongitudeDestination = longitudeD;
    }


    //Getters and Setters

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public LocalDate getStartDate() {return startDate;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}
    public Time getStartTime() {return startTime;}
    public void setStartTime(Time startTime) {this.startTime = startTime;}
    public Integer getOferredSeats() {return oferredSeats;}
    public void setOferredSeats(Integer oferredSeats) {this.oferredSeats = oferredSeats;}
    public Integer getOcupiedSeats() {return ocupiedSeats;}
    public void setOcupiedSeats(Integer ocupiedSeats) {this.ocupiedSeats = ocupiedSeats;}
    public String getRestrictions() {return restrictions;}
    public void setRestrictions(String restrictions) {this.restrictions = restrictions;}
    public String getDetails() {return details;}
    public void setDetails(String details) {this.details = details;}
    public LocalDate getCancelDate() {return cancelDate;}
    public void setCancelDate(LocalDate cancelDate) {this.cancelDate = cancelDate;}
    public String getNpVehicle() {return npVehicle;}
    public void setNpVehicle(String npVehicle) {this.npVehicle = npVehicle;}
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
        pS.setDate(2, Date.valueOf(startDate));
        pS.setTime(3,startTime);
        pS.setInt(4,oferredSeats);
        pS.setInt(5,ocupiedSeats);
        pS.setString(6,restrictions);
        pS.setString(7,details);
        pS.setDate(8, Date.valueOf(cancelDate));
        pS.setString(9,npVehicle);
        pS.setString(10,origin);
        pS.setString(11,destination);
        pS.setString(12, String.valueOf(LatitudeOrigin));
        pS.setString(13, String.valueOf(LongitudeOrigin));
        pS.setString(14, String.valueOf(LatitudeOrigin));
        pS.setString(15, String.valueOf(LongitudeOrigin));
    }

    public void setFullPreparedStatementUpdate(PreparedStatement pS) throws SQLException {
        pS.setDate(1, Date.valueOf(startDate));
        pS.setTime(2,startTime);
        pS.setInt(3,oferredSeats);
        pS.setInt(4,ocupiedSeats);
        pS.setString(5,restrictions);
        pS.setString(6,details);
        pS.setDate(7, Date.valueOf(cancelDate));
        pS.setString(8,npVehicle);
        pS.setString(9,origin);
        pS.setString(10,destination);
        pS.setString(11, String.valueOf(LatitudeOrigin));
        pS.setString(12, String.valueOf(LongitudeOrigin));
        pS.setString(13, String.valueOf(LatitudeDestination));
        pS.setString(14, String.valueOf(LongitudeDestination));
        pS.setString(15,id);
    }


    /**
     * @brief Funció inserta a la BD un Trip
     * @post A la BD queda agefit el Trip
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO TRIP VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?); ");
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
        PreparedStatement pS = c.prepareStatement("UPDATE TRIP SET startDate = ?, startTime = ?, oferredSeats = ?," +
                "ocupiedSeats = ?, restrictions = ?, details = ?, CancelDate = ?, nPVehicle = ?, origin = ?, destination = ?," +
                " LatitudeOrigin = ?, LongitudeOrigin = ?, LatitudeDestination = ?, LongitudeDestination = ? WHERE id = ?");
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
     * @brief Funció converteix en un String json un GatewayDriver
     * @post El GWDriver esta en format String json
     * @return es retorna el String Json amb la info del GWDriver
     */
    public String json() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
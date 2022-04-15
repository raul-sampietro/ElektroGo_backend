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
    private LocalDate cancelDate;
    private String npVehicle;
    private BigDecimal latitude;
    private BigDecimal longitude;


    /**
     * @brief Creadora de la Clase GatewayTrip amb tots els seus respectius parametres
     * @param id per identificar el viatge
     * @param cancelDate data del ultim dia de cancelacio
     * @param latitude longitud inici
     * @param longitude longitud desti
     * @param npVehicle matricula del vehicle que fara el viatge
     * @param ocupiedSeats seients ocupats
     * @param oferredSeats seients oferts
     * @param restrictions restriccions del viatge posades pel conductor
     * @param startDate data del viatge
     * @param startTime temps sortida viatge
     * @post Es crea un nou GWTrip amb els valors indicats
     */
    public GatewayTrip(String id, LocalDate startDate, Time startTime, Integer oferredSeats, Integer ocupiedSeats, String restrictions, LocalDate cancelDate, String npVehicle, BigDecimal latitude, BigDecimal longitude) {
        this.id = id;
        this.startDate = startDate;
        this.startTime = startTime;
        this.oferredSeats = oferredSeats;
        this.ocupiedSeats = ocupiedSeats;
        this.restrictions = restrictions;
        this.cancelDate = cancelDate;
        this.npVehicle = npVehicle;
        this.latitude = latitude;
        this.longitude = longitude;
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
    public LocalDate getCancelDate() {return cancelDate;}
    public void setCancelDate(LocalDate cancelDate) {this.cancelDate = cancelDate;}
    public String getNpVehicle() {return npVehicle;}
    public void setNpVehicle(String npVehicle) {this.npVehicle = npVehicle;}
    public BigDecimal getLatitude() {return latitude;}
    public void setLatitude(BigDecimal latitude) {latitude = latitude;}
    public BigDecimal getLongitude() {return longitude;}
    public void setLongitude(BigDecimal longitude) {longitude = longitude;}


    //SQL operations

    /**
     * @brief Coloca tota la info requerida  al PreparedStatement
     * @param pS PreparedStatement al que volem col·locar la info
     * @pre s'ens passa un pS
     * @post el pS queda assignat amb la info requerida
     */
    public void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1,id);
        pS.setDate(2, Date.valueOf(startDate));
        pS.setTime(3,startTime);
        pS.setInt(4,oferredSeats);
        pS.setInt(5,ocupiedSeats);
        pS.setString(6,restrictions);
        pS.setDate(7, Date.valueOf(cancelDate));
        pS.setString(8,npVehicle);
        pS.setString(9, String.valueOf(latitude));
        pS.setString(10, String.valueOf(longitude));
    }

    public void setFullPreparedStatementUpdate(PreparedStatement pS) throws SQLException {
        pS.setDate(1, Date.valueOf(startDate));
        pS.setTime(2,startTime);
        pS.setInt(3,oferredSeats);
        pS.setInt(4,ocupiedSeats);
        pS.setString(5,restrictions);
        pS.setDate(6, Date.valueOf(cancelDate));
        pS.setString(7,npVehicle);
        pS.setString(8, String.valueOf(latitude));
        pS.setString(9, String.valueOf(longitude));
        pS.setString(10,id);
    }


    /**
     * @brief Funció inserta a la BD un Trip
     * @post A la BD queda agefit el Trip
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO TRIP VALUES (?,?,?,?,?,?,?,?,?,?); ");
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
                "ocupiedSeats = ?, restrictions = ?, CancelDate = ?, nPVehicle = ?, Latitude = ?, Longitude = ? WHERE id = ?");
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
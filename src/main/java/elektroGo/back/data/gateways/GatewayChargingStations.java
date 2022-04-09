/**
 * @file GatewayChargingStations.java
 * @author Marc Castells
 * @date 13/03/2022
 * @brief Implementacio del Gateway de les estacions de carrega
 */
package elektroGo.back.data.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderChargingStations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @brief La classe GatewayChargingStations implementa el Gateway de ChargingStations el qual te els atributs de ChargingStations i fa insert/update/delete a la BD
 */
public class GatewayChargingStations implements Gateway{

    /**
     * @brief Identificador de l'estacio de carrega
     */
    private Integer id;

    /**
     * @brief Latitud del punt on esta situada l'estacio de carrega
     */
    private BigDecimal latitude;

    /**
     * @brief Longitud del punt on esta situada l'estacio de carrega
     */
    private BigDecimal longitude;

    /**
     * @brief Numero de carregadors que hi ha a l'estacio de carrega
     */
    private Integer numberOfChargers;

    /**
     * @brief Singleton amb el FinderChargingStations
     */
    private FinderChargingStations fcs;

    //CONSTRUCTORS

    /**
     * @brief Creadora buida de la classe GatewayChargingStations
     * @return Retorna la instancia del gateway que s'acaba de crear
     */
    public GatewayChargingStations() {

    }

    /**
     * @brief Creadora de la classe GatewayChargingStations
     * @param id Identificador de l'estacio de carrega
     * @param latitude Latitud del punt on esta situada l'estacio de carrega
     * @param longitude Longitud del punt on esta situada l'estacio de carrega
     * @param numberOfChargers Numero de carregadors que hi ha a l'estacio de carrega
     * @return Retorna la instancia del gateway que s'acaba de crear
     */
    public GatewayChargingStations(Integer id, BigDecimal latitude, BigDecimal longitude, Integer numberOfChargers) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numberOfChargers = numberOfChargers;
    }

    //GETTERS AND SETTERS


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Integer getNumberOfChargers() {
        return numberOfChargers;
    }

    public void setNumberOfChargers(Integer numberOfChargers) {
        this.numberOfChargers = numberOfChargers;
    }



    //SQL SENTENCES

    /**
     * @brief Col·loca tota la info requerida (id, latitude, longitude, numberOfChargers) al PreparedStatement
     * @param pS PreparedStatement al que volem col·locar la info
     * @pre S'ens passa un pS
     * @post el pS queda assignat amb la info requerida
     */
    private void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setInt(1,id);
        pS.setBigDecimal(2, latitude);
        pS.setBigDecimal(3,longitude);
        if (numberOfChargers != null) pS.setInt(4,numberOfChargers); else pS.setString(4, null);
    }

    /**
     * @brief Col·loca tota la info requerida (latitude, longitude, numberOfChargers) al PreparedStatement
     * @param pS PreparedStatement al que volem col·locar la info
     * @pre S'ens passa un pS
     * @post el pS queda assignat amb la info requerida
     */
    private void setPreparedStatementNoID(PreparedStatement pS) throws SQLException {
        pS.setBigDecimal(1, latitude);
        pS.setBigDecimal(2,longitude);
        if (numberOfChargers != null) pS.setInt(3,numberOfChargers); else pS.setString(3, null);
    }

    /**
     * @brief Funció d'insertar l'estacio de carrega a la base de dades
     * @post Queda l'estacio de carrega afegida a la base de dades
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO CHARGINGSTATIONS VALUES (?,?,?,?); ");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Funció que actualitza l'estacio de carrega a la base de dades
     * @post Queda l'estacio de carrega actualitzada a la base de dades
     */
    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE CHARGINGSTATIONS SET latitude = ?, longitude = ?," +
                " numberOfChargers = ? WHERE id = ?;");
        setPreparedStatementNoID(pS);
        pS.setLong(4, id);
        pS.executeUpdate();
    }

    /**
     * @brief Funció que elimina l'estacio de carrega de la base de dades
     * @post L'estacio de carrega es eliminada de la base de dades
     */
    public void remove() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("DELETE FROM CHARGINGSTATIONS WHERE id=" + id + ";");
    }

    /**
     * @brief Funcio converteix en un String json un GatewayChargingStation
     * @post El GatewayChargingStation esta en format String json
     * @return Es retorna el String Json amb la info del GatewayChargingStation
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

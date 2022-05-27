/**
 * @file GatewayCanceledTrip.java
 * @author Gerard Castell
 * @date 22/05/2023
 * @brief Implementació de la classe Gateway
 */

package elektroGo.back.data.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderCanceledTrip;
import elektroGo.back.data.finders.FinderDriver;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * @brief La classe Gateway implementa el Gateway el qual te els atributs de CanceledTrip i fa insert/update/delete a la BD
 */
public class GatewayCanceledTrip implements Gateway{

    /**
     * @brief id trip
     */
    private Integer id;
    private LocalDate dayCanceled;
    private String reason;

    /**
     * @brief SingleTon amb el Finder
     */
    private FinderCanceledTrip fD;

    public GatewayCanceledTrip() {}
    /**
     * @brief Creadora de la Clase Gateway
     * @param id Usuari del qual volem crear el GW
     * @param dayCanceled data cancelacio
     * @param reason motiu cancelacio
     * @post Es crea un nou GW amb els valors indicats
     */
    public GatewayCanceledTrip(Integer id, LocalDate dayCanceled, String reason) {
        this.id = id;
        this.dayCanceled = dayCanceled;
        this.reason = reason;
    }


    //Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDayCanceled() {
        return dayCanceled;
    }

    public void setDayCanceled(LocalDate dayCanceled) {
        this.dayCanceled = dayCanceled;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    //SQL operations
    /**
     * @brief Coloca tota la info requerida(userName) al PreparedStatement
     * @param pS PreparedStatement al que volem colocar la info
     * @pre s'ens passa un pS
     * @post el pS queda assignat amb la info requerida
     */
    public void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setInt(1, id);
        pS.setDate(2, Date.valueOf(dayCanceled));
        pS.setString(3,reason);
    }

    public void setUpdatePreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setInt(3, id);
        pS.setDate(1, Date.valueOf(dayCanceled));
        pS.setString(2,reason);
    }
    /**
     * @brief Funció inserta a la BD un Driver
     * @post A la BD queda agefit el Driver
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO CANCELEDTRIP VALUES (?,?,?); ");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    //UNUSED AT THE MOMENT
    /**
     * @brief Funció fa un update a un Driver de la BD
     * @post Es fa un Update del Driver
     */
    public void update() throws SQLException {
       Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE CANCELEDTRIP SET dayCanceled = ?,reason=? WHERE id=?;");
        setUpdatePreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Funció elimina un Driver de la DB
     * @post A la BD queda eliminat el Driver
     */
    public void remove() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("DELETE FROM CANCELEDTRIP WHERE id=" + id + ";");
    }

    /**
     * @brief Funció converteix en un String json un GatewayDriver
     * @post El GWDriver esta en format String json
     * @return es retorna el String Json amb la info del GWDriver
     */
    public String json() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        mapper.registerModule(new JavaTimeModule());
        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}

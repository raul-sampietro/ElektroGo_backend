/**
 * @file GatewayDriver.java
 * @author Gerard Castell
 * @date 10/03/2023
 * @brief Implementació de la classe GatewayDriver
 */

package elektroGo.back.data.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderDriver;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @brief La classe GatewayDriver implementa el Gateway de Driver el qual te els atributs de Driver i fa insert/update/delete a la BD
 */
public class GatewayDriver implements Gateway{

    /**
     * @brief Username del Driver
     */
    private String username; //CHANGE TYPE OF THIS ATTRIBUTE TO DRIVER WHEN IMPLEMENTED

    private Boolean verified;
    /**
     * @brief SingleTon amb el FinderDriver
     */
    private FinderDriver fD;

    public GatewayDriver() {}
    /**
     * @brief Creadora de la Clase Gateway Driver amb el username
     * @param username Usuari del qual volem crear el GW
     * @post Es crea un nou GWDriver amb els valors indicats
     * @return Retorna la instancia del gateway que s'acaba de crear
     */
    public GatewayDriver(String username, Boolean verified) {
        this.username = username;
        this.verified = verified;
    }

    //Getters and Setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Boolean getVerified() {
        return verified;
    }
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    //SQL operations
    /**
     * @brief Coloca tota la info requerida(userName) al PreparedStatement
     * @param pS PreparedStatement al que volem colocar la info
     * @pre s'ens passa un pS
     * @post el pS queda assignat amb la info requerida
     */
    public void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1, username);
        pS.setBoolean(2,false);
    }

    public void setUpdatePreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(2, username);
        pS.setBoolean(1,verified);
    }
    /**
     * @brief Funció inserta a la BD un Driver
     * @post A la BD queda agefit el Driver
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO DRIVER VALUES (?, ?); ");
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
        PreparedStatement pS = c.prepareStatement("UPDATE DRIVER SET verified = ? WHERE userName = ?");
        setUpdatePreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Funció elimina un Driver de la DB
     * @post A la BD queda eliminat el Driver
     */
    public void remove() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("DELETE FROM DRIVER WHERE userName='" + username + "';");
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

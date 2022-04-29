/**
 * @file GatewayChats.java
 * @author Adria Abad
 * @date 13/04/2022
 * @brief Implementacio del Gateway de Chats.
 */

package elektroGo.back.data.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * @brief La classe GatewayChats implementa el Gateway de Chats el qual te els atributs de Chats i fa insert/update/delete a la BD
 */
public class GatewayChats implements Gateway{

    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    /**
     * @brief Nom de l'usuari que envia el missatge
     */
    private String sender;

    /**
     * @brief Nom de l'usuari que rep el missatge
     */
    private String receiver;

    /**
     * @brief Contingut del missatge
     */
    private String message;

    /**
     * @brief Timestamp en el que s'ha enviat el missatge
     */
    private String sentAt;

    /**
     * @brief Constructora buida
     * @return Crea un GatewayChats buit
     */
    public GatewayChats(){}

    /**
     * @brief Constructora de GatewayChats amb els parametres indicats
     * @param sender usuari que envia el missatge
     * @param receiver usuari que rep el missatge
     * @param message contingut del missatge
     * @param sentAt moment en el qual s'ha enviat el missatge
     * @return Retorna la instancia del gateway que s'acaba de crear
     */
    public GatewayChats(String sender, String receiver, String message, String sentAt) {
        setUp(sender, receiver, message, sentAt);
    }

    /**
     * @brief Inicialitza els parametres indicats
     * @param sender usuari que envia el missatge
     * @param receiver usuari que rep el missatge
     * @param message contingut del missatge
     * @param sentAt moment en el qual s'ha enviat el missatge
     * @post Inicialitza l'objecte amb els parametres indicats previament
     */
    private void setUp(String sender, String receiver, String message, String sentAt) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.sentAt = sentAt;
    }

    //Getters and setters

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    //SQL operations

    /**
     * @brief Operacio per setejar un prepared statement amb els atributs de l'objecte
     * @pre pS conte una sentencia SQL amb prepared statement a la qual falten per establir tots els parametres els quals son els atributs de la classe en l'ordre en que es declaren
     * @param pS Objecte prepared statement amb una sentencia SQL
     * @post Estableix els parametres de la sentencia SQL de pS amb els atributs de l'objecte
     */
    public void setPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1, sender);
        pS.setString(2, receiver);
        pS.setString(3, message);
        pS.setString(4, sentAt);
    }

    /**
     * @brief Insert de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un insert en la BD amb els atributs de l'objecte
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO CHATS VALUES (?,?,?,?); ");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Update de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un update en la BD amb els atributs de l'objecte
     */
    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE CHATS SET message = ? WHERE sender = ? AND receiver = ? and sentAt = ?;");
        pS.setString(1, sender);
        pS.setString(2, receiver);
        pS.setString(3, sentAt);
        pS.executeUpdate();
    }

    /**
     * @brief Borra l'objecte de la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post L'objecte es esborrat de la BD
     */
    public void remove() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("DELETE FROM CHATS WHERE sender = ? AND receiver = ? and sentAt = ?;");
        pS.setString(1, sender);
        pS.setString(2, receiver);
        pS.setString(3, sentAt);
        pS.executeUpdate();
    }

    /**
     * @brief Passa l'objecte a JSON
     * @return Retorna l'objecte en format JSON amb un String
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
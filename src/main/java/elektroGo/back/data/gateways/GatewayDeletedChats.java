/**
 * @file GatewayDeletedChats.java
 * @author Adria Abad
 * @date 18/05/2022
 * @brief Implementacio del Gateway de DeletedChats.
 */

package elektroGo.back.data.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @brief La classe GatewayChats implementa el Gateway de DeletedChats el qual te els atributs de DeletedChats i fa insert/update/delete a la BD
 */
public class GatewayDeletedChats implements Gateway{

    /**
     * @brief Nom de l'usuari que elimina el xat
     */
    private String userA;

    /**
     * @brief Nom de l'usuari sobre el que s'elimina el xat
     */
    private String userB;

    /**
     * @brief Constructora buida
     * @return Crea un GatewayChats buit
     */
    public GatewayDeletedChats(){}

    /**
     * @brief Constructora de GatewayChats amb els parametres indicats
     * @param userA usuari que elimina el xat
     * @param userB usuari sobre el que s'elimina el xat
     * @return Retorna la instancia del gateway que s'acaba de crear
     */
    public GatewayDeletedChats(String userA, String userB) {
        setUp(userA, userB);
    }

    public String getUserA() {
        return userA;
    }

    public void setUserA(String userA) {
        this.userA = userA;
    }

    public String getUserB() {
        return userB;
    }

    public void setUserB(String userB) {
        this.userB = userB;
    }

    /**
     * @brief Inicialitza els parametres indicats
     * @param userA usuari que elimina el xat
     * @param userB usuari sobre el que s'elimina el xat
     * @post Inicialitza l'objecte amb els parametres indicats previament
     */
    private void setUp(String userA, String userB) {
        this.userA = userA;
        this.userB = userB;
    }

    /**
     * @brief Operacio per setejar un prepared statement amb els atributs de l'objecte
     * @pre pS conte una sentencia SQL amb prepared statement a la qual falten per establir tots els parametres els quals son els atributs de la classe en l'ordre en que es declaren
     * @param pS Objecte prepared statement amb una sentencia SQL
     * @post Estableix els parametres de la sentencia SQL de pS amb els atributs de l'objecte
     */
    public void setPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1, userA);
        pS.setString(2, userB);
    }

    /**
     * @brief Insert de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un insert en la BD amb els atributs de l'objecte
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO DELETEDCHATS VALUES (?,?); ");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    @Override
    public void update() throws SQLException {
        // NO SE UTILIZA, NO TIENE SENTIDO (sonarcloud)
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE DELETEDCHATS SET userB = ? WHERE userA = ? ;");
        setPreparedStatement(pS);
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
        PreparedStatement pS = c.prepareStatement("DELETE FROM DELETEDCHATS WHERE userA = ? AND userB = ?; ");
        setPreparedStatement(pS);
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

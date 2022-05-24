/**
 * @file GatewayBlock.java
 * @author Gerard Castell
 * @date 19/05/2022
 * @brief Implementacio del Gateway de Block.
 */

package elektroGo.back.data.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * @brief La classe GatewayBlock implementa el Gateway el qual te els atributs de Block i fa insert/update/delete a la BD
 */
public class GatewayBlock implements Gateway {

    /**
     * @brief Usuari que fa la block
     */
    String userBlocking;
    /**
     * @brief Usuari blocked
     */
    String blockUser;

    public String getUserBlocking() {
        return userBlocking;
    }

    public void setUserBlocking(String userBlocking) {
        this.userBlocking = userBlocking;
    }

    public String getBlockUser() {
        return blockUser;
    }

    public void setBlockUser(String blockUser) {
        this.blockUser = blockUser;
    }

    /**
     * @brief Constructora buida
     * @post Crea un GatewayBlock buit
     */
    public GatewayBlock() {
    }

    /**
     * @brief Constructora amb tots els parametres
     * @param userBlocking user que bloca
     * @param blockUser user blocat
     * @post Crea un GatewayReport amb els atributs previament indicats
     */
    public GatewayBlock(String userBlocking, String blockUser) {
        this.userBlocking = userBlocking;
        this.blockUser = blockUser;
    }

    /**
     * @brief Estableix un PreparedStatement amb tots els atributs de GWBlock
     * @param pS PreparedStatement en el que establirem els atributs
     * @post El preparedStatement té establerts els atributs de GWBlock
     */
    public void setPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1,userBlocking);
        pS.setString(2,blockUser);
    }

    /**
     * @brief Insert de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un insert en la BD amb els atributs de l'objecte
     */
    @Override
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO BLOCK VALUES (?,?); ");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    @Override
    public void update() throws SQLException {
    }


    /**
     * @brief Borra l'objecte de la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post L'objecte es esborrat de la BD
     */
    @Override
    public void remove() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("DELETE FROM BLOCK WHERE userBlocking = ? and blockUser = ?;");
        pS.setString(1, userBlocking);
        pS.setString(2,blockUser);
        pS.executeUpdate();
    }

    /**
     * @return Retorna l'objecte en format JSON amb un String
     * @brief Passa l'objecte a JSON
     * @pre cert
     */
    @Override
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

/**
 * @file GatewayUser.java
 * @author Gerard Castell
 * @date 10/03/2023
 * @brief Implementació de la classe GatewayUser
 */
package elektroGo.back.data.Gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;
import elektroGo.back.data.Finders.FinderUser;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @brief La classe Gateway User es la classe que representa els Users i a més fa comunicacions amb la BD
 */
public class GatewayUser implements Gateway{

    /**
     * @brief Username del User
     */
    private String userName; //CHANGE TYPE OF THIS ATTRIBUTE TO DRIVER WHEN IMPLEMENTED
    /**
     * @brief Correu electronic del Driver
     */
    private String mail;
    /**
     * @brief Contrasenya del Driver
     */
    private String password;

    /**
     * @brief SingleTon amb el FinderUser
     */
    private FinderUser fU;

    /**
     * @brief Creadora de la Clase Gateway User amb el userName
     * @param userName Usuari del qual volem crear el GW
     * @pre -
     * @post Es crea un nou GWUser amb els valors indicats
     */
    public GatewayUser(String userName, String mail, String password) {
        setUp(userName, mail, password);
    }

    /**
     * @brief Funció encarregada d'assignar els valors username, mail i password al User crear
     * @param userName que assignarem al propi User
     * @pre -
     * @post El userName del User s'actualitza
     */
    private void setUp(String userName, String mail, String password) {
        this.mail = mail;
        this.password = password;
        this.userName = userName;
    }

    //Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    //SQL operations

    /**
     * @brief Coloca tota la info requerida (userName, mail, password) al PreparedStatement
     * @param pS PreparedStatement al que volem col·locar la info
     * @pre s'ens passa un pS
     * @post el pS queda assignat amb la info requerida
     */
    public void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1,userName);
        pS.setString(2, mail);
        pS.setString(3,password);
    }

    /**
     * @brief Funció inserta a la BD un User
     * @pre -
     * @post A la BD queda agefit el User
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO USERS VALUES (?,?,?); ");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Funció fa un update a un User de la BD
     * @pre -
     * @post Es fa un Update del User
     */
    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE USERS SET userName = ?, mail = ?, password = ?;");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Funció elimina un User de la DB
     * @pre -
     * @post A la BD queda eliminat el User
     */
    public void remove() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("DELETE FROM USERS WHERE userName='" + userName + "';");
    }

    /**
     * @brief Funció converteix en un String json un GatewayDriver
     * @pre -
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
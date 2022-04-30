/**
 * @file GatewayUser.java
 * @author Gerard Castell
 * @date 10/03/2023
 * @brief Implementació de la classe GatewayUser
 */
package elektroGo.back.data.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderUser;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @brief La classe GatewayUser implementa el Gateway de User el qual te els atributs de User i fa insert/update/delete a la BD
 */
public class GatewayUser implements Gateway{

    /**
     * @brief Username del User
     */
    private String username; //CHANGE TYPE OF THIS ATTRIBUTE TO DRIVER WHEN IMPLEMENTED
    /**
     * @brief Correu electronic del Driver
     */
    private String email;
    private String id;
    private String provider;
    private String name;
    private String givenName;
    private String familyName;
    private String imageUrl;



    /**
     * @brief SingleTon amb el FinderUser
     */
    private FinderUser fU;
    public GatewayUser(){}
    /**
     * @brief Creadora de la Clase Gateway User amb el username
     * @param username Usuari del qual volem crear el GW
     * @post Es crea un nou GWUser amb els valors indicats
     */
    public GatewayUser(String id,String provider,String username, String email, String name, String givenName, String familyName, String imageUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.imageUrl = imageUrl;
    }

    //Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {return provider;}

    public void setProvider(String provider) {this.provider = provider;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getGivenName() {return givenName;}

    public void setGivenName(String givenName) {this.givenName = givenName;}

    public String getFamilyName() {return familyName;}

    public void setFamilyName(String familyName) {this.familyName = familyName;}

    public String getImageUrl() {return imageUrl;}

    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}
//SQL operations

    /**
     * @brief Coloca tota la info requerida (userName, email, password) al PreparedStatement
     * @param pS PreparedStatement al que volem col·locar la info
     * @pre s'ens passa un pS
     * @post el pS queda assignat amb la info requerida
     */
    public void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(3,username);
        pS.setString(4, email);
        pS.setString(1, id);
        pS.setString(2, provider);
        pS.setString(5, name);
        pS.setString(6, givenName);
        pS.setString(7, familyName);
        pS.setString(8, imageUrl);
    }

    public void setUpdatePreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(8,username);
        pS.setString(3, email);
        pS.setString(1, id);
        pS.setString(2, provider);
        pS.setString(4, name);
        pS.setString(5, givenName);
        pS.setString(6, familyName);
        pS.setString(7, imageUrl);
    }

    /**
     * @brief Funció inserta a la BD un User
     * @post A la BD queda agefit el User
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO USERS VALUES (?,?,?,?,?,?,?,?); ");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Funció fa un update a un User de la BD
     * @post Es fa un Update del User
     */
    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE USERS SET id = ?, provider = ?, email = ?, name = ?, givenName = ?,familyName=?,imageUrl=? WHERE username = ?;");
        setUpdatePreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Funció elimina un User de la DB
     * @post A la BD queda eliminat el User
     */
    public void remove() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("DELETE FROM USERS WHERE username='" + username + "';");
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
package elektroGo.back.data.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GatewayRating implements Gateway{

    String userWhoRates;
    String ratedUser;
    int points;
    String comment;

    public GatewayRating(String userWhoRates, String ratedUser, int points, String comment) {
        this.userWhoRates = userWhoRates;
        this.ratedUser = ratedUser;
        this.points = points;
        this.comment = comment;
    }

    public String getUserWhoRates() {
        return userWhoRates;
    }

    public void setUserWhoRates(String userWhoRates) {
        this.userWhoRates = userWhoRates;
    }

    public String getRatedUser() {
        return ratedUser;
    }

    public void setRatedUser(String ratedUser) {
        this.ratedUser = ratedUser;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    //SQL operations

    /**
     * @brief Operacio per setejar un prepared statement amb els atributs de l'objecte
     * @pre pS conte una sentencia SQL amb prepared statement a la qual falten per establir tots els parametres els quals son els atributs de la classe en l'ordre en que es declaren
     * @param pS Objecte prepared statement amb una sentencia SQL
     * @post Estableix els parametres de la sentencia SQL de pS amb els atributs de l'objecte
     */
    public void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1,userWhoRates);
        pS.setString(2, ratedUser);
        pS.setInt(3,points);
        pS.setString(4,comment);
    }

    /**
     * @brief Operacio per setejar un prepared statement amb els atributs de l'objecte
     * @pre pS conte una sentencia SQL amb prepared statement a la qual falten per establir tots els parametres els quals son els atributs de la classe en l'ordre en que es declaren sense els clau
     * @param pS Objecte prepared statement amb una sentencia SQL
     * @post Estableix els parametres de la sentencia SQL de pS amb els atributs de l'objecte
     */
    public void setPreparedStatementNoPK(PreparedStatement pS) throws SQLException {
        pS.setInt(1,points);
        pS.setString(2,comment);
    }

    /**
     * @brief Insert de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un insert en la BD amb els atributs de l'objecte
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO RATING VALUES (?,?,?, ?); ");
        setFullPreparedStatement(pS);
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
        PreparedStatement pS = c.prepareStatement("UPDATE RATING SET points = ?, comment = ?, " +
               "WHERE userWhoRates = ? and ratedUser = ?;");
        setPreparedStatementNoPK(pS);
        pS.setString(3, userWhoRates);
        pS.setString(4, ratedUser);
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
        PreparedStatement pS = c.prepareStatement("DELETE FROM RATING WHERE userWhoRates = ? and ratedUser = ? ;");
        pS.setString(1, userWhoRates);
        pS.setString(2, ratedUser);
        pS.executeUpdate();
    }

    /**
     * @return Retorna l'objecte en format JSON amb un String
     * @brief Passa l'objecte a JSON
     * @pre cert
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

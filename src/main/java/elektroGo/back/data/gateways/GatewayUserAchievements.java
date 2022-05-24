package elektroGo.back.data.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import elektroGo.back.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GatewayUserAchievements implements Gateway{
    String username;
    String achievement;
    int points;

    public GatewayUserAchievements(String username, String achievement) {
        this.username = username;
        this.achievement = achievement;
        points = 0;
    }

    public GatewayUserAchievements(String username, String achievement, int points) {
        this.username = username;
        this.achievement = achievement;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * @brief Insert de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un insert en la BD amb els atributs de l'objecte
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO USERACHIEVEMENTS VALUES (?,?,?); ");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    private void setPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1, username);
        pS.setString(2, achievement);
        pS.setInt(3, points);
    }

    /**
     * @brief Update de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un update en la BD amb els atributs de l'objecte
     */
    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE USERACHIEVEMENTS SET points = ? " +
                "WHERE username = ? and achievement = ?;");
        pS.setInt(1, points);
        pS.setString(2,username);
        pS.setString(3, achievement);
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
        PreparedStatement pS = c.prepareStatement("DELETE FROM USERACHIEVEMENTS WHERE username = ? and achievement = ?;");
        pS.setString(1, username);
        pS.setString(2, achievement);
        pS.executeUpdate();
    }

    /**
     * @return Retorna l'objecte en format JSON amb un String
     * @brief Passa l'objecte a JSON
     * @pre cert
     */
    public String json() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String json = "";
        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}

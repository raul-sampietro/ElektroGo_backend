package elektroGo.back.data.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import elektroGo.back.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GatewayAchieviements implements Gateway{

    /**
     * @brief Nom de l'assoliment
     */
    String name;

    /**
     * @brief Descripció de l'assoliment
     */
    String description;

    public GatewayAchieviements() {

    }

    public GatewayAchieviements(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1, name);
        pS.setString(2, description);
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
        PreparedStatement pS = c.prepareStatement("INSERT INTO ACHIEVEMENTS VALUES (?,?); ");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Update de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un update en la BD amb els atributs de l'objecte
     */
    @Override
    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE ACHIEVEMENTS SET description = ?" +
                "WHERE name = ?;");
        pS.setString(1, description);
        pS.setString(2,name);
        pS.executeUpdate();
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
        PreparedStatement pS = c.prepareStatement("DELETE FROM ACHIEVEMENTS WHERE name = ?;");
        pS.setString(1, name);
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

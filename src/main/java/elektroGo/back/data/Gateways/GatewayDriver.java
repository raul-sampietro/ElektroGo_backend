package elektroGo.back.data.Gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;
import elektroGo.back.data.Finders.FinderDriver;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GatewayDriver implements Gateway{

    private String userName; //CHANGE TYPE OF THIS ATTRIBUTE TO DRIVER WHEN IMPLEMENTED

    private FinderDriver fD;

    public GatewayDriver(String userName) {
        setUp(userName);
    }

    private void setUp(String userName) {
        this.userName = userName;
    }

    //Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    //SQL operations

    public void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1,userName);
    }

    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO DRIVER VALUES (?); ");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE DRIVER SET userName = ?;");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    public void remove() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("DELETE FROM DRIVER WHERE userName='" + userName + "';");
    }

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

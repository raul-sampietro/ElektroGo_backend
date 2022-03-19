package elektroGo.back.data.Gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;
import elektroGo.back.data.Finders.FinderUser;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GatewayUser implements Gateway{

    private String userName; //CHANGE TYPE OF THIS ATTRIBUTE TO DRIVER WHEN IMPLEMENTED
    private String mail;
    private String password;

    private FinderUser fU;

    public GatewayUser(String userName, String mail, String password) {
        setUp(userName, mail, password);
    }

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

    public void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1,userName);
        pS.setString(2, mail);
        pS.setString(3,password);
    }

    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO USERS VALUES (?,?,?); ");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE USERS SET userName = ?, mail = ?, password = ?;");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    public void remove() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("DELETE FROM USERS WHERE userName='" + userName + "';");
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
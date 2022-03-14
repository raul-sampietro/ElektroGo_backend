package elektroGo.back.data.Gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;
import elektroGo.back.data.Finders.FinderChargingStations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GatewayChargingStations {
    private Integer id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer numberOfChargers;

    private FinderChargingStations fcs;

    //CONSTRUCTORS

    public GatewayChargingStations() {

    }

    public GatewayChargingStations(Integer id, BigDecimal latitude, BigDecimal longitude, Integer numberOfChargers) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numberOfChargers = numberOfChargers;
    }

    //GETTERS AND SETTERS


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Integer getNumberOfChargers() {
        return numberOfChargers;
    }

    public void setNumberOfChargers(Integer numberOfChargers) {
        this.numberOfChargers = numberOfChargers;
    }

    //SQL SENTENCES
    private void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setInt(1,id);
        pS.setBigDecimal(2, latitude);
        pS.setBigDecimal(3,longitude);
        if (numberOfChargers != null) pS.setInt(4,numberOfChargers); else pS.setString(4, null);
    }

    private void setPreparedStatementNoID(PreparedStatement pS) throws SQLException {
        pS.setBigDecimal(1, latitude);
        pS.setBigDecimal(2,longitude);
        if (numberOfChargers != null) pS.setInt(3,numberOfChargers); else pS.setString(3, null);
    }

    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO CHARGINGSTATIONS VALUES (?,?,?,?); ");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE CHARGINGSTATIONS SET longitude = ?, latitude = ?," +
                " numberOfChargers = ? WHERE id = ?;");
        setPreparedStatementNoID(pS);
        pS.setLong(4, id);
        pS.executeUpdate();
    }


    public void remove() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("DELETE FROM CHARGINGSTATIONS WHERE id=" + id + ";");
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

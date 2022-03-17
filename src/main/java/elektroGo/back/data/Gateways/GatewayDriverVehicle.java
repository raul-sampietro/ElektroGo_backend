package elektroGo.back.data.Gateways;

import elektroGo.back.data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GatewayDriverVehicle implements Gateway {
    private final String nPVehicle;
    private final String userDriver;

    public GatewayDriverVehicle(String nPVehicle, String userDriver) {
        this.nPVehicle = nPVehicle;
        this.userDriver = userDriver;
    }

    private void setPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1,nPVehicle);
        pS.setString(2,userDriver);
    }

    public String getnPVehicle() {
        return nPVehicle;
    }

    public String getUserDriver() {
        return userDriver;
    }


    @Override
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO DRIVERVEHICLE VALUES (?,?); ");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }


    @Override
    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE DRIVERVEHICLE SET nPVehicle = ?, userDriver = ?;");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    @Override
    public void remove() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("DELETE FROM DRIVERVEHICLE WHERE nPVehicle = ? and userDriver = ?;");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    @Override
    public String json() {
        return null;
    }
}

package elektroGo.back.data.Finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.Gateways.GatewayDriverVehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FinderDriverVehicle {
    private static FinderDriverVehicle singletonObject;

    private FinderDriverVehicle() {}

    public static FinderDriverVehicle getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderDriverVehicle();
        }
        return singletonObject;
    }

    private GatewayDriverVehicle createGateway(ResultSet r) throws SQLException {
        String nPVehicle = r.getString(1);
        String userDriver = r.getString(2);
        return new GatewayDriverVehicle(nPVehicle,userDriver);
    }

    public ArrayList<GatewayDriverVehicle> findAll() throws SQLException {
        GatewayDriverVehicle gDV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayDriverVehicle> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM DRIVERVEHICLE;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    public GatewayDriverVehicle findByNumberPlateDriver(String userDriver, String nPVehicle) throws SQLException {
        GatewayDriverVehicle gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM DRIVERVEHICLE WHERE userDriver = ? and nPVehicle = ?;");
        pS.setString(1,userDriver);
        pS.setString(2,nPVehicle);
        ResultSet r = pS.executeQuery();
        if (r.next()) gV = createGateway(r);
        return gV;
    }

    public ArrayList<GatewayDriverVehicle> findByNumberPlateV(String nPVehicle) throws SQLException {
        GatewayDriverVehicle gDV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayDriverVehicle> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM DRIVERVEHICLE WHERE nPVehicle = ?;");
        pS.setString(1,nPVehicle);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    public ArrayList<GatewayDriverVehicle> findByUserDriver(String userDriver) throws SQLException {
        GatewayDriverVehicle gDV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayDriverVehicle> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM DRIVERVEHICLE WHERE userDriver = ?;");
        pS.setString(1,userDriver);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }




}

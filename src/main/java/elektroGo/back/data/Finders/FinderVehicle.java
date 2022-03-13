package elektroGo.back.data.Finders;

import elektroGo.back.data.Gateways.Gateway;
import elektroGo.back.data.Gateways.GatewayVehicle;
import elektroGo.back.data.Database;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


public class FinderVehicle {

    private static FinderVehicle singletonObject;

    private FinderVehicle() {}

    public static FinderVehicle getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderVehicle();
        }
        return singletonObject;
    }

    public ArrayList<GatewayVehicle> findAll() throws SQLException {
        GatewayVehicle gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayVehicle> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM VEHICLE;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    public GatewayVehicle findByID(long idVehicle) throws SQLException {
        GatewayVehicle gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM VEHICLE WHERE id = ?;");
        pS.setLong(1,idVehicle);
        ResultSet r = pS.executeQuery();
        if (r.next()) gV = createGateway(r);

        return gV;
    }

    public ArrayList<GatewayVehicle> findByUserName(String userName) throws SQLException {
        GatewayVehicle gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayVehicle> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM VEHICLE WHERE userName = ?;");
        pS.setString(1,userName);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    private GatewayVehicle createGateway(ResultSet r) throws SQLException {
        long id = r.getLong(1);
        String brand = r.getString(2);
        String model = r.getString(3);
        String numberPlate = r.getString(4);
        int drivingRange = r.getInt(5);
        Date fabricationYearD = r.getDate(6);
        LocalDate fabricationYear = null;
        if (fabricationYearD != null) fabricationYear=  fabricationYearD.toLocalDate();
        int seats = r.getInt(7);
        String imageId = r.getString(8);
        String userName = r.getString(9);
        return new GatewayVehicle(id, brand, model, numberPlate, drivingRange, fabricationYear, seats,imageId, userName);
    }

    //Add necessary finders
}

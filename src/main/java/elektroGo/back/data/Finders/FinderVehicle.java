package elektroGo.back.data.Finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.Gateways.GatewayVehicle;


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

    public GatewayVehicle findByNumberPlate(String numberPlate) throws SQLException {
        GatewayVehicle gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM VEHICLE WHERE numberPlate = ?;");
        pS.setString(1,numberPlate);
        ResultSet r = pS.executeQuery();
        if (r.next()) gV = createGateway(r);
        return gV;
    }

    private GatewayVehicle createGateway(ResultSet r) throws SQLException {
        String brand = r.getString(1);
        String model = r.getString(2);
        String numberPlate = r.getString(3);
        int drivingRange = r.getInt(4);
        Date fabricationYearD = r.getDate(5);
        LocalDate fabricationYear = null;
        if (fabricationYearD != null) fabricationYear=  fabricationYearD.toLocalDate();
        int seats = r.getInt(6);
        String imageId = r.getString(7);
        return new GatewayVehicle(brand, model, numberPlate, drivingRange, fabricationYear, seats,imageId);
    }

    //Add necessary finders
}

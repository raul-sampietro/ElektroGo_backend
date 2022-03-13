package elektroGo.back.data.Finders;

import elektroGo.back.data.Gateways.GatewayVehicle;

import java.sql.*;
import java.time.LocalDate;


public class FinderVehicle {

    private static FinderVehicle singletonObject;

    private FinderVehicle() {}

    public static FinderVehicle getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderVehicle();
        }
        return singletonObject;
    }

    public GatewayVehicle findByID(long idVehicle) {
        GatewayVehicle gV = null;
        Database d = Database.getInstance();
        try {
            ResultSet r = d.executeSQLQuery("select * from vehicle where id = " + sID);
            if (r.next()) gV = createGateway(r);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gV;
    }

    private GatewayVehicle createGateway(ResultSet r) throws SQLException {
        long id = r.getInt(1);
        String brand = r.getString(2);
        String model = r.getString(3);
        String numberPlate = r.getString(4);
        int drivingRange = r.getInt(5);
        Date fabricationYearD = r.getDate(6);
        LocalDate fabricationYear =  fabricationYearD.toLocalDate();
        int seats = r.getInt(7);
        return new GatewayVehicle(id, brand, model, numberPlate, drivingRange, fabricationYear, seats);
    }

    //Add necessary finders
}

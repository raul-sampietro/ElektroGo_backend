package elektroGo.back.data.Finders;

import elektroGo.back.data.Gateways.GatewayVehicle;

import java.sql.*;


public class FinderVehicle {

    private static FinderVehicle singletonObject;
    private Connection conn;

    private FinderVehicle() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://10.4.41.58/elektrogo",
                    "test", "test");
            boolean valid = conn.isValid(50000);
            System.out.println(valid ? "TEST OK" : "TEST FAIL");
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

    public static FinderVehicle getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderVehicle();
        }
        return singletonObject;
    }

    public Connection getConnection() {
        return conn;
    }

    public GatewayVehicle findByID(long idVehicle) {
        GatewayVehicle gV = null;
        try {
            Statement s = conn.createStatement();
            String sID = String.valueOf(idVehicle);
            ResultSet r = s.executeQuery("select * from vehicle where id = " + sID);
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
        int range = r.getInt(5);
        Date yearOfProduction = r.getDate(6);
        int maxSeats = r.getInt(7);
        return new GatewayVehicle(id, brand, model, numberPlate, range, yearOfProduction, maxSeats);
    }

    //Add necessary finders
}

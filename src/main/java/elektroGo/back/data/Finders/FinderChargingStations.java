package elektroGo.back.data.Finders;

import java.sql.Connection;
import java.sql.DriverManager;

public class FinderChargingStations {
    private static FinderChargingStations singletonObject;
    private Connection conn;

    private FinderChargingStations() {
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

    public static FinderChargingStations getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderChargingStations();
        }
        return singletonObject;
    }

    public Connection getConnection() {
        return conn;
    }
}

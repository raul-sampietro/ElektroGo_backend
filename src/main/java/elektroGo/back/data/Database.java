package elektroGo.back.data;
import java.sql.*;

public class Database {

    private static Database singletonObject;
    private Connection conn;

    private Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://10.4.41.58/elektrogo",
                    "test", "test");
            boolean valid = conn.isValid(50000);
            System.out.println(valid ? "Connection established" : "Connection failed");
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

    public static Database getInstance() {
        if (singletonObject == null) {
            singletonObject = new Database();
        }
        return singletonObject;
    }

    public void executeSQLUpdate(String sql) throws SQLException {
        Statement s = conn.createStatement();
        s.executeUpdate(sql);

    }

    public ResultSet executeSQLQuery(String sql) throws SQLException {
        Statement s = conn.createStatement();
        return s.executeQuery(sql);
    }

    public Connection getConnection() {
        return conn;
    }


}
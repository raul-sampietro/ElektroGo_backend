package elektroGo.back.data.Finders;

import elektroGo.back.data.Gateways.GatewayDriver;
import elektroGo.back.data.Database;


import java.sql.*;
import java.util.ArrayList;


public class FinderDriver {

    //PRIVATE
    private static FinderDriver singletonObject;

    private FinderDriver() {
    }

    //PUBLIC
    public static FinderDriver getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderDriver();
        }
        return singletonObject;
    }

    public ArrayList<GatewayDriver> findAll() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayDriver> gdriver = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM DRIVER;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            gdriver.add(createGateway(r));
        }
        return gdriver;
    }

    public GatewayDriver findByUserName(String userName) throws SQLException {
        GatewayDriver gU = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM DRIVER WHERE userName = ?;");
        pS.setString(1, userName);
        ResultSet r = pS.executeQuery();
        if (r.next()) gU = createGateway(r);
        return gU;
    }

    private GatewayDriver createGateway(ResultSet r) throws SQLException {
        String userName = r.getString(1);
        return new GatewayDriver(userName);
    }

}
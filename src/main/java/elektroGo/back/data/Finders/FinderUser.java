package elektroGo.back.data.Finders;

import elektroGo.back.data.Gateways.GatewayUser;
import elektroGo.back.data.Gateways.GatewayVehicle;
import elektroGo.back.data.Database;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


public class FinderUser {

    //PRIVATE
    private static FinderUser singletonObject;

    private FinderUser() {
    }

    //PUBLIC
    public static FinderUser getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderUser();
        }
        return singletonObject;
    }

    public ArrayList<GatewayUser> findAll() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayUser> gusers = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERS;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            gusers.add(createGateway(r));
        }
        return gusers;
    }

    public GatewayUser findByUserName(String userName) throws SQLException {
        GatewayUser gU = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERS WHERE userName = ?;");
        pS.setString(1, userName);
        ResultSet r = pS.executeQuery();
        if (r.next()) gU = createGateway(r);
        return gU;
    }

    private GatewayUser createGateway(ResultSet r) throws SQLException {
        String userName = r.getString(1);
        String mail = r.getString(2);
        String password = r.getString(3);
        return new GatewayUser(userName, mail, password);
    }

}
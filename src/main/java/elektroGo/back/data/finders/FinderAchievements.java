package elektroGo.back.data.finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.gateways.GatewayAchieviements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FinderAchievements {

    private static FinderAchievements singletonObject;

    private FinderAchievements()  {}

    public FinderAchievements getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderAchievements();
        }
        return singletonObject;
    }

    public ArrayList<GatewayAchieviements> findAll() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayAchieviements> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM ACHIEVEMENTS;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    public GatewayAchieviements findByName(String name) throws SQLException {
        GatewayAchieviements gA = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM ACHIEVEMENTS " +
                                                          " WHERE name = ? ;");
        ResultSet r = pS.executeQuery();
        if (r.next()) {
            gA = createGateway(r);
        }
        return gA;
    }

    private GatewayAchieviements createGateway(ResultSet r) throws SQLException {
        String name = r.getString(1);
        String description = r.getString(2);
        return new GatewayAchieviements(name, description);
    }
}

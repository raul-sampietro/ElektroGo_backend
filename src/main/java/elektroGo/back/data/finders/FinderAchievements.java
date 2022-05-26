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
    private final Connection conn;

    private FinderAchievements()  {
        Database d = Database.getInstance();
        conn = d.getConnection();
    }

    public static FinderAchievements getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderAchievements();
        }
        return singletonObject;
    }

    //pre: pS has a SQL sentence
    private ArrayList<GatewayAchieviements> findTemplate(PreparedStatement pS) throws SQLException {
        ArrayList<GatewayAchieviements> aL = new ArrayList<>();
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    public ArrayList<GatewayAchieviements> findAll() throws SQLException {
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM ACHIEVEMENTS;");
        return findTemplate(pS);
    }

    public GatewayAchieviements findByName(String name) throws SQLException {
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM ACHIEVEMENTS " +
                                                          " WHERE name = ? ;");
        pS.setString(1, name);
        ArrayList<GatewayAchieviements> aL = findTemplate(pS);
        if (aL.isEmpty()) return null;
        return aL.get(0);
    }

    private GatewayAchieviements createGateway(ResultSet r) throws SQLException {
        String name = r.getString(1);
        String description = r.getString(2);
        return new GatewayAchieviements(name, description);
    }
}

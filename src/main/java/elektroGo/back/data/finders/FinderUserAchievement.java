package elektroGo.back.data.finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.gateways.GatewayUserAchievements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FinderUserAchievement {

    FinderUserAchievement singletonObject;
    Connection conn;

    private FinderUserAchievement() {
        Database d = Database.getInstance();
        conn = d.getConnection();
    }

    public FinderUserAchievement getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderUserAchievement();
        }
        return singletonObject;
    }

    public GatewayUserAchievements createGateway(ResultSet r) throws SQLException {
        String username = r.getString(1);
        String achievement = r.getString(2);
        int points = r.getInt(3);
        return new GatewayUserAchievements(username, achievement, points);
    }

    //pre: pS has a SQL sentence
    public ArrayList<GatewayUserAchievements> findWithPS(PreparedStatement pS) throws SQLException {
        ArrayList<GatewayUserAchievements> aL = new ArrayList<>();
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    public ArrayList<GatewayUserAchievements> findAll() throws SQLException {
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERACHIEVEMENTS;");
        return findWithPS(pS);
    }

    public ArrayList<GatewayUserAchievements> findByUsername(String username) throws SQLException {
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERACHIEVEMENTS WHERE username = ?;");
        pS.setString(1,username);
        return findWithPS(pS);
    }

    public ArrayList<GatewayUserAchievements> findByAchievement(String achievement) throws SQLException {
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERACHIEVEMENTS WHERE achievement = ?;");
        pS.setString(1,achievement);
        return findWithPS(pS);
    }

    public GatewayUserAchievements findByPK(String username, String achievement) throws SQLException {
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM USERACHIEVEMENTS WHERE username = ? and achievement = ?;");
        pS.setString(1, username);
        pS.setString(2, achievement);
        ArrayList<GatewayUserAchievements> aL = findWithPS(pS);
        if (aL.size() == 0) return null;
        else return aL.get(0);
    }


}

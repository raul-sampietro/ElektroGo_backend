package elektroGo.back.data.Finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.Gateways.GatewayChargingStations;
import elektroGo.back.data.Gateways.GatewayVehicle;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class FinderChargingStations {
    private static FinderChargingStations singletonObject;

    private FinderChargingStations() {}

    public static FinderChargingStations getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderChargingStations();
        }
        return singletonObject;
    }

    public ArrayList<GatewayChargingStations> findAll() throws SQLException {
        GatewayChargingStations gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayChargingStations> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM CHARGINGSTATIONS;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    public GatewayChargingStations findByID(long idChargingStation) throws SQLException {
        GatewayChargingStations gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM CHARGINGSTATIONS WHERE id = ?;");
        pS.setLong(1,idChargingStation);
        ResultSet r = pS.executeQuery();
        if (r.next()) gV = createGateway(r);

        return gV;
    }

    private GatewayChargingStations createGateway(ResultSet r) throws SQLException {
        Integer id = r.getInt(1);
        BigDecimal longitude = r.getBigDecimal(2);
        BigDecimal latitude = r.getBigDecimal(3);
        Integer numberOfChargers = r.getInt(4);
        return new GatewayChargingStations(id, longitude, latitude, numberOfChargers);
    }
}

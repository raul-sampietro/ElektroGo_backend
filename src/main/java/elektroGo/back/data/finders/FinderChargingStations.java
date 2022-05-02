/**
 * @file FinderChargingStations.java
 * @author Marc Castells
 * @date 13/03/2022
 * @brief Implementacio del Finder de les estacions de càrrega
 */

package elektroGo.back.data.finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.gateways.GatewayChargingStations;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;


/**
 * @brief La classe FinderChargingStations es l'encarregada de conectar-se amb la BD y retornar GW Driver
 */
public class FinderChargingStations {

    /**
     * @brief Singleton del Finder
     */
    private static FinderChargingStations singletonObject;

    /**
     * @brief Creadora de la clase FinderChargingStations
     */
    private FinderChargingStations() {}

    /**
     * @brief Funcio que retorna una instancia Singleton de FinderChargingStations
     * @return Es retorna un singletonObject per treballar amb aquesta classe
     */
    public static FinderChargingStations getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderChargingStations();
        }
        return singletonObject;
    }

    /**
     * @brief Retorna tots els punts de carrega que hi ha a la base de dades
     * @return Retorna un array amb tots els punts de carrega
     */
    public ArrayList<GatewayChargingStations> findAll() throws SQLException {
        GatewayChargingStations gCS = null;
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

    /**
     * @brief Retorna el punt de carrega seguint l'id donat
     * @param idChargingStation id del punt de carrega
     * @return Retorna el punt de carrega amb tota la seva informacio
     */
    public GatewayChargingStations findByID(int idChargingStation) throws SQLException {
        GatewayChargingStations gCS = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM CHARGINGSTATIONS WHERE id = ?;");
        pS.setLong(1, idChargingStation);
        ResultSet r = pS.executeQuery();
        if (r.next()) {
            gCS = createGateway(r);
        }

        return gCS;
    }

    /**
     * @brief Retorna tots els punts de carrega dins del quadrat format per dues coordenades
     * @param latitude1 latitud de la coordenada superior
     * @param longitude1 longitud de la coordenada superior
     * @param latitude2 latitud de la coordenada inferior
     * @param longitude2 longitud de la coordenada inferior
     * @return Retorna un array amb tots els punts de carrega que es troben dins les coordenades
     */
    public ArrayList<GatewayChargingStations> findByCoordinates(BigDecimal latitude1, BigDecimal longitude1, BigDecimal latitude2, BigDecimal longitude2) throws SQLException {
        GatewayChargingStations gCS = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayChargingStations> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * " +
                "FROM CHARGINGSTATIONS " +
                "WHERE latitude < ? and latitude > ? and longitude < ? and longitude > ?;");
        pS.setBigDecimal(1,latitude1);
        pS.setBigDecimal(2,latitude2);
        pS.setBigDecimal(3,longitude1);
        pS.setBigDecimal(4,longitude2);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Crear un gateway amb els parametres passats
     * @param r ResultSet que contindra tots el parametres per poder crear el Gateway (id, longitud, latitude, numberOfChargers)
     * @return Retorna un GatewayChargingStations amb els parametres donats
     */
    private GatewayChargingStations createGateway(ResultSet r) throws SQLException {
        Integer id = r.getInt(1);
        String promotor = r.getString(2);
        String acces = r.getString(3);
        String tipusVelocitat = r.getString(4);
        String tipusConnexio = r.getString(5);
        BigDecimal longitude = r.getBigDecimal(6);
        BigDecimal latitude = r.getBigDecimal(7);
        String descriptiva_designacio = r.getString(8);
        Double kw = r.getDouble(9);
        String AcDc = r.getString(10);
        String ident = r.getString(11);
        String numberOfChargers = r.getString(12);
        String tipusVehicle = r.getString(13);
        return new GatewayChargingStations(id, promotor, acces, tipusVelocitat, tipusConnexio, longitude, latitude, descriptiva_designacio,
                kw, AcDc, ident, numberOfChargers, tipusVehicle);
    }
}

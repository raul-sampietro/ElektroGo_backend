/**
 * @file FinderVehicle.java
 * @author Daniel Pulido
 * @date 11/03/2022
 * @brief Implementacio del Finder de DriverVehicle
 */

package elektroGo.back.data.Finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.Gateways.GatewayDriverVehicle;
import elektroGo.back.data.Gateways.GatewayVehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe FinderDriverVehicle es l'encarregada de conectar-se amb la BD y retornar GW DriverVehicle
 */
public class FinderDriverVehicle {

    /**
     * @brief Singleton del Finder
     */
    private static FinderDriverVehicle singletonObject;

    /**
     * @brief Creadora de la clase FinderDriverVehicle
     */
    private FinderDriverVehicle() {}

    /**
     * @brief Funcio que retorna una instancia Singleton de FinderDriverVehicle
     * @return Es retorna un singletonObject per treballar amb aquesta classe
     */
    public static FinderDriverVehicle getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderDriverVehicle();
        }
        return singletonObject;
    }

    /**
     * @brief Crear un gateway amb els parametres passats
     * @param r ResultSet que contindra tots el parametres per poder crear el Gateway (nPVheicle, userDriver)
     * @return Retorna un GatewayDriverVehicle amb els parametres donats
     */
    private GatewayDriverVehicle createGateway(ResultSet r) throws SQLException {
        String nPVehicle = r.getString(1);
        String userDriver = r.getString(2);
        return new GatewayDriverVehicle(nPVehicle,userDriver);
    }

    /**
     * @brief Retorna totes les instancies de DriverVehicle que hi ha a la base de dades
     * @return Retorna un array amb totes les instancies de DriverVehicle
     */
    public ArrayList<GatewayDriverVehicle> findAll() throws SQLException {
        GatewayDriverVehicle gDV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayDriverVehicle> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM DRIVERVEHICLE;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Retorna la instacia de DriverVehicle seguint els identificadors donats
     * @param userDriver identificador de l'usuari
     * @param nPVehicle identificador del vehicle
     * @return Retorna un GatewayUser amb tota la info del DriverVehicle
     */
    public GatewayDriverVehicle findByNumberPlateDriver(String userDriver, String nPVehicle) throws SQLException {
        GatewayDriverVehicle gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM DRIVERVEHICLE WHERE userDriver = ? and nPVehicle = ?;");
        pS.setString(1,userDriver);
        pS.setString(2,nPVehicle);
        ResultSet r = pS.executeQuery();
        if (r.next()) gV = createGateway(r);
        return gV;
    }

    /**
     * @brief Funcio que busca tots els vehicles a partir d'un usuari
     * @param userDriver identificador de l'usuari
     * @return Retorna un array de GatewayUser amb tota la info dels DriverVehicle
     */
    public ArrayList<GatewayVehicle> findVehiclesByUser(String userDriver) throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement(
                "SELECT v.brand, v.model, v.numberPlate , v.drivingRange , v.fabricationYear , v.seats , v.imageId " +
                "FROM VEHICLE v, DRIVERVEHICLE d " +
                "WHERE v.numberPlate = d.nPVehicle and d.userDriver = ?;");
        pS.setString(1,userDriver);
        ResultSet r = pS.executeQuery();
        ArrayList<GatewayVehicle> aL = new ArrayList<>();
        FinderVehicle fV = FinderVehicle.getInstance();
        while (r.next()) {
            aL.add(fV.createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Funcio que busca tots els users a partir d'un vehicle
     * @param nPVehicle identificador del vehicle
     * @return Retorna un array de GatewayUser amb tota la info dels DriverVehicle
     */
    public ArrayList<GatewayDriverVehicle> findByNumberPlateV(String nPVehicle) throws SQLException {
        GatewayDriverVehicle gDV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayDriverVehicle> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM DRIVERVEHICLE WHERE nPVehicle = ?;");
        pS.setString(1,nPVehicle);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Funcio que busca a partir d'un conductor
     * @param userDriver identificador del conductor
     * @return Retorna un array de GatewayUser amb tota la info dels DriverVehicle
     */
    public ArrayList<GatewayDriverVehicle> findByUserDriver(String userDriver) throws SQLException {
        GatewayDriverVehicle gDV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayDriverVehicle> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM DRIVERVEHICLE WHERE userDriver = ?;");
        pS.setString(1,userDriver);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }




}

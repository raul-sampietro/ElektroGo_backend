/**
 * @file FinderVehicle.java
 * @author Daniel Pulido
 * @date 11/03/2022
 * @brief Implementacio del Finder de Vehicle.
 */

package elektroGo.back.data.finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.gateways.GatewayVehicle;


import java.sql.*;
import java.util.ArrayList;

/**
 * @brief La classe FinderVehicle es l'encarregada de conectar-se amb la BD y retornar GW Vehicle.
 */
public class FinderVehicle {

    /**
     * @brief FinderVehicle, es un singleton
     */
    private static FinderVehicle singletonObject;
    private final Connection conn;

    /**
     * @brief Creadorà de la clase FinderVehicle
     */
    private FinderVehicle() {
        Database d = Database.getInstance();
        conn = d.getConnection();
    }


    /**
     * @brief Funció que retorna una instancia Singleton de FinderVehicle
     * @return Es retorna un singletonObject per treballar amb aquesta classe
     */
    public static FinderVehicle getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderVehicle();
        }
        return singletonObject;
    }

    //pre: pS has a SQL sentence
    public ArrayList<GatewayVehicle> findTemplate(PreparedStatement pS) throws SQLException {
        ArrayList<GatewayVehicle> aL = new ArrayList<>();
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Funció que agafa tots els Vehicles de la BD i els posa a un Array
     * @return Es retorna un array de GatewayVehicle amb tota la info dels Vehicles
     */
    public ArrayList<GatewayVehicle> findAll() throws SQLException {
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM VEHICLE;");
        return findTemplate(pS);
    }

    public ArrayList<GatewayVehicle> findNotVerified() throws SQLException {
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM VEHICLE WHERE verification = 'pending';");
        return findTemplate(pS);
    }

    /**
     * @brief Funció que agafa un vehicle de la BD i el retorna
     * @param numberPlate matricula del vehicle
     * @return Es retorna un GatewayVehicle amb tota la info del Vehivle
     */
    public GatewayVehicle findByNumberPlate(String numberPlate) throws SQLException {
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM VEHICLE WHERE numberPlate = ?;");
        pS.setString(1,numberPlate);
        ArrayList<GatewayVehicle> aL = findTemplate(pS);
        if (aL.size() == 0) return null;
        return aL.get(0);
    }

    /**
     * @brief Crear un gateway amb els parametres passats
     * @param r ResultSet que contindra tots el parametres per poder crear el Gateway (brand, model, numberPlate, drivingRange, fabricationYear, seats, imageId)
     * @return Retorna un GatewayVehicle amb els parametres donats
     */
    public GatewayVehicle createGateway(ResultSet r) throws SQLException {
        String brand = r.getString(1);
        String model = r.getString(2);
        String numberPlate = r.getString(3);
        int drivingRange = r.getInt(4);
        Integer fabricationYear= r.getInt(5);
        int seats = r.getInt(6);
        String imageId = r.getString(7);
        String verification = r.getString(8);
        return new GatewayVehicle(brand, model, numberPlate, drivingRange, fabricationYear, seats,imageId, verification);
    }

    //Add necessary finders
}

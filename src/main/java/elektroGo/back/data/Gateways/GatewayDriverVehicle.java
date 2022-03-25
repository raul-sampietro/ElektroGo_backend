/**
 * @file GatewayDriverVehicle.java
 * @author Daniel Pulido
 * @date 15/03/2022
 * @brief Implementacio del Gateway de VehicleDriver.
 */

package elektroGo.back.data.Gateways;

import elektroGo.back.data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * @brief La classe GatewayVehicle implementa el Gateway de VehicleDriver el qual te els atributs de VehicleDriver i fa insert/update/delete a la BD
 */
public class GatewayDriverVehicle implements Gateway {
    /**
     * @brief Matricula del Vehicle
     */
    private final String nPVehicle;
    /**
     * @brief Username del Driver
     */
    private final String userDriver;

    /**
     * @brief Constructora de GatewayDriverVehicle amb els parametres indicats a continuacio.
     * @param nPVehicle Matricula del vehicle
     * @param userDriver Username del driver
     * @post Crea un DriverVehicle amb els parametres indicats previament.
     */
    public GatewayDriverVehicle(String nPVehicle, String userDriver) {
        this.nPVehicle = nPVehicle;
        this.userDriver = userDriver;
    }

    /**
     * @brief Operacio per setejar un prepared statement amb els atributs de l'objecte
     * @pre pS conte una sentencia SQL amb prepared statement a la qual falten per establir dos parametres els quals son nPVehicle i userDriver en aquest ordre
     * @param pS Objecte prepared statement amb una sentencia SQL
     * @post Estableix els parametres de la sentencia SQL de pS amb els atributs de l'objecte
     */
    private void setPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1,nPVehicle);
        pS.setString(2,userDriver);
    }

    public String getnPVehicle() {
        return nPVehicle;
    }

    public String getUserDriver() {
        return userDriver;
    }

    /**
     * @brief Insert de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un insert en la BD amb els atributs de l'objecte
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO DRIVERVEHICLE VALUES (?,?); ");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief No fa res, s'ha de posar perque tots els gateways han de tenir aquesta operacio
     * @pre cert
     * @post No fa res, ja que tots els atributs de la classe son clau primaria
     */
    public void update() throws SQLException {
        //Can't update nothing because all atrbiutes are primary key
    }

    /**
     * @brief Borra l'objecte de la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post L'objecte es esborrat de la BD
     */
    public void remove() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("DELETE FROM DRIVERVEHICLE WHERE nPVehicle = ? and userDriver = ?;");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    public String json() {
        return null;
    }
}

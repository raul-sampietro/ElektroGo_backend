/**
 * @file FinderReport.java
 * @author Daniel Pulido
 * @date 19/04/2022
 * @brief Implementacio del Finder de Report.
 */

package elektroGo.back.data.finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.gateways.GatewayReport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe FinderReport es l'encarregat de conectar-se amb la BD y retornar GW Report d'acord a les consultes que calguin.
 */
public class FinderReport {


    /**
     * @brief FinderReport, es un singleton
     */
    private static FinderReport singletonObject;

    /**
     * @brief Creadora de la clase FinderVehicle
     */
    private FinderReport() {}

    /**
     * @brief Funció que retorna una instancia Singleton de FinderReport
     * @return Es retorna un singletonObject per treballar amb aquesta classe
     */
    public static FinderReport getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderReport();
        }
        return singletonObject;
    }

    /**
     * @brief Crea un gateway amb els parametres passats
     * @param r ResultSet que contindra tots el parametres per poder crear el GatewayReport
     * @return Retorna el GatewayReport amb els parametres del ResultSet
     */
    private GatewayReport createGateway(ResultSet r) throws SQLException {
        String userWhoReports = r.getString(1);
        String reportedUser = r.getString(2);
        String reason = r.getString(3);
        return new GatewayReport(userWhoReports, reportedUser, reason);
    }

    /**
     * @brief Funció que agafa tots els Reports de la BD i els posa a un Array
     * @return Es retorna un array de GatewayReport amb tota la info dels Reports
     */
    public ArrayList<GatewayReport> findAll() throws SQLException {
        GatewayReport gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayReport> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM REPORT;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Cerca tots els reports tals que la seva primary key es la dels parametres indicats a continuacio
     * @param userWhoReports Username de l'usuari que fa la denuncia
     * @param reportedUser Username de l'usuari denunciat
     * @return GatewayReport de l'usuari identificat per aquests parametres
     */
    public GatewayReport findByPrimaryKey(String userWhoReports, String reportedUser) throws SQLException {
        GatewayReport gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM REPORT WHERE userWhoReports = ? and reportedUser = ?;");
        pS.setString(1,userWhoReports);
        pS.setString(2, reportedUser);
        ResultSet r = pS.executeQuery();
        if (r.next()) gV = createGateway(r);
        return gV;
    }

    /**
     * @brief Cerca tots els reports on l'usuari que fa la denuncia es "userWhoReports"
     * @param userWhoReports Usuari que fa la denuncia
     * @return ArrayList de GatewayReport amb els reports que tenen com a usuari que denuncia "userWhoReports"
     */
    public ArrayList<GatewayReport> findByUserWhoReports(String userWhoReports) throws SQLException {
        GatewayReport gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayReport> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM REPORT WHERE userWhoReports = ?;");
        pS.setString(1,userWhoReports);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Cerca tots els reports on l'usuari denunciat es "reportedUser"
     * @param reportedUser Usuari que rep la denuncia
     * @return ArrayList de GatewayReport amb els reports que tenen com a usuari denunciat "reportedUser"
     */
    public ArrayList<GatewayReport> findByReportedUser(String reportedUser) throws SQLException {
        GatewayReport gV = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayReport> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM REPORT WHERE reportedUser = ?;");
        pS.setString(1,reportedUser);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

}
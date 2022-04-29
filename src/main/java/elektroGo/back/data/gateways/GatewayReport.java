/**
 * @file GatewayReport.java
 * @author Daniel Pulido
 * @date 17/04/2022
 * @brief Implementacio del Gateway de Report.
 */

package elektroGo.back.data.gateways;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * @brief La classe GatewayReport implementa el Gateway de Report el qual te els atributs de Report i fa insert/update/delete a la BD
 */
public class GatewayReport implements Gateway {

    /**
     * @brief Usuari que fa la denuncia
     */
    String userWhoReports;
    /**
     * @brief Usuari denunciat
     */
    String reportedUser;

    /**
     * @brief Rao per la qual s'ha fet la denuncia
     */
    String reason;

    public String getUserWhoReports() {
        return userWhoReports;
    }

    public void setUserWhoReports(String userWhoReports) {
        this.userWhoReports = userWhoReports;
    }

    public String getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(String reportedUser) {
        this.reportedUser = reportedUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @brief Constructora buida
     * @post Crea un GatewayReport buit
     */
    public GatewayReport() {
    }

    /**
     * @brief Constructora amb tots els parametres
     * @param userWhoReports Usuari que fa la denuncia
     * @param reportedUser Usuari denunciat
     * @param reason motiu de la denuncia
     * @post Crea un GatewayReport amb els atributs previament indicats
     */
    public GatewayReport(String userWhoReports, String reportedUser, String reason) {
        this.userWhoReports = userWhoReports;
        this.reportedUser = reportedUser;
        this.reason = reason;
    }

    /**
     * @brief Estableix un PreparedStatement amb tots els atributs de GWReport
     * @param pS PreparedStatement en el que establirem els atributs
     * @post El preparedStatement té establerts els atributs de GWReport
     */
    public void setPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1,userWhoReports);
        pS.setString(2,reportedUser);
        pS.setString(3,reason);
    }

    /**
     * @brief Insert de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un insert en la BD amb els atributs de l'objecte
     */
    @Override
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO REPORT VALUES (?,?,?); ");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Update de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un update en la BD amb els atributs de l'objecte
     */
    @Override
    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE REPORT SET reason = ? " +
                "WHERE  userWhoReports = ? and reportedUser = ?;");
        pS.setString(1, reason);
        pS.setString(2,userWhoReports);
        pS.setString(3,reportedUser);
        pS.executeUpdate();
    }

    /**
     * @brief Borra l'objecte de la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post L'objecte es esborrat de la BD
     */
    @Override
    public void remove() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("DELETE FROM REPORT WHERE userWhoReports = ? and reportedUser = ?;");
        pS.setString(1, userWhoReports);
        pS.setString(2,reportedUser);
        pS.executeUpdate();
    }

    /**
     * @return Retorna l'objecte en format JSON amb un String
     * @brief Passa l'objecte a JSON
     * @pre cert
     */
    @Override
    public String json() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}

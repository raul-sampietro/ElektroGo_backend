/**
 * @file GatewayChargingStations.java
 * @author Marc Castells
 * @date 13/03/2022
 * @brief Implementacio del Gateway de les estacions de carrega
 */
package elektroGo.back.data.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderChargingStations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @brief La classe GatewayChargingStations implementa el Gateway de ChargingStations el qual te els atributs de ChargingStations i fa insert/update/delete a la BD
 */
public class GatewayChargingStations implements Gateway{

    /**
     * @brief Identificador de l'estacio de carrega
     */
    private Integer id;

    /**
     * @brief Nom del promotor
     */
    private String promotor_gestor;

    /**
     * @brief Tipus d'accés
     */
    private String acces;

    /**
     * @brief Tipus de velocitat del carregador
     */
    private String tipusVelocitat;

    /**
     * @brief Tipus de connexions de l'estacio de carrega
     */
    private String tipusConnexio;

    /**
     * @brief Latitud del punt on esta situada l'estacio de carrega
     */
    private BigDecimal latitude;

    /**
     * @brief Longitud del punt on esta situada l'estacio de carrega
     */
    private BigDecimal longitude;

    /**
     * @brief Descripció de la situació de l'estacio
     */
    private String designacioDescriptiva;

    /**
     * @brief Potencia de carrega de l'estacio
     */
    private Double kw;

    /**
     * @brief Tipus de corrent de l'estacio
     */
    private String acDc;

    /**
     * @brief Identificador de l'estacio de les dades obertes
     */
    private String idePdr;

    /**
     * @brief Numero de carregadors que hi ha a l'estacio de carrega
     */
    private String numberOfChargers;

    /**
     * @brief tipus de vehicles que ponden fer us de l'estacio
     */
    private String tipusVehicle;


    /**
     * @brief Singleton amb el FinderChargingStations
     */
    private FinderChargingStations fcs;

    //CONSTRUCTORS

    /**
     * @brief Creadora buida de la classe GatewayChargingStations
     */
    public GatewayChargingStations() {

    }

    /**
     * @brief Creadora de la classe GatewayChargingStations
     * @param id Identificador de l'estacio de carrega
     * @param latitude Latitud del punt on esta situada l'estacio de carrega
     * @param longitude Longitud del punt on esta situada l'estacio de carrega
     * @param numberOfChargers Numero de carregadors que hi ha a l'estacio de carrega
     */
    public GatewayChargingStations(Integer id, String promotor_gestor, String acces, String tipus_velocitat, String tipus_connexio,
                                   BigDecimal latitude, BigDecimal longitude, String designacio_descriptiva, Double kw, String ac_dc,
                                   String ident, String numberOfChargers, String tipus_vehicle) {
        this.id = id;
        this.promotor_gestor = promotor_gestor;
        this.acces = acces;
        this.tipusVelocitat = tipus_velocitat;
        this.tipusConnexio = tipus_connexio;
        this.latitude = latitude;
        this.longitude = longitude;
        this.designacioDescriptiva = designacio_descriptiva;
        this.kw = kw;
        this.acDc = ac_dc;
        this.idePdr = ident;
        this.numberOfChargers = numberOfChargers;
        this.tipusVehicle = tipus_vehicle;
    }


    //GETTERS AND SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromotor_gestor() {
        return promotor_gestor;
    }

    public void setPromotor_gestor(String promotor_gestor) {
        this.promotor_gestor = promotor_gestor;
    }

    public String getAcces() {
        return acces;
    }

    public void setAcces(String acces) {
        this.acces = acces;
    }

    public String getTipusVelocitat() {
        return tipusVelocitat;
    }

    public void setTipusVelocitat(String tipusVelocitat) {
        this.tipusVelocitat = tipusVelocitat;
    }

    public String getTipusConnexio() {
        return tipusConnexio;
    }

    public void setTipusConnexio(String tipusConnexio) {
        this.tipusConnexio = tipusConnexio;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getDesignacioDescriptiva() {
        return designacioDescriptiva;
    }

    public void setDesignacioDescriptiva(String designacioDescriptiva) {
        this.designacioDescriptiva = designacioDescriptiva;
    }

    public Double getKw() {
        return kw;
    }

    public void setKw(Double kw) {
        this.kw = kw;
    }

    public String getAcDc() {
        return acDc;
    }

    public void setAcDc(String acDc) {
        this.acDc = acDc;
    }

    public String getIdePdr() {
        return idePdr;
    }

    public void setIdePdr(String idePdr) {
        this.idePdr = idePdr;
    }

    public String getNumberOfChargers() {
        return numberOfChargers;
    }

    public void setNumberOfChargers(String numberOfChargers) {
        this.numberOfChargers = numberOfChargers;
    }

    public String getTipusVehicle() {
        return tipusVehicle;
    }

    public void setTipusVehicle(String tipusVehicle) {
        this.tipusVehicle = tipusVehicle;
    }

    public FinderChargingStations getFcs() {
        return fcs;
    }

    public void setFcs(FinderChargingStations fcs) {
        this.fcs = fcs;
    }


    //SQL SENTENCES

    /**
     * @brief Col·loca tota la info requerida (id, latitude, longitude, numberOfChargers) al PreparedStatement
     * @param pS PreparedStatement al que volem col·locar la info
     * @pre S'ens passa un pS
     * @post el pS queda assignat amb la info requerida
     */
    private void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setInt(1,id);
        if (promotor_gestor != null) pS.setString(2,promotor_gestor);else pS.setString(2, null);
        if (acces != null) pS.setString(3,acces);else pS.setString(3, null);
        if (tipusVelocitat != null) pS.setString(4,tipusVelocitat);else pS.setString(4, null);
        if (tipusConnexio != null) pS.setString(5,tipusConnexio);else pS.setString(5, null);
        pS.setBigDecimal(6, latitude);
        pS.setBigDecimal(7, longitude);
        if (designacioDescriptiva != null) pS.setString(8,designacioDescriptiva);else pS.setString(8, null);
        if (kw != null) pS.setDouble(9,kw);else pS.setString(9, null);
        if (acDc != null) pS.setString(10,acDc);else pS.setString(10, null);
        if (idePdr != null) pS.setString(11,idePdr);else pS.setString(11, null);
        if (numberOfChargers != null) pS.setString(12,numberOfChargers);else pS.setString(12, null);
        if (tipusVehicle != null) pS.setString(13,tipusVehicle);else pS.setString(13, null);
    }

    /**
     * @brief Col·loca tota la info requerida (latitude, longitude, numberOfChargers) al PreparedStatement
     * @param pS PreparedStatement al que volem col·locar la info
     * @pre S'ens passa un pS
     * @post el pS queda assignat amb la info requerida
     */
    private void setPreparedStatementIdEnd(PreparedStatement pS) throws SQLException {
        if (promotor_gestor != null) pS.setString(1,promotor_gestor);else pS.setString(1, null);
        if (acces != null) pS.setString(2,acces);else pS.setString(2, null);
        if (tipusVelocitat != null) pS.setString(3,tipusVelocitat);else pS.setString(3, null);
        if (tipusConnexio != null) pS.setString(4,tipusConnexio);else pS.setString(4, null);
        pS.setBigDecimal(5, latitude);
        pS.setBigDecimal(6, longitude);
        if (designacioDescriptiva != null) pS.setString(7,designacioDescriptiva);else pS.setString(7, null);
        if (kw != null) pS.setDouble(8,kw);else pS.setString(8, null);
        if (acDc != null) pS.setString(9,acDc);else pS.setString(9, null);
        if (idePdr != null) pS.setString(10,idePdr);else pS.setString(10, null);
        if (numberOfChargers != null) pS.setString(11,numberOfChargers);else pS.setString(11, null);
        if (tipusVehicle != null) pS.setString(12,tipusVehicle);else pS.setString(12, null);
        pS.setInt(13,id);
    }

    /**
     * @brief Funció d'insertar l'estacio de carrega a la base de dades
     * @post Queda l'estacio de carrega afegida a la base de dades
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO CHARGINGSTATIONS VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?); ");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Funció que actualitza l'estacio de carrega a la base de dades
     * @post Queda l'estacio de carrega actualitzada a la base de dades
     */
    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE CHARGINGSTATIONS SET promotorGestor = ?, acces = ?, " +
                " tipusVelocitat = ?, tipusConnexio = ?, latitude = ?, longitude = ?, descriptiva_deseignacio = ?," +
                " kw = ?, AcDc = ?, ident = ?, numeroPlaces = ?, tipus_vehicle = ? WHERE id = ?;");
        setPreparedStatementIdEnd(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Funció que elimina l'estacio de carrega de la base de dades
     * @post L'estacio de carrega es eliminada de la base de dades
     */
    public void remove() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("DELETE FROM CHARGINGSTATIONS WHERE id=" + id + ";");
    }

    /**
     * @brief Funcio converteix en un String json un GatewayChargingStation
     * @post El GatewayChargingStation esta en format String json
     * @return Es retorna el String Json amb la info del GatewayChargingStation
     */
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

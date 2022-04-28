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
    private String tipus_velocitat;

    /**
     * @brief Tipus de connexions de l'estacio de carrega
     */
    private String tipus_connexio;

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
    private String designacio_descriptiva;

    /**
     * @brief Potencia de carrega de l'estacio
     */
    private Double kw;

    /**
     * @brief Tipus de corrent de l'estacio
     */
    private String ac_dc;

    /**
     * @brief Identificador de l'estacio de les dades obertes
     */
    private String ide_pdr;

    /**
     * @brief Numero de carregadors que hi ha a l'estacio de carrega
     */
    private String numberOfChargers;

    /**
     * @brief tipus de vehicles que ponden fer us de l'estacio
     */
    private String tipus_vehicle;


    /**
     * @brief Singleton amb el FinderChargingStations
     */
    private FinderChargingStations fcs;

    //CONSTRUCTORS

    /**
     * @brief Creadora buida de la classe GatewayChargingStations
     * @return Retorna la instancia del gateway que s'acaba de crear
     */
    public GatewayChargingStations() {

    }

    /**
     * @brief Creadora de la classe GatewayChargingStations
     * @param id Identificador de l'estacio de carrega
     * @param latitude Latitud del punt on esta situada l'estacio de carrega
     * @param longitude Longitud del punt on esta situada l'estacio de carrega
     * @param numberOfChargers Numero de carregadors que hi ha a l'estacio de carrega
     * @return Retorna la instancia del gateway que s'acaba de crear
     */
    public GatewayChargingStations(Integer id, String promotor_gestor, String acces, String tipus_velocitat, String tipus_connexio,
                                   BigDecimal latitude, BigDecimal longitude, String designacio_descriptiva, Double kw, String ac_dc,
                                   String ident, String numberOfChargers, String tipus_vehicle) {
        this.id = id;
        this.promotor_gestor = promotor_gestor;
        this.acces = acces;
        this.tipus_velocitat = tipus_velocitat;
        this.tipus_connexio = tipus_connexio;
        this.latitude = latitude;
        this.longitude = longitude;
        this.designacio_descriptiva = designacio_descriptiva;
        this.kw = kw;
        this.ac_dc = ac_dc;
        this.ide_pdr = ident;
        this.numberOfChargers = numberOfChargers;
        this.tipus_vehicle = tipus_vehicle;
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

    public String getTipus_velocitat() {
        return tipus_velocitat;
    }

    public void setTipus_velocitat(String tipus_velocitat) {
        this.tipus_velocitat = tipus_velocitat;
    }

    public String getTipus_connexio() {
        return tipus_connexio;
    }

    public void setTipus_connexio(String tipus_connexio) {
        this.tipus_connexio = tipus_connexio;
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

    public String getDesignacio_descriptiva() {
        return designacio_descriptiva;
    }

    public void setDesignacio_descriptiva(String designacio_descriptiva) {
        this.designacio_descriptiva = designacio_descriptiva;
    }

    public Double getKw() {
        return kw;
    }

    public void setKw(Double kw) {
        this.kw = kw;
    }

    public String getAc_dc() {
        return ac_dc;
    }

    public void setAc_dc(String ac_dc) {
        this.ac_dc = ac_dc;
    }

    public String getIde_pdr() {
        return ide_pdr;
    }

    public void setIde_pdr(String ide_pdr) {
        this.ide_pdr = ide_pdr;
    }

    public String getNumberOfChargers() {
        return numberOfChargers;
    }

    public void setNumberOfChargers(String numberOfChargers) {
        this.numberOfChargers = numberOfChargers;
    }

    public String getTipus_vehicle() {
        return tipus_vehicle;
    }

    public void setTipus_vehicle(String tipus_vehicle) {
        this.tipus_vehicle = tipus_vehicle;
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
        if (tipus_velocitat != null) pS.setString(4,tipus_velocitat);else pS.setString(4, null);
        if (tipus_connexio != null) pS.setString(5,tipus_connexio);else pS.setString(5, null);
        pS.setBigDecimal(6, latitude);
        pS.setBigDecimal(7, longitude);
        if (designacio_descriptiva != null) pS.setString(8,designacio_descriptiva);else pS.setString(5, null);
        if (kw != null) pS.setDouble(9,kw);else pS.setString(9, null);
        if (ac_dc != null) pS.setString(10,ac_dc);else pS.setString(10, null);
        if (ide_pdr != null) pS.setString(11,ide_pdr);else pS.setString(11, null);
        if (numberOfChargers != null) pS.setString(12,numberOfChargers);else pS.setString(12, null);
        if (tipus_vehicle != null) pS.setString(13,tipus_vehicle);else pS.setString(13, null);
    }

    /**
     * @brief Col·loca tota la info requerida (latitude, longitude, numberOfChargers) al PreparedStatement
     * @param pS PreparedStatement al que volem col·locar la info
     * @pre S'ens passa un pS
     * @post el pS queda assignat amb la info requerida
     */
    private void setPreparedStatementNoID(PreparedStatement pS) throws SQLException {
        pS.setBigDecimal(1, latitude);
        pS.setBigDecimal(2,longitude);
        if (numberOfChargers != null) pS.setString(3,numberOfChargers); else pS.setString(3, null);
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
        PreparedStatement pS = c.prepareStatement("UPDATE CHARGINGSTATIONS SET latitude = ?, longitude = ?," +
                " numeroPlaces = ? WHERE id = ?;");
        setPreparedStatementNoID(pS);
        pS.setLong(4, id);
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

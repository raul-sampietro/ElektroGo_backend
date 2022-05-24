/**
 * @file FinderBlock.java
 * @author Daniel Pulido
 * @date 19/05/2022
 * @brief Implementacio del Finder de Block.
 */

package elektroGo.back.data.finders;

import elektroGo.back.data.Database;
import elektroGo.back.data.gateways.GatewayBlock;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe FinderBlock es l'encarregat de conectar-se amb la BD y retornar GW Block d'acord a les consultes que calguin.
 */
public class FinderBlock {


    /**
     * @brief FinderBlock, es un singleton
     */
    private static FinderBlock singletonObject;

    /**
     * @brief Creadora de la clase FinderBlock
     */
    private FinderBlock() {}

    /**
     * @brief Funció que retorna una instancia Singleton de FinderBlock
     * @return Es retorna un singletonObject per treballar amb aquesta classe
     */
    public static FinderBlock getInstance() {
        if (singletonObject == null) {
            singletonObject = new FinderBlock();
        }
        return singletonObject;
    }

    /**
     * @brief Crea un gateway amb els parametres passats
     * @param r ResultSet que contindra tots el parametres per poder crear el GatewayBlock
     * @return Retorna el GatewayReport amb els parametres del ResultSet
     */
    private GatewayBlock createGateway(ResultSet r) throws SQLException {
        String userBlocking = r.getString(1);
        String blockUser = r.getString(2);
        return new GatewayBlock(userBlocking, blockUser);
    }

    /**
     * @brief Funció que agafa tots els Block de la BD i els posa a un Array
     * @return Es retorna un array de GatewayBlock amb tota la info dels Block
     */
    public ArrayList<GatewayBlock> findAll() throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayBlock> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM REPORT;");
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Cerca tots els blocats tals que la seva primary key es la dels parametres indicats a continuacio
     * @param userBlocking Username de l'usuari que fa la denuncia
     * @param blockUser de l'usuari blocat
     * @return GatewayRBlock de l'usuari identificat per aquests parametres
     */
    public GatewayBlock findByPrimaryKey(String userBlocking, String blockUser) throws SQLException {
        GatewayBlock gB = null;
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM BLOCK WHERE userBlocking = ? and blockUser = ?;");
        pS.setString(1,userBlocking);
        pS.setString(2, blockUser);
        ResultSet r = pS.executeQuery();
        if (r.next()) gB = createGateway(r);
        return gB;
    }

    /**
     * @brief Cerca tots els reports on l'usuari que fa blocat
     * @param userBlocking Usuari que fa la blocat
     * @return ArrayList de GatewayBlock amb els reports que tenen com a usuari el userBlocking
     */
    public ArrayList<GatewayBlock> findByUserBlocking(String userBlocking) throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayBlock> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM BLOCK WHERE userBlocking = ?;");
        pS.setString(1,userBlocking);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

    /**
     * @brief Cerca tots els reports on l'usuari que el bloquegen
     * @param blockUser que fa la blocat
     * @return ArrayList de GatewayBlock amb els reports que tenen com a blocat el userBlocking
     */
    public ArrayList<GatewayBlock> findByBlockUser(String blockUser) throws SQLException {
        Database d = Database.getInstance();
        Connection conn = d.getConnection();
        ArrayList<GatewayBlock> aL = new ArrayList<>();
        PreparedStatement pS = conn.prepareStatement("SELECT * FROM BLOCK WHERE blockUser = ?;");
        pS.setString(1,blockUser);
        ResultSet r = pS.executeQuery();
        while (r.next()) {
            aL.add(createGateway(r));
        }
        return aL;
    }

}
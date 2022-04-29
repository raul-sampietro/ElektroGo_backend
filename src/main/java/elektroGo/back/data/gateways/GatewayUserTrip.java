/**
 * @file GatewayUserTrip.java
 * @author Gerard Castell
 * @date 28/04/2022
 * @brief Implementacio del Gateway de UserTrip.
 */

package elektroGo.back.data.gateways;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import elektroGo.back.data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * @brief La classe GatewayUserTrip implementa el Gateway de UserTrip el qual te els atributs i fa insert/update/delete a la BD
 */
public class GatewayUserTrip implements Gateway {
    /**
     * @brief id Trip
     */
    private final Integer id;
    /**
     * @brief Username del Trip
     */
    private final String username;

    /**
     * @brief Constructora de GatewayUserTrip amb els parametres indicats a continuacio.
     * @param id id viatge
     * @param username Username del participant
     * @post Crea un UserTrip amb els parametres indicats previament.
     */
    public GatewayUserTrip(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public Integer getId() {return id;}
    public String getUsername() {return username;}

    /**
     * @brief Operacio per setejar un prepared statement amb els atributs de l'objecte
     * @pre pS conte una sentencia SQL amb prepared statement a la qual falten per establir dos parametres els quals son id i username en aquest ordre
     * @param pS Objecte prepared statement amb una sentencia SQL
     * @post Estableix els parametres de la sentencia SQL de pS amb els atributs de l'objecte
     */
    private void setPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setInt(1,id);
        pS.setString(2,username);
    }

    /**
     * @brief Insert de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un insert en la BD amb els atributs de l'objecte
     */
    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO USERTRIP VALUES (?,?); ");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief No fa res, s'ha de posar perque tots els gateways han de tenir aquesta operacio
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
        PreparedStatement pS = c.prepareStatement("DELETE FROM USERTRIP WHERE id = ? and username = ?;");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    /**
     * @brief Funció converteix en un String json un Gateway
     * @post El GW esta en format String json
     * @return es retorna el String Json amb la info del GW
     */
    public String json() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = "";
        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}


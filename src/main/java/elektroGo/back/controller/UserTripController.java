/**
 * @file UserTripController.java
 * @author Gerard Castell
 * @date 29/04/2023
 * @brief Implementacio de la classe UserTrip
 */

package elektroGo.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.finders.FinderTrip;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.finders.FinderUserTrip;
import elektroGo.back.data.gateways.GatewayTrip;
import elektroGo.back.data.gateways.GatewayUser;
import elektroGo.back.data.gateways.GatewayUserTrip;
import elektroGo.back.exceptions.*;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe UTController es la classe que comunicarà front-end i back-end alhora de tractar amb dades dels TRIPS assignats a USERS
 */
@RestController
public class UserTripController {

    //NO TE SENTIT
    /**
     * @brief Funció amb metode 'GET' que retorna la informació del membre amb el username i l'id del viatgecorresponen
     * @param username Usuari del que volem agafar la info
     * @param id id del viatge
     * @return Es retorna un String amb la info del usuari demanada
     */
    @GetMapping("/userTrip")
    public String getUserTrip(@RequestParam Integer id, @RequestParam String username) throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gU = fU.findByUsername(username);
        if(gU == null)throw new UserNotFound(username);
        FinderTrip fT = FinderTrip.getInstance();
        GatewayTrip gT = fT.findById(id);
        if(gT == null)throw new TripNotFound(id);
        FinderUserTrip fUT = FinderUserTrip.getInstance();
        GatewayUserTrip gUT = fUT.findByTripUser(id,username);
        if(gUT == null) throw new UserTripNotFound(username, id);
        return gU.json();
    }

    // Obté els UserTrip d'un user. ELIMINAR.
    @GetMapping("/userTrip/byUser")
    public ArrayList<GatewayUserTrip> getUserTripUSer(@RequestParam String username) throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gU = fU.findByUsername(username);
        if(gU == null)throw new UserNotFound(username);
        FinderUserTrip fUT = FinderUserTrip.getInstance();
        ArrayList<GatewayUserTrip> gUT = fUT.findTripByUser(username);
        if(gUT == null) throw new UserTripNotFound();
        return gUT;
    }

    // Obté els trips d'un user
    @GetMapping("/userTrip/TripByUser")
    public ArrayList<GatewayTrip> getUserTripUSerinfo(@RequestParam String username) throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gU = fU.findByUsername(username);
        if(gU == null)throw new UserNotFound(username);

        FinderUserTrip fUT = FinderUserTrip.getInstance();
        ArrayList<GatewayUserTrip> gUT = fUT.findTripByUser(username);
        if(gUT == null) throw new UserTripNotFound();
        ArrayList<GatewayTrip> end = new ArrayList<>();
        FinderTrip fT = FinderTrip.getInstance();
        for (GatewayUserTrip gatewayUserTrip : gUT) {
            end.add(fT.findById(gatewayUserTrip.getId()));
        }
        return end;
    }

    // ELIMINAR
    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els trips a la BD
     * @return Es retorna un String amb la info dels trips
     */
    @GetMapping("/userTrips")
    public String getUserTrip() throws SQLException, JsonProcessingException {
        FinderUserTrip fU = FinderUserTrip.getInstance();
        ArrayList<GatewayUserTrip> lU = fU.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(lU);
    }

    /**
     * @brief Funció amb metode 'POST' que crearà un Trip amb la info requerida
     * @param gD GatewayTrip amb tota la informació necessaria
     * @post S'afegeix Trip la BD
     */
    @PostMapping("/userTrip/create")
    public void createUserTrip(@RequestBody GatewayUserTrip gD) throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gU = fU.findByUsername(gD.getUsername());
        if(gU == null)throw new UserNotFound(gD.getUsername());
        FinderTrip fT = FinderTrip.getInstance();
        GatewayTrip gT = fT.findById(gD.getId());
        if(gT == null)throw new TripNotFound(gD.getId());
        FinderUserTrip fUT = FinderUserTrip.getInstance();
        if (fUT.findByTripUser(gD.getId(),gD.getUsername()) != null) throw new UserTripAlreadyExists(gD.getId(),gD.getUsername());
        gD.insert();
    }

    /**
     * @brief Funció amb metode 'POST' que demana que s'esborri un userTrip de la BD
     * @param username Usuari que volem eliminar
     * @param id id del Trip
     * @post El usertrip s'elimina de la BD
     */
    @PostMapping("/userTrip/delete")
    public void deleteDriver(@RequestParam Integer id,@RequestParam String username) {
        FinderUserTrip fD = FinderUserTrip.getInstance();
        try {
            GatewayUserTrip gD = fD.findByTripUser(id,username);
            if (gD != null) gD.remove();
            else throw new UserTripNotFound(username,id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

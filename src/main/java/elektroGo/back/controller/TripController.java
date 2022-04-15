/**
 * @file TripController.java
 * @author Gerard Castell
 * @date 15/04/2023
 * @brief Implementació de la classe TripController
 */

package elektroGo.back.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import elektroGo.back.data.finders.FinderTrip;
import elektroGo.back.data.gateways.GatewayTrip;
import elektroGo.back.exceptions.TripAlreadyExists;
import elektroGo.back.exceptions.TripNotFound;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe TripController és la classe que comunicarà front-end i back-end a l'hora de tractar amb dades dels Trips
 */
@RestController
public class TripController {

    /**
     * @brief Funció amb metode 'GET' que retorna la informació del trip amb el id corresponen
     * @param id Trip del que volem agafar la info
     * @return Es retorna un String amb la info del trip demanada
     */
    @GetMapping("/trip")
    public GatewayTrip getTrip(@RequestParam String id) throws SQLException {
        FinderTrip fT = FinderTrip.getInstance();
        GatewayTrip gT = fT.findById(id);
        if(gT == null)throw new TripNotFound(id);
        return gT;
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els Users a la BD
     * @return Es retorna un String amb la info dels usuaris
     */
    @GetMapping("/trips")
    public ArrayList<GatewayTrip> getTrips() throws SQLException, JsonProcessingException {
        FinderTrip fT = FinderTrip.getInstance();
        return fT.findAll();
    }

    /**
     * @brief Funció amb metode 'POST' que crearà un Trip amb la info requerida
     * @param gT GatewayTrip amb tota la informació necessaria
     * @post S'afegeix el trip a la BD
     */
    @PostMapping("/trip/create")
    public void createTrip(@RequestBody GatewayTrip gT) throws SQLException {
        FinderTrip fT = FinderTrip.getInstance();
        if (fT.findById(gT.getId()) != null) throw new TripAlreadyExists(gT.getId());
        gT.insert();
    }
    /**
     * @brief Funció amb metode 'POST' que demana que s'esborri un Trip de la BD
     * @param id  que volem eliminar
     * @post El trip s'elimina de la BD
     */

    @PostMapping("/trip/delete")
    public void deleteTrip(@RequestParam String id) {
        FinderTrip fU = FinderTrip.getInstance();
        try {
            GatewayTrip gU = fU.findById(id);
            if (gU != null) gU.remove();
            else throw new TripNotFound(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
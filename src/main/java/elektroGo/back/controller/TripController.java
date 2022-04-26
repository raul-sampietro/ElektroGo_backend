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
import elektroGo.back.exceptions.InvalidKey;
import elektroGo.back.exceptions.TripAlreadyExists;
import elektroGo.back.exceptions.TripNotFound;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.cos;

/**
 * @brief La classe TripController és la classe que comunicarà front-end i back-end a l'hora de tractar amb dades dels Trips
 */
@RestController
public class TripController {
    String password = "test";
    /**
     * @brief Funció amb metode 'GET' que retorna la informació del trip amb el id corresponen
     * @param id Trip del que volem agafar la info
     * @return Es retorna un String amb la info del trip demanada
     */
    @GetMapping("/car-pooling")
    public GatewayTrip getTrip(@RequestParam Integer id) throws SQLException {
        FinderTrip fT = FinderTrip.getInstance();
        GatewayTrip gT = fT.findById(id);
        if(gT == null)throw new TripNotFound(id);
        return gT;
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els Users a la BD
     * @return Es retorna un String amb la info dels usuaris
     */
    @GetMapping("/car-poolings")
    public ArrayList<GatewayTrip> getTrips() throws SQLException, JsonProcessingException {
        FinderTrip fT = FinderTrip.getInstance();
        return fT.findAll();
    }

    /**
     * @brief Funció amb metode 'POST' que crearà un Trip amb la info requerida
     * @param gT GatewayTrip amb tota la informació necessaria
     * @post S'afegeix el trip a la BD
     */
    @PostMapping("/car-pooling/create")
    public void createTrip(@RequestBody GatewayTrip gT) throws SQLException {
        System.out.println(gT.json());
        System.out.println(gT.getUserName());
        FinderTrip fT = FinderTrip.getInstance();
        //if (fT.findById(gT.getId()) != null) throw new TripAlreadyExists(gT.getId());
        gT.insert();
    }
    /**
     * @brief Funció amb metode 'POST' que demana que s'esborri un Trip de la BD
     * @param id  que volem eliminar
     * @post El trip s'elimina de la BD
     */

    @PostMapping("/car-pooling/delete")
    public void deleteTrip(@RequestParam Integer id) {
        FinderTrip fU = FinderTrip.getInstance();
        try {
            GatewayTrip gU = fU.findById(id);
            if (gU != null) gU.remove();
            else throw new TripNotFound(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/car-pooling/byCoord")
    public ArrayList<GatewayTrip> getTripByCord(@RequestParam BigDecimal latitude, @RequestParam BigDecimal longitude, @RequestParam BigDecimal Radi,  @RequestParam String key) throws SQLException {
        if(!Objects.equals(key, password))throw new InvalidKey();
        FinderTrip fT = FinderTrip.getInstance();
        BigDecimal radiLat = Radi.multiply(BigDecimal.valueOf(0.00904371));
        System.out.println(radiLat);
        BigDecimal radiLong = BigDecimal.valueOf(Radi.doubleValue()/(111.320*cos(latitude.doubleValue())));
        System.out.println(radiLong);
        BigDecimal a = new BigDecimal("0.5");
        System.out.println(latitude.subtract(a));
        ArrayList<GatewayTrip> corT = fT.findByCoordinates(latitude.subtract(a),latitude.add(a),longitude.subtract(a), longitude.add(a));
        if(corT == null)throw new TripNotFound();
        return corT;
    }




}
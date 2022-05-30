/**
 * @file UserController.java
 * @author Gerard Castell
 * @date 14/03/2022
 * @brief Implementació de la classe UserController
 */

package elektroGo.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import elektroGo.back.data.finders.FinderTrip;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.finders.FinderUserAchievement;
import elektroGo.back.data.finders.FinderUserTrip;
import elektroGo.back.data.gateways.GatewayTrip;
import elektroGo.back.data.gateways.GatewayUser;
import elektroGo.back.data.gateways.GatewayUserAchievements;
import elektroGo.back.data.gateways.GatewayUserTrip;
import elektroGo.back.exceptions.UserAlreadyExists;
import elektroGo.back.exceptions.UserNotFound;
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe UserController és la classe que comunicarà front-end i back-end a l'hora de tractar amb dades dels Users
 */
@RequestMapping("/users")
@RestController
public class UserController {

    private final CustomLogger logger = CustomLogger.getInstance();

    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els Users a la BD
     * @return Es retorna un String amb la info dels usuaris
     */
    @GetMapping("")
    public ArrayList<GatewayUser> getUsers() throws SQLException, JsonProcessingException {
        logger.log("\nStarting getUsers method...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        String log = "";
        ArrayList<GatewayUser> lU = fU.findAll();
        for (GatewayUser gU : lU) log += gU.json() + "\n";
        logger.log("Returning this users... (end of method)", logType.TRACE);
        return lU;
    }

    /**
     * @brief Funció amb metode 'POST' que crearà un User amb la info requerida
     * @param gU GatewayUser amb tota la informació necessaria
     * @post S'afegeix l'usuari a la BD
     */
    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody GatewayUser gU) throws SQLException {
        logger.log("\nStarting createUser method with username " + gU.getUsername() + " ...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(gU.getUsername()) != null) throw new UserAlreadyExists(gU.getUsername());
        gU.insert();
        logger.log("User inserted (End of method)", logType.TRACE);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació del user amb el username corresponen
     * @param username Usuari del que volem agafar la info
     * @return Es retorna un String amb la info del usuari demanada
     */
    @GetMapping("/{username}")
    public String getUserByUsername(@PathVariable String username) throws SQLException {
        String log = "\nStarting getUserByUsername method with this parameters:\n";
        log += " username = " + username;
        logger.log(log, logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gU = null;
        gU = fU.findByUsername(username);
        if(gU == null) throw new UserNotFound(username);
        logger.log("Returning this user: " + gU.json(), logType.TRACE);
        return gU.json();
    }

    /**
     * @brief Funció amb metode 'POST' que demana que s'esborri un User de la BD
     * @param username Usuari que volem eliminar
     * @post El usuari s'elimina de la BD
     */
    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        logger.log("Starting deleteUser method with username '" + username + "'...", logType.TRACE );
        FinderUser fU = FinderUser.getInstance();
        try {
            GatewayUser gU = fU.findByUsername(username);
            if (gU != null) {
                FinderUserTrip fUT = FinderUserTrip.getInstance();
                ArrayList<GatewayUserTrip> aL = fUT.findTripByUser(username);
                FinderTrip fT = FinderTrip.getInstance();
                for (GatewayUserTrip gUT : aL) {
                    GatewayTrip gT = fT.findById(gUT.getId());
                    if (gT.getState().equals("current")) {
                        gT.setOccupiedSeats(gT.getOccupiedSeats()-1);
                        gT.update();
                    }
                }
                gU.remove();
                logger.log("This user was removed succesfully: " + gU.json() + " end of method", logType.TRACE);
            }
            else throw new UserNotFound(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació del user amb els id i provider donats
     * @param id ID de l'Usuari
     * @param provider Proveidor de sessio de l'Usuari
     * @return Es retorna un String amb la info del usuari demanada
     */
    @GetMapping("/provider/{provider}/id/{id}")
    public String getUserById(@PathVariable String id, @PathVariable String provider) throws SQLException {
        String log = "\nStarting getUserById method with this parameters:\n";
        log += " id = " + id;
        log += " provider =  " + provider;
        logger.log(log, logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gU = null;
        gU = fU.findById(id, provider);
        if(gU == null) throw new UserNotFound("unknown");
        logger.log("Returning this user: " + gU.json(), logType.TRACE);
        return gU.json();
    }
}
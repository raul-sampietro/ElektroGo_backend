/**
 * @file DriverController.java
 * @author Gerard Castell
 * @date 14/03/2023
 * @brief Implementació de la classe DriverController
 */

package elektroGo.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.finders.FinderDriver;
import elektroGo.back.data.finders.FinderRating;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayDriver;
import elektroGo.back.exceptions.DriverAlreadyExists;
import elektroGo.back.data.gateways.GatewayRating;
import elektroGo.back.exceptions.DriverNotFound;
import elektroGo.back.exceptions.UserAlreadyExists;
import elektroGo.back.exceptions.UserNotFound;
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe DriverController es la classe que comunicarà front-end i back-end alhora de tractar amb dades dels Drivers
 */
@RequestMapping("/drivers")
@RestController
public class DriverController {

    private final CustomLogger logger = CustomLogger.getInstance();

    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els Drivers a la BD
     * @return Es retorna un String amb la info dels usuaris
     */
    @GetMapping("")
    public String getDrivers() throws SQLException, JsonProcessingException {
        FinderDriver fU = FinderDriver.getInstance();
        ArrayList<GatewayDriver> lU = fU.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(lU);
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació del driver amb el username corresponen
     * @param username Usuari del que volem agafar la info
     * @return Es retorna un String amb la info del usuari demanada
     */
    @GetMapping("/{username}")
    public String getDriver(@PathVariable String username) throws SQLException {
        logger.log("Starting getDriver method with username '" + username + "'...", logType.TRACE);
        FinderDriver fU = FinderDriver.getInstance();
        GatewayDriver gD = fU.findByUserName(username);
        if(gD == null)throw new DriverNotFound(username);
        logger.log("Returning this driver:  " + gD.json() + " end of method", logType.TRACE);
        return gD.json();
    }

    /**
     * @brief Funció amb metode 'POST' que crearà un Driver amb la info requerida
     * @param username Username de l'usuari que es vol solicitar convertir en Driver
     * @post S'afegeix l'usuari a la BD
     */
    @PostMapping("/{username}")
    public void createDriver(@PathVariable String username) throws SQLException {
        logger.log("\nStarting createDriver method with username " + username + " ...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(username) == null) throw new UserNotFound(username);
        FinderDriver fD = FinderDriver.getInstance();
        if (fD.findByUserName(username) != null) throw new DriverAlreadyExists(username);
        GatewayDriver gD = new GatewayDriver(username, false);
        gD.insert();
        logger.log("Driver inserted (End of method)", logType.TRACE);
    }

    @PutMapping("/{username}/verify")
    public void updateDriver(@PathVariable String username) throws SQLException {
        FinderDriver fD = FinderDriver.getInstance();
        GatewayDriver gD = fD.findByUserName(username);
        if (gD == null) throw new DriverNotFound(username);
        gD.setVerified(true);
        gD.update();
    }

    /**
     * @brief Funció amb metode 'POST' que demana que s'esborri un Driver de la BD
     * @param username Usuari que volem eliminar
     * @post El usuari s'elimina de la BD
     */
    @DeleteMapping("/{username}")
    public void deleteDriver(@PathVariable String username) {
        FinderDriver fD = FinderDriver.getInstance();
        try {
            GatewayDriver gD = fD.findByUserName(username);
            if (gD != null) gD.remove();
            else throw new DriverNotFound(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

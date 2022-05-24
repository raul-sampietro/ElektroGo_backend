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
@RestController
public class DriverController {

    private final CustomLogger logger = CustomLogger.getInstance();

    /**
     * @brief Funció amb metode 'GET' que retorna la informació del driver amb el username corresponen
     * @param userName Usuari del que volem agafar la info
     * @return Es retorna un String amb la info del usuari demanada
     */
    @GetMapping("/driver")
    public String getDriver(@RequestParam String userName) throws SQLException {
        logger.log("Starting getDriver method with username '" + userName + "'...", logType.TRACE);
        FinderDriver fU = FinderDriver.getInstance();
        GatewayDriver gU = fU.findByUserName(userName);
        if(gU == null)throw new DriverNotFound(userName);
        logger.log("Returning this driver:  " + gU.json() + " end of method", logType.TRACE);
        return gU.json();
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els Drivers a la BD
     * @return Es retorna un String amb la info dels usuaris
     */
    @GetMapping("/drivers")
    public String getDrivers() throws SQLException, JsonProcessingException {
        FinderDriver fU = FinderDriver.getInstance();
        ArrayList<GatewayDriver> lU = fU.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(lU);
    }

    /**
     * @brief Funció amb metode 'POST' que crearà un Driver amb la info requerida
     * @param gD GatewayDriver amb tota la informació necessaria
     * @post S'afegeix l'usuari a la BD
     */
    @PostMapping("/drivers/create")
    public void createDriver(@RequestBody GatewayDriver gD) throws SQLException {
        logger.log("\nStarting createDriver method with username " + gD.getUsername() + " ...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(gD.getUsername()) == null) throw new UserNotFound(gD.getUsername());
        FinderDriver fD = FinderDriver.getInstance();
        if (fD.findByUserName(gD.getUsername()) != null) throw new UserAlreadyExists(gD.getUsername());
        gD.insert();
        logger.log("Driver inserted (End of method)", logType.TRACE);
    }

    @PostMapping("/driver/update")
    public void updateDriver(@RequestBody GatewayDriver gD) throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(gD.getUsername()) == null) throw new UserNotFound(gD.getUsername());
        FinderDriver fD = FinderDriver.getInstance();
        gD.update();
    }

    /**
     * @brief Funció amb metode 'POST' que demana que s'esborri un Driver de la BD
     * @param userName Usuari que volem eliminar
     * @post El usuari s'elimina de la BD
     */
    @PostMapping("/drivers/delete")
    public void deleteDriver(@RequestParam String userName) {
        FinderDriver fD = FinderDriver.getInstance();
        try {
            GatewayDriver gD = fD.findByUserName(userName);
            if (gD != null) gD.remove();
            else throw new DriverNotFound(userName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

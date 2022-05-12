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
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayDriver;
import elektroGo.back.exceptions.DriverNotFound;
import elektroGo.back.exceptions.UserAlreadyExists;
import elektroGo.back.exceptions.UserNotFound;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe DriverController es la classe que comunicarà front-end i back-end alhora de tractar amb dades dels Drivers
 */
@RestController
public class DriverController {

    /**
     * @brief Funció amb metode 'GET' que retorna la informació del driver amb el username corresponen
     * @param userName Usuari del que volem agafar la info
     * @return Es retorna un String amb la info del usuari demanada
     */
    @GetMapping("/driver")
    public String getDriver(@RequestParam String userName) throws SQLException {
        FinderDriver fU = FinderDriver.getInstance();
        GatewayDriver gU = fU.findByUserName(userName);
        if(gU == null)throw new DriverNotFound(userName);
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
        System.out.println("\nStarting createDriver method with username " + gD.getUsername() + " ...");
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(gD.getUsername()) == null) throw new UserNotFound(gD.getUsername());
        FinderDriver fD = FinderDriver.getInstance();
        if (fD.findByUserName(gD.getUsername()) != null) throw new UserAlreadyExists(gD.getUsername());
        gD.insert();
        System.out.println("Driver inserted (End of method)");
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

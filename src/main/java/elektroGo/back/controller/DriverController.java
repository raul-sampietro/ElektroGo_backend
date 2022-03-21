/**
 * @file DriverController.java
 * @author Gerard Castell
 * @date 14/03/2023
 * @brief Implementació de la classe DriverController
 */

package elektroGo.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Finders.FinderDriver;
import elektroGo.back.data.Gateways.GatewayDriver;
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
     * @pre -
     * @post -
     * @return Es retorna un String amb la info del usuari demanada
     */
    @GetMapping("/driver/{userName}")
    public String getUser(@PathVariable String userName) throws SQLException {
        FinderDriver fU = FinderDriver.getInstance();
        GatewayDriver gU = fU.findByUserName(userName);
        return gU.json();
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els Drivers a la BD
     * @pre -
     * @post -
     * @return Es retorna un String amb la info dels usuaris
     */
    @GetMapping("/drivers")
    public String getUsers() throws SQLException, JsonProcessingException {
        FinderDriver fU = FinderDriver.getInstance();
        ArrayList<GatewayDriver> lU = fU.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(lU);
    }

    /**
     * @brief Funció amb metode 'POST' que crearà un Driver amb la info requerida
     * @param gD GatewayDriver amb tota la informació necessaria
     * @pre -
     * @post S'afegeix l'usuari a la BD
     */
    @PostMapping("/drivers/create")
    public void createVehicle(@RequestBody GatewayDriver gD) {
        try {
            gD.insert();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Funció amb metode 'POST' que demana que s'esborri un Driver de la BD
     * @param userName Usuari que volem eliminar
     * @pre -
     * @post El usuari s'elimina de la BD
     */
    @PostMapping("/drivers/delete")
    public void deleteVehicle(@RequestParam String userName) {
        FinderDriver fD = FinderDriver.getInstance();
        try {
            GatewayDriver gD = fD.findByUserName(userName);
            if (gD != null) gD.remove();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

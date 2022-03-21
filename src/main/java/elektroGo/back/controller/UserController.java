/**
 * @file UserController.java
 * @author Gerard Castell
 * @date 14/03/2023
 * @brief Implementació de la classe UserController
 */

package elektroGo.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Finders.FinderUser;
import elektroGo.back.data.Gateways.GatewayUser;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe UserController és la classe que comunicarà front-end i back-end a l'hora de tractar amb dades dels Users
 */
@RestController
public class UserController {

    /**
     * @brief Funció amb metode 'GET' que retorna la informació del user amb el username corresponen
     * @param userName Usuari del que volem agafar la info
     * @pre -
     * @post -
     * @return Es retorna un String amb la info del usuari demanada
     */
    @GetMapping("/user/{userName}")
    public String getUser(@PathVariable String userName) throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gU = fU.findByUserName(userName);
        return gU.json();
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els Users a la BD
     * @pre -
     * @post -
     * @return Es retorna un String amb la info dels usuaris
     */
    @GetMapping("/users")
    public String getUsers() throws SQLException, JsonProcessingException {
        FinderUser fU = FinderUser.getInstance();
        ArrayList<GatewayUser> lU = fU.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(lU);
    }

    /**
     * @brief Funció amb metode 'POST' que crearà un User amb la info requerida
     * @param gD GatewayUser amb tota la informació necessaria
     * @pre -
     * @post S'afegeix l'usuari a la BD
     */
    @PostMapping("/users/create")
    public void createVehicle(@RequestBody GatewayUser gU) {
        try {
            gU.insert();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Funció amb metode 'POST' que demana que s'esborri un User de la BD
     * @param userName Usuari que volem eliminar
     * @pre -
     * @post El usuari s'elimina de la BD
     */
    @PostMapping("/users/delete")
    public void deleteVehicle(@RequestParam String userName) {
        FinderUser fU = FinderUser.getInstance();
        try {
            GatewayUser gU = fU.findByUserName(userName);
            if (gU != null) gU.remove();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
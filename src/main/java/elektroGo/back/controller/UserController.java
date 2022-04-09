/**
 * @file UserController.java
 * @author Gerard Castell
 * @date 14/03/2023
 * @brief Implementació de la classe UserController
 */

package elektroGo.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayUser;
import elektroGo.back.exceptions.UserAlreadyExists;
import elektroGo.back.exceptions.UserNotFound;
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
     * @return Es retorna un String amb la info del usuari demanada
     */
    @GetMapping("/user")
    public String getUser(@RequestParam String userName) throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gU = fU.findByUserName(userName);
        if(gU == null)throw new UserNotFound(userName);
        return gU.json();
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els Users a la BD
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
     * @param gU GatewayUser amb tota la informació necessaria
     * @post S'afegeix l'usuari a la BD
     */
    @PostMapping("/users/create")
    public void createVehicle(@RequestBody GatewayUser gU) throws SQLException {
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUserName(gU.getUserName()) != null) throw new UserAlreadyExists(gU.getUserName());
        gU.insert();
    }

    /**
     * @brief Funció amb metode 'POST' que demana que s'esborri un User de la BD
     * @param userName Usuari que volem eliminar
     * @post El usuari s'elimina de la BD
     */
    @PostMapping("/users/delete")
    public void deleteVehicle(@RequestParam String userName) {
        FinderUser fU = FinderUser.getInstance();
        try {
            GatewayUser gU = fU.findByUserName(userName);
            if (gU != null) gU.remove();
            else throw new UserNotFound(userName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
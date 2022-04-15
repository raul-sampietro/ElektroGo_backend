/**
 * @file UserController.java
 * @author Gerard Castell
 * @date 14/03/2023
 * @brief Implementació de la classe UserController
 */

package elektroGo.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.finders.FinderRating;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayRating;
import elektroGo.back.data.gateways.GatewayUser;
import elektroGo.back.exceptions.RatingNotFound;
import elektroGo.back.exceptions.UserAlreadyExists;
import elektroGo.back.exceptions.UserNotFound;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * @brief Metode per llegir els ratings que ha fet un usuari
     * @param userName Usuari que ha fet els ratings
     * @return Llistat de ratings que ha fet l'usuari
     */
    @GetMapping("/users/ratings")
    public List<GatewayRating> getRatingsUser(@RequestParam String userName) throws SQLException {
        System.out.println("\nStarting getRatingsUser method with userName '" + userName + "'...");
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUserName(userName) == null) throw new UserNotFound(userName);
        FinderRating fR = FinderRating.getInstance();
        List<GatewayRating> l = fR.findByUserWhoRates(userName);
        System.out.println("Returning this ratings:");
        for (GatewayRating g : l) System.out.println(g.json());
        return fR.findByUserWhoRates(userName);
    }

    /**
     * @brief Metode que retorna els ratings que se li han fet a un usuari
     * @param userName Usuari al qual se li han fet els ratings
     * @return Llistat de ratings que s'han fet a l'usuari "userName"
     */
    @GetMapping("/users/rated")
    public List<GatewayRating> getRated(@RequestParam String userName) throws SQLException {
        System.out.println("\nStarting getRated method with userName '" + userName + "'...");
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUserName(userName) == null) throw new UserNotFound(userName);
        FinderRating fR = FinderRating.getInstance();
        List<GatewayRating> l = fR.findByRatedUser(userName);
        System.out.println("Returning this ratings:");
        for (GatewayRating g : l) System.out.println(g.json());
        return fR.findByRatedUser(userName);
    }

    /**
     * @brief Metode que fa un rating donada la informacio d'aquest a "gR"
     * @param gR GatewayRating amb la informacio necessaria per fer un rating
     * @post Es fa un rating amb la informacio que te "gR"
     */
    @PostMapping("/users/rate")
    public void rateUser(@RequestBody GatewayRating gR) throws SQLException {
        System.out.println("\nStarting rateUser method with this rating:");
        System.out.println(gR.json());
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUserName(gR.getUserWhoRates()) == null) throw new UserNotFound(gR.getUserWhoRates());
        if (fU.findByUserName(gR.getRatedUser()) == null) throw new UserNotFound(gR.getRatedUser());
        FinderRating fR = FinderRating.getInstance();
        if (fR.findByPrimaryKey(gR.getUserWhoRates(), gR.getRatedUser()) != null) {
            //Rating already exists, we have to modify it
            System.out.println("Rating already exist, modifying it...");
            gR.update();
            System.out.println("Rating updated successfully");
        }
        else {
            System.out.println("Rating didn't exist, creating it...");
            gR.insert();
            System.out.println("Rating created successfully");
        }
        System.out.println("End of method");
    }

    /**
     * @brief Metode que esborra el rating identificat pels seguents parametres
     * @param userWhoRates usuari que fa la valoracio
     * @param ratedUser usuari valorat
     * @post S'esborra l'usuari identificat pels parametres abans esmentats
     */
    @PostMapping("/users/unrate")
    public void unrateUser(@RequestParam String userWhoRates, String ratedUser) throws SQLException {
        System.out.println("\nStarting unrateUser method with userWhoRates : '" + userWhoRates + "' and ratedUser: '" + ratedUser + "'...");
        FinderRating fR = FinderRating.getInstance();
        GatewayRating gR = fR.findByPrimaryKey(userWhoRates, ratedUser);
        if (gR == null) throw new RatingNotFound(userWhoRates, ratedUser);
        gR.remove();
        System.out.println("Rating removed successfully, end of method");
    }



}
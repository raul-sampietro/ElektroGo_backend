/**
 * @file RatingController.java
 * @author Raül Sampietro
 * @date 15/5/2022
 * @brief Implementació de la classe RatingController
 */

package elektroGo.back.controller;

import elektroGo.back.data.finders.FinderRating;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayRating;
import elektroGo.back.exceptions.RatingNotFound;
import elektroGo.back.exceptions.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RequestMapping("/ratings")
@RestController
public class RatingController {

    /**
     * @brief Metode que fa un rating donada la informacio d'aquest a "gR"
     * @param gR GatewayRating amb la informacio necessaria per fer un rating
     * @post Es fa un rating amb la informacio que te "gR"
     */
    @PostMapping("")
    public ResponseEntity<?> rateUser(@RequestBody GatewayRating gR) throws SQLException {
        System.out.println("\nStarting rateUser method with this rating:");
        System.out.println(gR.json());
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(gR.getUserWhoRates()) == null) throw new UserNotFound(gR.getUserWhoRates());
        if (fU.findByUsername(gR.getRatedUser()) == null) throw new UserNotFound(gR.getRatedUser());
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
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    /**
     * @brief Metode que esborra el rating identificat pels seguents parametres
     * @param userWhoRates usuari que fa la valoracio
     * @param ratedUser usuari valorat
     * @post S'esborra l'usuari identificat pels parametres abans esmentats
     */
    @DeleteMapping("")
    public void unrateUser(@RequestParam String userWhoRates, String ratedUser) throws SQLException {
        System.out.println("\nStarting unrateUser method with userWhoRates : '" + userWhoRates + "' and ratedUser: '" + ratedUser + "'...");
        FinderRating fR = FinderRating.getInstance();
        GatewayRating gR = fR.findByPrimaryKey(userWhoRates, ratedUser);
        if (gR == null) throw new RatingNotFound(userWhoRates, ratedUser);
        gR.remove();
        System.out.println("Rating removed successfully, end of method");
    }
}

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
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RequestMapping("/ratings")
@RestController
public class RatingController {

    private final CustomLogger logger = CustomLogger.getInstance();

    /**
     * @brief Metode que fa un rating donada la informacio d'aquest a "gR"
     * @param gR GatewayRating amb la informacio necessaria per fer un rating
     * @post Es fa un rating amb la informacio que te "gR"
     */
    @PostMapping("")
    public ResponseEntity<?> rateUser(@RequestBody GatewayRating gR) throws SQLException {
        logger.log("\nStarting rateUser method with this rating:", logType.TRACE);
        logger.log(gR.json(), logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(gR.getUserWhoRates()) == null) throw new UserNotFound(gR.getUserWhoRates());
        if (fU.findByUsername(gR.getRatedUser()) == null) throw new UserNotFound(gR.getRatedUser());
        FinderRating fR = FinderRating.getInstance();
        if (fR.findByPrimaryKey(gR.getUserWhoRates(), gR.getRatedUser()) != null) {
            //Rating already exists, we have to modify it
            logger.log("Rating already exist, updating it...", logType.TRACE);
            gR.update();
            logger.log("Rating updated successfully", logType.TRACE);
        }
        else {
            logger.log("Rating didn't exist, creating it...", logType.TRACE);
            gR.insert();
            logger.log("Rating created successfully", logType.TRACE);
        }
        logger.log("End of method", logType.TRACE);
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
        logger.log("\nStarting unrateUser method with userWhoRates : '" + userWhoRates + "' and ratedUser: '" + ratedUser + "'...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(userWhoRates) == null) throw new UserNotFound(userWhoRates);
        if (fU.findByUsername(ratedUser) == null) throw new UserNotFound(ratedUser);
        FinderRating fR = FinderRating.getInstance();
        GatewayRating gR = fR.findByPrimaryKey(userWhoRates, ratedUser);
        if (gR == null) throw new RatingNotFound(userWhoRates, ratedUser);
        gR.remove();
        logger.log("Rating removed successfully, end of method", logType.TRACE);
    }
}

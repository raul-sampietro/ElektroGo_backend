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
import elektroGo.back.model.avgRate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

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
     * @brief Metode per llegir els ratings que ha fet un usuari
     * @param username Usuari que ha fet els ratings
     * @return Llistat de ratings que ha fet l'usuari
     */
    @GetMapping("/from/{username}")
    public List<GatewayRating> getRatingsMadeUser(@PathVariable String username) throws SQLException {
        logger.log("\nStarting getRatingsMadeUser method with userName '" + username + "'...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(username) == null) throw new UserNotFound(username);
        FinderRating fR = FinderRating.getInstance();
        List<GatewayRating> l = fR.findByUserWhoRates(username);
        String log = "Returning this ratings:";
        for (GatewayRating g : l) log += g.json() + "\n";
        logger.log(log, logType.TRACE);
        return l;
    }

    /**
     * @brief Metode que retorna els ratings que se li han fet a un usuari
     * @param username Usuari al qual se li han fet els ratings
     * @return Llistat de ratings que s'han fet a l'usuari "username"
     */
    @GetMapping("/to/{username}")
    public List<GatewayRating> getRatingsReceivedUser(@PathVariable String username) throws SQLException {
        logger.log("\nStarting getRatingsReceivedUser method with username '" + username + "'...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(username) == null) throw new UserNotFound(username);
        FinderRating fR = FinderRating.getInstance();
        List<GatewayRating> l = fR.findByRatedUser(username);
        String log = "Returning this ratings:";
        for (GatewayRating g : l) log += g.json() + "\n";
        logger.log(log, logType.TRACE);
        return l;
    }

    @GetMapping("/to/{username}/avg")
    public avgRate avgRate(@PathVariable String username) throws SQLException {
        logger.log("\nStarting avgRate method with username : '" + username +"'...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(username) == null) throw new UserNotFound(username);
        FinderRating fR = FinderRating.getInstance();
        avgRate ret = fR.findUserRateAvg(username);
        logger.log("Returning this avgRate: ratingValue = " + ret.getRatingValue() + " and numberOfRatings = " + ret.getNumberOfRatings() + " end of method", logType.TRACE);
        return ret;
    }

    @GetMapping("/from/{userFrom}/to/{userTo}")
    public GatewayRating getSingleRating(@PathVariable String userFrom, @PathVariable String userTo) throws SQLException {
        logger.log("\nStarting getSingleRating from ' " + userFrom + "' to '" + userTo, logType.TRACE);
        FinderRating fR = FinderRating.getInstance();
        GatewayRating gR = fR.findByPrimaryKey(userFrom, userTo);
        if (gR == null) throw new RatingNotFound(userFrom, userTo);
        logger.log("Returning this rating:\n" + gR.json() + "\nEnd of method", logType.TRACE);
        return gR;
    }

    /**
     * @brief Metode que esborra el rating identificat pels seguents parametres
     * @param userFrom usuari que fa la valoracio
     * @param userTo usuari valorat
     * @post S'esborra l'usuari identificat pels parametres abans esmentats
     */
    @DeleteMapping("/from/{userFrom}/to/{userTo}")
    public void unrateUser(@PathVariable String userFrom, @PathVariable String userTo) throws SQLException {
        logger.log("\nStarting unrateUser method from ' " + userFrom + "' to '" + userTo, logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(userFrom) == null) throw new UserNotFound(userFrom);
        if (fU.findByUsername(userTo) == null) throw new UserNotFound(userTo);
        FinderRating fR = FinderRating.getInstance();
        GatewayRating gR = fR.findByPrimaryKey(userFrom, userTo);
        if (gR == null) throw new RatingNotFound(userFrom, userTo);
        gR.remove();
        logger.log("Rating removed successfully, end of method", logType.TRACE);
    }
}

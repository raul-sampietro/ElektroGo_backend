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
import elektroGo.back.data.finders.FinderReport;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.Gateway;
import elektroGo.back.data.gateways.GatewayRating;
import elektroGo.back.data.gateways.GatewayReport;
import elektroGo.back.data.gateways.GatewayUser;
import elektroGo.back.exceptions.RatingNotFound;
import elektroGo.back.exceptions.ReportNotFound;
import elektroGo.back.exceptions.UserAlreadyExists;
import elektroGo.back.exceptions.UserNotFound;
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;
import elektroGo.back.model.avgRate;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @brief La classe UserController és la classe que comunicarà front-end i back-end a l'hora de tractar amb dades dels Users
 */
@RestController
public class UserController {

    private final CustomLogger logger = CustomLogger.getInstance();

    /**
     * @brief Funció amb metode 'GET' que retorna la informació del user amb el username corresponen
     * @param username Usuari del que volem agafar la info
     * @param id ID de l'Usuari
     * @param provider Proveidor de sessio de l'Usuari
     * @return Es retorna un String amb la info del usuari demanada
     */
    @GetMapping("/user")
    public String getUser(@RequestParam(required = false) String username,
                          @RequestParam(required = false) String id,
                          @RequestParam(required = false) String provider) throws SQLException {
        String log = "\nStarting getUser method with this parameters:\n";
        if (username != null) log += " username = " + username;
        if (id != null) log += " id = " + id;
        if (provider != null) log += " provider =  " + provider;
        logger.log(log, logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gU = null;
        if (username != null && id == null && provider == null) {
            gU = fU.findByUsername(username);
        }
        else if (username == null && id != null && provider != null) {
            gU = fU.findById(id, provider);
        }
        if(gU == null) throw new UserNotFound(username);
        logger.log("Returning this user: " + gU.json(), logType.TRACE);
        return gU.json();
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els Users a la BD
     * @return Es retorna un String amb la info dels usuaris
     */
    @GetMapping("/users")
    public ArrayList<GatewayUser> getUsers() throws SQLException, JsonProcessingException {
        logger.log("\nStarting getUsers method...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        String log = "";
        ArrayList<GatewayUser> lU = fU.findAll();
        for (GatewayUser gU : lU) log += gU.json() + "\n";
        logger.log("Returning this users... (end of method)", logType.TRACE);
        return lU;
    }

    /**
     * @brief Funció amb metode 'POST' que crearà un User amb la info requerida
     * @param gU GatewayUser amb tota la informació necessaria
     * @post S'afegeix l'usuari a la BD
     */
    @PostMapping("/users/create")
    public void createUser(@RequestBody GatewayUser gU) throws SQLException {
        logger.log("\nStarting createUser method with username " + gU.getUsername() + " ...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(gU.getUsername()) != null) throw new UserAlreadyExists(gU.getUsername());
        gU.insert();
        logger.log("User inserted (End of method)", logType.TRACE);
    }

    /**
     * @brief Funció amb metode 'POST' que demana que s'esborri un User de la BD
     * @param username Usuari que volem eliminar
     * @post El usuari s'elimina de la BD
     */
    @PostMapping("/users/delete")
    public void deleteUser(@RequestParam String username) {
        logger.log("Starting deleteUser method with username '" + username + "'...", logType.TRACE );
        FinderUser fU = FinderUser.getInstance();
        try {
            GatewayUser gU = fU.findByUsername(username);
            if (gU != null) {
                gU.remove();
                logger.log("This user was removed succesfully: " + gU.json() + " end of method", logType.TRACE);
            }
            else throw new UserNotFound(username);
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
        logger.log("\nStarting getRatingsUser method with userName '" + userName + "'...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(userName) == null) throw new UserNotFound(userName);
        FinderRating fR = FinderRating.getInstance();
        List<GatewayRating> l = fR.findByUserWhoRates(userName);
        String log = "Returning this ratings:";
        for (GatewayRating g : l) log += g.json() + "\n";
        logger.log(log, logType.TRACE);
        return l;
    }

    /**
     * @brief Metode que retorna els ratings que se li han fet a un usuari
     * @param userName Usuari al qual se li han fet els ratings
     * @return Llistat de ratings que s'han fet a l'usuari "userName"
     */
    @GetMapping("/users/rated")
    public List<GatewayRating> getRated(@RequestParam String userName) throws SQLException {
        logger.log("\nStarting getRated method with userName '" + userName + "'...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(userName) == null) throw new UserNotFound(userName);
        FinderRating fR = FinderRating.getInstance();
        List<GatewayRating> l = fR.findByRatedUser(userName);
        String log = "Returning this ratings:";
        for (GatewayRating g : l) log += g.json() + "\n";
        logger.log(log, logType.TRACE);
        return l;
    }

    @GetMapping("/ratings/from/{userFrom}/to/{userTo}")
    public GatewayRating getSingleRating(@PathVariable String userFrom, @PathVariable String userTo) throws SQLException {
        logger.log("\nStarting getSingleRating from ' " + userFrom + "' to '" + userTo, logType.TRACE);
        FinderRating fR = FinderRating.getInstance();
        GatewayRating gR = fR.findByPrimaryKey(userFrom, userTo);
        if (gR == null) throw new RatingNotFound(userFrom, userTo);
        logger.log("Returning this rating:\n" + gR.json() + "\nEnd of method", logType.TRACE);
        return gR;
    }

    /**
     * @brief Metode que fa un rating donada la informacio d'aquest a "gR"
     * @param gR GatewayRating amb la informacio necessaria per fer un rating
     * @post Es fa un rating amb la informacio que te "gR"
     */
    @PostMapping("/users/rate")
    public void rateUser(@RequestBody GatewayRating gR) throws SQLException {
        logger.log("\nStarting rateUser method with this rating:", logType.TRACE);
        logger.log(gR.json(), logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(gR.getUserWhoRates()) == null) throw new UserNotFound(gR.getUserWhoRates());
        if (fU.findByUsername(gR.getRatedUser()) == null) throw new UserNotFound(gR.getRatedUser());
        FinderRating fR = FinderRating.getInstance();
        if (fR.findByPrimaryKey(gR.getUserWhoRates(), gR.getRatedUser()) != null) {
            //Rating already exists, we have to modify it
            logger.log("Rating already exist, modifying it...", logType.TRACE);
            gR.update();
            logger.log("Rating updated successfully", logType.TRACE);
        }
        else {
            logger.log("Rating didn't exist, creating it...", logType.TRACE);
            gR.insert();
            logger.log("Rating created successfully", logType.TRACE);
        }
        logger.log("End of method", logType.TRACE);
    }

    /**
     * @brief Metode que esborra el rating identificat pels seguents parametres
     * @param userWhoRates usuari que fa la valoracio
     * @param ratedUser usuari valorat
     * @post S'esborra l'usuari identificat pels parametres abans esmentats
     */
    @PostMapping("/users/unrate")
    public void unrateUser(@RequestParam String userWhoRates, String ratedUser) throws SQLException {
        logger.log("\nStarting unrateUser method with userWhoRates : '" + userWhoRates + "' and ratedUser: '" + ratedUser + "'...", logType.TRACE);
        FinderRating fR = FinderRating.getInstance();
        GatewayRating gR = fR.findByPrimaryKey(userWhoRates, ratedUser);
        if (gR == null) throw new RatingNotFound(userWhoRates, ratedUser);
        gR.remove();
        logger.log("Rating removed successfully, end of method", logType.TRACE);
    }


    @GetMapping("/user/avgRate")
    public avgRate avgRate(@RequestParam String userName) throws SQLException {
        logger.log("\nStarting avgRate method with userName : '" + userName +"'...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if (fU.findByUsername(userName) == null) throw new UserNotFound(userName);
        FinderRating fR = FinderRating.getInstance();
        avgRate ret = fR.findUserRateAvg(userName);
        logger.log("Returning this avgRate: ratingValue = " + ret.getRatingValue() + " and numberOfRatings = " + ret.getNumberOfRatings() + " end of method", logType.TRACE);
        return ret;
    }

    @GetMapping("/user/reports")
    public List<GatewayReport> reportsUser(@RequestParam String userWhoReports) throws SQLException {
        logger.log("\nStarting reportsUser method with userWhoReports : '" + userWhoReports + "'", logType.TRACE);
        FinderReport fR = FinderReport.getInstance();
        FinderUser fU = FinderUser.getInstance();
        if ( fU.findByUsername(userWhoReports) == null) throw new UserNotFound(userWhoReports);
        List<GatewayReport> l = fR.findByUserWhoReports(userWhoReports);
        String log = "Returning reports with userWhoReports: '" + userWhoReports + "' that are:";
        for (GatewayReport g : l) log += g.json() + "\n";
        logger.log(log + "End of method", logType.TRACE);
        return l;
    }

    @GetMapping("/user/reported")
    public List<GatewayReport> reportedUser(@RequestParam String reportedUser) throws SQLException {
        logger.log("\nStarting reportsUser method with reportedUser : '" + reportedUser + "'", logType.TRACE);
        FinderReport fR = FinderReport.getInstance();
        FinderUser fU = FinderUser.getInstance();
        if ( fU.findByUsername(reportedUser) == null) throw new UserNotFound(reportedUser);
        List<GatewayReport> l = fR.findByReportedUser(reportedUser);
        String log = "Returning reports with reportedUser: '";
        for (GatewayReport g : l) log += g.json() + "\n";
        logger.log(log + "End of method", logType.TRACE);
        return l;
    }

    @PostMapping("/user/report")
    public void reportUser(@RequestBody GatewayReport gR) throws SQLException {
        logger.log("\nStarting reportUser method with report:\n" + gR.json() , logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if ( fU.findByUsername(gR.getUserWhoReports()) == null) throw new UserNotFound(gR.getUserWhoReports());
        if ( fU.findByUsername(gR.getReportedUser()) == null) throw new UserNotFound(gR.getReportedUser());
        FinderReport fR = FinderReport.getInstance();
        //Report doesn't already exist
        if (fR.findByPrimaryKey(gR.getUserWhoReports(), gR.getReportedUser()) == null) {
            logger.log("Report doesn't already exist, creating new report...", logType.TRACE);
            gR.insert();
            logger.log("Report created, end of method", logType.TRACE);
        }
        //Report exists
        else {
            logger.log("Report already exists, updating report...", logType.TRACE);
            gR.update();
            logger.log("Report updated, end of method", logType.TRACE);
        }
    }

    @PostMapping("/user/unreport")
    public void unreportUser(@RequestParam String userWhoReports, @RequestParam String reportedUser) throws SQLException {
        logger.log("\nStarting unreportUser method with userWhoReports: '" + userWhoReports + "' and reportedUser: '" + reportedUser + "'", logType.TRACE);
        FinderReport fR = FinderReport.getInstance();
        GatewayReport gR = fR.findByPrimaryKey(userWhoReports, reportedUser);
        if (gR == null) throw new ReportNotFound(userWhoReports, reportedUser);
        gR.remove();
        if (fR.findByPrimaryKey(userWhoReports, reportedUser) == null) logger.log("Report removed successfully, end of method", logType.TRACE);
        else logger.log("ERROR, couldn't delete the report", logType.ERROR);
    }

    @GetMapping("/users/Allreports")
    public List<GatewayReport> allReports() throws SQLException {
        logger.log("\nStarting allReports method...", logType.TRACE);
        FinderReport fR = FinderReport.getInstance();
        List<GatewayReport> l= fR.findAll();
        String log = "Returning this reports: \n";
        for (GatewayReport g : l) log += g.json() + "\n";
        logger.log(log + "End of method", logType.TRACE);
        return fR.findAll();
    }
}
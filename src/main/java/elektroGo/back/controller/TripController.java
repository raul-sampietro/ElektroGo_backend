/**
 * @file TripController.java
 * @author Gerard Castell
 * @date 15/04/2023
 * @brief Implementació de la classe TripController
 */

package elektroGo.back.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import elektroGo.back.data.finders.*;
import elektroGo.back.data.gateways.GatewayCanceledTrip;
import elektroGo.back.data.gateways.GatewayTrip;
import elektroGo.back.data.gateways.GatewayUser;
import elektroGo.back.data.gateways.GatewayUserTrip;
import elektroGo.back.exceptions.*;
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Math.cos;

/**
 * @brief La classe TripController és la classe que comunicarà front-end i back-end a l'hora de tractar amb dades dels Trips
 */
@RequestMapping("/car-pooling")
@RestController
public class TripController {
        String password = "34ee7e6c4c51e43ed6a7767bc717a7f9127d3d0025a0efbf6af124d15821c6ec";

    private final CustomLogger logger = CustomLogger.getInstance();

    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els Trips a la BD
     * @return Es retorna un String amb la info dels trips
     */
    @GetMapping("")
    public ArrayList<GatewayTrip> getTrips(@RequestParam(required = false) String username,
                                           @RequestParam(required = false) Boolean order) throws SQLException, JsonProcessingException {
        if (username != null) {
            if (order != null && order) {
                logger.log("Starting getTripsOrdered method...", logType.TRACE);
                FinderTrip fT = FinderTrip.getInstance();
                ArrayList<GatewayTrip> all = fT.findOrdered(username);
                if (all == null) throw new TripNotFound();
                String log = "Returning this trips: \n";
                for (GatewayTrip gT : all) log += gT.json() + "\n";
                logger.log(log + "End of method", logType.TRACE);
                return all;
            } else {
                logger.log("Starting getTrips method...", logType.TRACE);
                FinderTrip fT = FinderTrip.getInstance();
                if (fT.findAll() == null) throw new TripNotFound();
                String log = "Returning this trips: \n";
                ArrayList<GatewayTrip> aL = fT.findAll();
                for (GatewayTrip gT : aL) log += gT.json() + "\n";
                logger.log(log + "End of method", logType.TRACE);
                return aL;
            }
        }
        else {
            if (order != null && order) {
                logger.log("Starting getTripsOrdered method...", logType.TRACE);
                FinderTrip fT = FinderTrip.getInstance();
                // TODO userless findOrdered() method
                ArrayList<GatewayTrip> all = fT.findOrdered();
                if (all == null) throw new TripNotFound();
                String log = "Returning this trips: \n";
                for (GatewayTrip gT : all) log += gT.json() + "\n";
                logger.log(log + "End of method", logType.TRACE);
                return all;
            } else {
                logger.log("Starting getTrips method...", logType.TRACE);
                FinderTrip fT = FinderTrip.getInstance();
                if (fT.findAll() == null) throw new TripNotFound();
                String log = "Returning this trips: \n";
                ArrayList<GatewayTrip> aL = fT.findAll();
                for (GatewayTrip gT : aL) log += gT.json() + "\n";
                logger.log(log + "End of method", logType.TRACE);
                return aL;
            }
        }

    }

    /**
     * @brief Funció amb metode 'POST' que crearà un Trip amb la info requerida
     * @param gT GatewayTrip amb tota la informació necessaria
     * @post S'afegeix el trip a la BD
     */
    @PostMapping("")
    public void createTrip(@RequestBody GatewayTrip gT) throws SQLException {
        logger.log("Starting createTrip method with this trip:\n" + gT.json(), logType.TRACE);
        FinderTrip fT = FinderTrip.getInstance();
        if (fT.findByUser(gT.getUsername(),gT.getStartDate(),gT.getStartTime()) != null) throw new TripAlreadyExists(gT.getUsername());
        gT.insert();
        GatewayTrip gNew = fT.findByUser(gT.getUsername(),gT.getStartDate(),gT.getStartTime());
        GatewayUserTrip gU = new GatewayUserTrip(gNew.getId(),gNew.getUsername());
        gU.insert();
        logger.log("Inserted the last trip written and this gatewayUserTrip:\n" + gU.json(),logType.TRACE);
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació del trip amb el id corresponen
     * @param id Trip del que volem agafar la info
     * @return Es retorna un String amb la info del trip demanada
     */
    @GetMapping("/{id}")
    public GatewayTrip getTrip(@PathVariable Integer id) throws SQLException {
        logger.log("Starting getTrip method with id = " + id + "...", logType.TRACE);
        FinderTrip fT = FinderTrip.getInstance();
        GatewayTrip gT = fT.findById(id);
        if(gT == null)throw new TripNotFound(id);
        logger.log("Returning this trip:  " + gT.json(), logType.TRACE);
        return gT;
    }

    /**
     * @brief Funció amb metode 'DELETE' que demana que s'esborri un Trip de la BD
     * @param id  que volem eliminar
     * @post El trip s'elimina de la BD
     */
    @DeleteMapping("/{id}")
    public void deleteTrip(@PathVariable Integer id) {
        logger.log("Starting deleteTrip method with id = " + id, logType.TRACE);
        FinderTrip fU = FinderTrip.getInstance();
        try {
            GatewayTrip gU = fU.findById(id);
            if (gU != null) gU.remove();
            else throw new TripNotFound(id);
            logger.log("Removed succesfully the trip written before, end of method", logType.TRACE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/{id}/users")
    public List<GatewayUser> getTripParticipants(@PathVariable Integer id) throws SQLException {
        logger.log("\nStarting getting Trip Participants...", logType.TRACE);
        FinderTrip fT = FinderTrip.getInstance();
        if (fT.findById(id) == null) throw new TripNotFound();
        FinderUserTrip fUT = FinderUserTrip.getInstance();
        FinderUser fU = FinderUser.getInstance();
        ArrayList<GatewayUserTrip> utl = fUT.findByTrip(id);
        ArrayList<GatewayUser> ul = new ArrayList<>();
        for (GatewayUserTrip gUT : utl) ul.add(fU.findByUsername(gUT.getUsername()));
        return ul;
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació del trip amb el id corresponen
     * @return Es retorna un String amb la info del trip demanada
     */
    @GetMapping("/search")
    public ArrayList<GatewayTrip> getTripSelection(@RequestParam BigDecimal LatO,
                                                   @RequestParam BigDecimal LongO,
                                                   @RequestParam BigDecimal LatD,
                                                   @RequestParam BigDecimal LongD,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sDate,
                                                   @RequestParam(required = false) Time sTimeMin,
                                                   @RequestParam(required = false) Time sTimeMax,
                                                   @RequestParam String username) throws SQLException {
        logger.log("Starting getTripSelection method with this parameters: " + "\n" +
                "LatO = "+ LatO + ", LongO = " + LongO + "LatD = " + LatD + ", LongD = " + LongD + "sDate = " + sDate +
                ", sTimeMin = " + sTimeMin + "and sTimeMax = " + sTimeMax, logType.TRACE);
        FinderTrip fT = FinderTrip.getInstance();
        ArrayList<GatewayTrip> gT;
        BigDecimal a = new BigDecimal("0.05");
        if(sDate == null){
            if(sTimeMax == null){
                if(sTimeMin == null)gT = fT.findByNot(username,LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a));
                else gT = fT.findByMin(username,LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sTimeMin);
            }
            else{
                if(sTimeMin == null)gT = fT.findByMax(username,LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sTimeMax);
                else gT = fT.findByMaxMin(username,LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sTimeMax,sTimeMin);
            }
        }
        else{
            if(sTimeMax == null){
                if(sTimeMin == null)gT = fT.findByDat(username,LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sDate);
                else gT = fT.findByDatMin(username,LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sDate,sTimeMin);
            }
            else{
                if(sTimeMin == null)gT = fT.findByDatMax(username,LatO.subtract(a),LatO.add(a),LongO.subtract(a),
                        LongO.add(a),LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sDate,sTimeMax);
                else gT = fT.findByDatMaxMin(username,LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sDate,sTimeMax,sTimeMin);
            }
        }
        if(gT == null)throw new TripNotFound();

        String log = "Returning this trips: \n";
        for (GatewayTrip gatTrip : gT) log += gatTrip.json() + "\n";
        logger.log(log + "End of method", logType.TRACE);

        return gT;
    }

    @GetMapping("/byCoord")
    public ArrayList<GatewayTrip> getTripByCord(@RequestParam BigDecimal latitude, @RequestParam BigDecimal longitude, @RequestParam BigDecimal Radi,  @RequestParam String key) throws SQLException {
        logger.log("This is an external API method", logType.INFO);
        logger.log("Starting getTripByCoord method with this parameters: \n" +
                "latitude = " + latitude + ", longitude = " + longitude + ", radi = " + Radi + "key = " + key, logType.TRACE);
        logger.log("\nStarting getTripByCord method..." , logType.TRACE);
        if(!Objects.equals(key, password))throw new InvalidKey();
        FinderTrip fT = FinderTrip.getInstance();
        BigDecimal radiLat = Radi.multiply(BigDecimal.valueOf(0.00904371));
        logger.log(radiLat + "", logType.TRACE);
        BigDecimal radiLong = BigDecimal.valueOf(Radi.doubleValue()/(111.320*cos(latitude.doubleValue()))).abs();
        logger.log(radiLong + "", logType.TRACE);
        ArrayList<GatewayTrip> corT = fT.findByCoordinates(latitude.subtract(radiLat),latitude.add(radiLat),longitude.subtract(radiLong), longitude.add(radiLong));
        if (corT.size() == 0) throw new TripNotFound();
        String log = "Returning this coords\n";
        for (GatewayTrip gT : corT) log += gT.json() + "\n";
        logger.log(log, logType.TRACE);
        return corT;
    }

    @GetMapping("/from/{username}")
    public ArrayList<GatewayTrip> getUserTripUSerinfo(@PathVariable String username) throws SQLException {
        logger.log("Starting getUserTripUserInfo method with username '"+ username + "'...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gU = fU.findByUsername(username);
        if(gU == null)throw new UserNotFound(username);

        FinderUserTrip fUT = FinderUserTrip.getInstance();
        ArrayList<GatewayUserTrip> gUT = fUT.findTripByUser(username);
        if(gUT == null) throw new UserTripNotFound();
        ArrayList<GatewayTrip> end = new ArrayList<>();
        FinderTrip fT = FinderTrip.getInstance();
        String log = "Returning this userTrips:\n";
        for (GatewayUserTrip gatewayUserTrip : gUT) {
            end.add(fT.findById(gatewayUserTrip.getId()));
            log += gatewayUserTrip.json() + "\n";
        }
        logger.log(log + "End of method", logType.TRACE);
        return end;
    }

    @PutMapping("/{id}/cancel")
    public void cancel(@PathVariable Integer id, @RequestBody GatewayCanceledTrip gCT) throws SQLException {
        logger.log("Starting cancel trip method with id = " + id + " and CanceledTrip\n"+ gCT.json(), logType.TRACE);
        FinderTrip fT = FinderTrip.getInstance();
        GatewayTrip gT = fT.findById(id);
        if (gT == null) throw new TripNotFound(id);
        gT.setState("cancelled");
        gT.update();
        gT = fT.findById(id);
        logger.log("Trip updated:\n" + gT.json(), logType.TRACE);
        FinderCanceledTrip fCT = FinderCanceledTrip.getInstance();
        if (fCT.findByID(id) == null) gCT.insert();
    }


    /**
     * @brief Funció amb metode 'POST' que crearà un Trip amb la info requerida
     * @param id id del trip
     * @param username username del user
     * @post S'afegeix Trip la BD
     */
    @PostMapping("/{id}/from/{username}")
    public void createUserTrip(@PathVariable Integer id, @PathVariable String username) throws SQLException {
        logger.log("Starting createUserTrip method with userTrip:\n" + username + " " + id + "...", logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        GatewayUser gU = fU.findByUsername(username);
        if(gU == null)throw new UserNotFound(username);
        FinderTrip fT = FinderTrip.getInstance();
        GatewayTrip gT = fT.findById(id);
        if(gT == null)throw new TripNotFound(id);
        FinderUserTrip fUT = FinderUserTrip.getInstance();
        if (fUT.findByTripUser(id,username) != null) throw new UserTripAlreadyExists(id,username);
        if (Objects.equals(gT.getOccupiedSeats(), gT.getOfferedSeats())) throw new TripIsFull(gT.getId(), gT.getOfferedSeats(), gT.getOccupiedSeats());
        gT.setOccupiedSeats(gT.getOccupiedSeats()+1);
        gT.update();
        GatewayUserTrip gUT = new GatewayUserTrip(id, username);
        gUT.insert();
        logger.log("userTrip inserted successfully", logType.TRACE);
    }

    /**
     * @brief Funció amb metode 'DELETE' que demana que s'esborri un userTrip de la BD
     * @param username Usuari que volem eliminar
     * @param id id del Trip
     * @post El usertrip s'elimina de la BD
     */
    @DeleteMapping("/{id}/from/{username}")
    public void deleteDriver(@PathVariable Integer id,@PathVariable String username) {
        logger.log("Starting delete driver method with id = " + id + " and username '" + username + "'...", logType.TRACE);
        FinderUserTrip fD = FinderUserTrip.getInstance();
        try {
            GatewayUserTrip gD = fD.findByTripUser(id,username);
            if (gD != null) {
                gD.remove();
                FinderTrip fT = FinderTrip.getInstance();
                GatewayTrip gT = fT.findById(gD.getId());
                gT.setOccupiedSeats(gT.getOccupiedSeats()-1);
                if (gT.getOccupiedSeats() == -1) {
                    logger.log("Occupied seats would be -1", logType.ERROR);
                }
                else gT.update();
            }
            else throw new UserTripNotFound(username,id);
            logger.log("userTrip removed successfully, end of method", logType.TRACE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Funció amb metode 'GET' que demana que s'esborri un userTrip de la BD
     * @param username Usuari que volem eliminar
     * @post El usertrip s'elimina de la BD
     */
    @GetMapping("/created/{username}")
    public ArrayList<GatewayTrip> getDriver(@PathVariable String username) throws SQLException {
        logger.log("Starting get trip form " + username + "'...", logType.TRACE);
        FinderDriver fd = FinderDriver.getInstance();
        if(fd.findByUserName(username) == null)throw new DriverNotFound(username);
        FinderTrip ft = FinderTrip.getInstance();
        ArrayList<GatewayTrip> gt = ft.findByDriver(username);
        if(gt == null)throw new TripNotFound();
        return gt;
    }

    @PutMapping("/{id}/finish")
    public void endTrip(@PathVariable Integer id) throws SQLException {
        logger.log("Starting finish trip method with id = " + id+"...",logType.TRACE);
        FinderTrip fT = FinderTrip.getInstance();
        GatewayTrip gT = fT.findById(id);
        if (gT == null) throw new TripNotFound(id);
        gT.setState("finished");
        gT.update();
        gT = fT.findById(id);
        logger.log("Trip updated:\n" + gT.json(),logType.TRACE);
    }



}
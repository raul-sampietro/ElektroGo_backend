/**
 * @file TripController.java
 * @author Gerard Castell
 * @date 15/04/2023
 * @brief Implementació de la classe TripController
 */

package elektroGo.back.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import elektroGo.back.data.finders.FinderRating;
import elektroGo.back.data.finders.FinderTrip;
import elektroGo.back.data.gateways.GatewayTrip;
import elektroGo.back.data.gateways.GatewayUserTrip;
import elektroGo.back.exceptions.InvalidKey;
import elektroGo.back.exceptions.TripAlreadyExists;
import elektroGo.back.exceptions.TripNotFound;
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
@RestController
public class TripController {
    String password = "34ee7e6c4c51e43ed6a7767bc717a7f9127d3d0025a0efbf6af124d15821c6ec";
    private final CustomLogger logger = CustomLogger.getInstance();

    /**
     * @brief Funció amb metode 'GET' que retorna la informació del trip amb el id corresponen
     * @param id Trip del que volem agafar la info
     * @return Es retorna un String amb la info del trip demanada
     */
    @GetMapping("/car-pooling")
    public GatewayTrip getTrip(@RequestParam Integer id) throws SQLException {
        logger.log("Starting getTrip method with id = " + id + "...", logType.TRACE);
        FinderTrip fT = FinderTrip.getInstance();
        GatewayTrip gT = fT.findById(id);
        if(gT == null)throw new TripNotFound(id);
        logger.log("Returning this trip:  " + gT.json(), logType.TRACE);
        return gT;
    }
    /**
     * @brief Funció amb metode 'GET' que retorna la informació del trip amb el id corresponen
     * @return Es retorna un String amb la info del trip demanada
     */
    @GetMapping("/car-pooling/sel")
    public ArrayList<GatewayTrip> getTripSelection(@RequestParam BigDecimal LatO, @RequestParam BigDecimal LongO,
                                                   @RequestParam BigDecimal LatD, @RequestParam BigDecimal LongD,
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sDate, Time sTimeMin,
                                                   Time sTimeMax) throws SQLException {
        logger.log("Starting getTripSelection method with this parameters: " + "\n" +
                "LatO = "+ LatO + ", LongO = " + LongO + "LatD = " + LatD + ", LongD = " + LongD + "sDate = " + sDate +
                ", sTimeMin = " + sTimeMin + "and sTimeMax = " + sTimeMax, logType.TRACE);
        FinderTrip fT = FinderTrip.getInstance();
        ArrayList<GatewayTrip> gT;
        BigDecimal a = new BigDecimal("0.05");
        if(sDate == null){
            if(sTimeMax == null){
                if(sTimeMin == null)gT = fT.findByNot(LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a));
                else gT = fT.findByMin(LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sTimeMin);
            }
            else{
                if(sTimeMin == null)gT = fT.findByMax(LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sTimeMax);
                else gT = fT.findByMaxMin(LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sTimeMax,sTimeMin);
            }
        }
        else{
            if(sTimeMax == null){
                if(sTimeMin == null)gT = fT.findByDat(LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sDate);
                else gT = fT.findByDatMin(LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sDate,sTimeMin);
            }
            else{
                if(sTimeMin == null)gT = fT.findByDatMax(LatO.subtract(a),LatO.add(a),LongO.subtract(a),
                        LongO.add(a),LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sDate,sTimeMax);
                else gT = fT.findByDatMaxMin(LatO.subtract(a),LatO.add(a),LongO.subtract(a),LongO.add(a),
                        LatD.subtract(a),LatD.add(a),LongD.subtract(a),LongD.add(a),sDate,sTimeMax,sTimeMin);
            }
        }
        if(gT == null)throw new TripNotFound();

        String log = "Returning this trips: \n";
        for (GatewayTrip gatTrip : gT) log += gatTrip.json() + "\n";
        logger.log(log + "End of method", logType.TRACE);

        return gT;
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els Trips a la BD
     * @return Es retorna un String amb la info dels trips
     */
    @GetMapping("/car-poolings")
    public ArrayList<GatewayTrip> getTrips() throws SQLException, JsonProcessingException {
        logger.log("Starting getTrips method...", logType.TRACE);
        FinderTrip fT = FinderTrip.getInstance();
        if(fT.findAll()==null)throw new TripNotFound();
        String log = "Returning this trips: \n";
        ArrayList<GatewayTrip> aL = fT.findAll();
        for (GatewayTrip gT : aL)  log += gT.json() + "\n";
        logger.log(log + "End of method", logType.TRACE);
        return aL;
    }

    /**
     * @brief Funció amb metode 'GET' que retorna la informació de tots els Users a la BD
     * @return Es retorna un String amb la info dels usuaris
     */
    @GetMapping("/car-poolings/order")
    public ArrayList<GatewayTrip> getTripsOrdered() throws SQLException {
        FinderTrip fT = FinderTrip.getInstance();
        ArrayList<GatewayTrip> all = fT.findOrdered();
        if(all ==null)throw new TripNotFound();
        String log = "Returning this trips: \n";
        for (GatewayTrip gT : all)  log += gT.json() + "\n";
        logger.log(log + "End of method", logType.TRACE);
        return all;
    }


    /**
     * @brief Funció amb metode 'POST' que crearà un Trip amb la info requerida
     * @param gT GatewayTrip amb tota la informació necessaria
     * @post S'afegeix el trip a la BD
     */
    @PostMapping("/car-pooling/create")
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
     * @brief Funció amb metode 'POST' que demana que s'esborri un Trip de la BD
     * @param id  que volem eliminar
     * @post El trip s'elimina de la BD
     */
    @PostMapping("/car-pooling/delete")
    public void deleteTrip(@RequestParam Integer id) {
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

    @GetMapping("/car-pooling/byCoord")
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




}
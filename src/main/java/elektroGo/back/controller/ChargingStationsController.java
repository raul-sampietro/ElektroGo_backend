/**
 * @file ChargingStationsController.java
 * @author Marc Castells
 * @date 15/03/2022
 * @brief Implementacio del Controller de les estacions de càrrega
 */

package elektroGo.back.controller;

import elektroGo.back.data.finders.FinderChargingStations;
import elektroGo.back.data.gateways.GatewayChargingStations;
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe ChargingStationsController permet la comunicació entre back-end i front-end alhora de tractar les estacions de carrega
 */
@RestController
public class ChargingStationsController {

    private final CustomLogger logger = CustomLogger.getInstance();
    /**
     * @brief Metode 'GET' que retorna totes les estacions de càrrega de la base de dades
     * @return Retorna un array amb la informació de tots els punts de càrrega
     */
    @GetMapping("/ChargingStations")
    public ArrayList<GatewayChargingStations> getChargingStations() throws SQLException {
        logger.log("\nGetting all charging stations:\n", logType.TRACE);
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        logger.log("End getChargingStations method", logType.TRACE);
        return fCS.findAll();
    }

    /**
     * @brief Metode 'GET' que retorna totes les estacions de càrrega que es trobin dins del quadrat/rectangle format per les coordenades donades
     * @param latitude1 Latitiud de la primera coordenada
     * @param longitude1 Longitud de la primera coordenada
     * @param latitude2 Latitud de la segona coordenada
     * @param longitude2 Longitud de la segona coordenada
     * @return Retorna un array amb la informacio de tots els punts de carrega els quals estan situats dins de les coordenades donades
     */
    @GetMapping("/ChargingStations/ByCoordinates")
    public ArrayList<GatewayChargingStations> getChargingStationByCoordinates(@RequestParam BigDecimal latitude1, BigDecimal longitude1, BigDecimal latitude2, BigDecimal longitude2) throws SQLException {
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        return fCS.findByCoordinates(latitude1, longitude1, latitude2, longitude2);
    }

}

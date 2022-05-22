/**
 * @file ChargingStationsController.java
 * @author Marc Castells
 * @date 15/03/2022
 * @brief Implementacio del Controller de les estacions de càrrega
 */

package elektroGo.back.controller;

import elektroGo.back.data.finders.FinderChargingStations;
import elektroGo.back.data.gateways.GatewayChargingStations;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La classe ChargingStationsController permet la comunicació entre back-end i front-end alhora de tractar les estacions de carrega
 */
@RequestMapping("/charging-stations")
@RestController
public class ChargingStationsController {

    /**
     * @brief Metode 'GET' que retorna totes les estacions de càrrega de la base de dades
     * @return Retorna un array amb la informació de tots els punts de càrrega
     */
    @GetMapping("")
    public ArrayList<GatewayChargingStations> getChargingStations() throws SQLException {
        System.out.println("\nGetting all charging stations:\n");
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        System.out.println("End getChargingStations method");
        return fCS.findAll();
    }

    /**
     * @brief Metode 'GET' que retorna totes les estacions de càrrega que es trobin dins del rectangle format per les coordenades donades
     * @param latitude1 Latitiud de la primera coordenada
     * @param longitude1 Longitud de la primera coordenada
     * @param latitude2 Latitud de la segona coordenada
     * @param longitude2 Longitud de la segona coordenada
     * @return Retorna un array amb la informacio de tots els punts de carrega els quals estan situats dins de les coordenades donades
     */
    @GetMapping("/byCoord")
    public ArrayList<GatewayChargingStations> getChargingStationByCoordinates(@RequestParam BigDecimal latitude1, BigDecimal longitude1, BigDecimal latitude2, BigDecimal longitude2) throws SQLException {
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        return fCS.findByCoordinates(latitude1, longitude1, latitude2, longitude2);
    }

}

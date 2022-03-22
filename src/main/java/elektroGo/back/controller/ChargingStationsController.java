/**
 * @file ChargingStationsController.java
 * @author Marc Castells
 * @date 15/03/2022
 * @brief Implementació del Controller de les estacions de càrrega
 */


package elektroGo.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Finders.FinderChargingStations;
import elektroGo.back.data.Gateways.GatewayChargingStations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @brief La clase Controller de les estacions de càrrega permetrà la comunicació entre backend i frontend
 */
@RestController
public class ChargingStationsController {

    /**
     * @brief Metode 'GET' que retorna totes les estacions de càrrega de la base de dades
     * @pre -
     * @post -
     * @return Retorna un array amb l'informació de tots els punts de càrrega
     */
    @GetMapping("/ChargingStations")
    public String getChargingStations() throws JsonProcessingException, SQLException {
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        ArrayList<GatewayChargingStations> ArrayCS = fCS.findAll();
        ObjectMapper ob = new ObjectMapper();
        return ob.writeValueAsString(ArrayCS);
    }

    @GetMapping("/ChargingStations/ByCoord")
    public String getChargingStationC(@RequestParam BigDecimal latitude1, BigDecimal longitude1, BigDecimal latitude2, BigDecimal longitude2) throws JsonProcessingException, SQLException {
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        ArrayList<GatewayChargingStations> ArrayCS = fCS.findByCoordinates(latitude1, longitude1, latitude2, longitude2);
        ObjectMapper ob = new ObjectMapper();
        return ob.writeValueAsString(ArrayCS);
    }

}

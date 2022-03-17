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

@RestController
public class ChargingStationsController {

    @GetMapping("/ChargingStations")
    public String getChargingStations() throws JsonProcessingException, SQLException {
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        ArrayList<GatewayChargingStations> ArrayCS = fCS.findAll();
        ObjectMapper ob = new ObjectMapper();
        return ob.writeValueAsString(ArrayCS);
    }

    @GetMapping("/ChargingStations/ByID")
    public String getChargingStation(@RequestParam Integer ChargingStationID) throws JsonProcessingException, SQLException {
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        GatewayChargingStations gCS = fCS.findByID(ChargingStationID);
        ObjectMapper ob = new ObjectMapper();
        return ob.writeValueAsString(gCS);
    }

    @GetMapping("/ChargingStations/ByCoord")
    public String getChargingStationC(@RequestParam BigDecimal latitude1, BigDecimal longitude1, BigDecimal latitude2, BigDecimal longitude2) throws JsonProcessingException, SQLException {
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        ArrayList<GatewayChargingStations> ArrayCS = fCS.findByCoordinates(latitude1, longitude1, latitude2, longitude2);
        ObjectMapper ob = new ObjectMapper();
        return ob.writeValueAsString(ArrayCS);
    }

}

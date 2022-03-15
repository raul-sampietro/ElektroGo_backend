package elektroGo.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Finders.FinderChargingStations;
import elektroGo.back.data.Gateways.GatewayChargingStations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

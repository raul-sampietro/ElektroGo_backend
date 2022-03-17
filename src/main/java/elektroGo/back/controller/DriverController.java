package elektroGo.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Finders.FinderDriver;
import elektroGo.back.data.Gateways.GatewayDriver;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class DriverController {

    @GetMapping("/driver/{userName}")
    public String getUser(@PathVariable String userName) throws SQLException {
        FinderDriver fU = FinderDriver.getInstance();
        GatewayDriver gU = fU.findByUserName(userName);
        return gU.json();
    }

    @GetMapping("/drivers")
    public String getUsers() throws SQLException, JsonProcessingException {
        FinderDriver fU = FinderDriver.getInstance();
        ArrayList<GatewayDriver> lU = fU.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(lU);
    }

    @PostMapping("/drivers/create")
    public void createVehicle(@RequestBody GatewayDriver gD) {
        try {
            gD.insert();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/drivers/delete")
    public void deleteVehicle(@RequestParam String userName) {
        FinderDriver fD = FinderDriver.getInstance();
        try {
            GatewayDriver gD = fD.findByUserName(userName);
            if (gD != null) gD.remove();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

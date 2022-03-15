package elektroGo.back.controller;

import elektroGo.back.data.Finders.FinderVehicle;
import elektroGo.back.data.Gateways.GatewayVehicle;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class VehicleController {

    @PostMapping("/vehicle/create")
    public void createVehicle(@RequestBody GatewayVehicle gV) {
        try {
            gV.insert();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/vehicle/read")
    public String readVehicle(@RequestParam Long id) {
        FinderVehicle fV = FinderVehicle.getInstance();
        try {
            GatewayVehicle gV = fV.findByID(id);
            if (gV == null) return null; //null is fine or show some http error or return something that front knows?
            return gV.json();
        }
        catch (SQLException e ) {
            return e.getSQLState();
        }
    }

    @PostMapping("/vehicle/delete")
    public void deleteVehicle(@RequestParam Long id) {
        FinderVehicle fV = FinderVehicle.getInstance();
        try {
            GatewayVehicle gV = fV.findByID(id);
            if (gV != null) gV.remove();
            //Otherwise, show some http error?
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

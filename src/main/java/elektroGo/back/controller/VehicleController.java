package elektroGo.back.controller;

import elektroGo.back.data.Database;
import elektroGo.back.data.Finders.FinderVehicle;
import elektroGo.back.data.Gateways.GatewayVehicle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
public class VehicleController {

    @PostMapping("/vehicle/create")
    public void createVehicle(@RequestBody GatewayVehicle gV) {
        Database d = Database.getInstance();
        try {
            gV.insert();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/vehicle/provaGetVehicle")
    public String prova(@RequestBody GatewayVehicle gV) {
        return gV.json();
    }

    @GetMapping("/vehicle/provaReadVehicle")
    public String provaReadVehicle() {
        Database d = Database.getInstance();
        try {
            d.executeSQLUpdate("insert into VEHICLE values(22,null,'testModel','5755',null,null,null,null,'Test');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gVTest = null;
        try {
            gVTest = fV.findByID(22);
            d.executeSQLUpdate("delete from VEHICLE where id = 22;");
            return gVTest.json();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping("/vehicle/read")
    public String readVehicle(@RequestParam Long id) {
        FinderVehicle fV = FinderVehicle.getInstance();
        try {
            GatewayVehicle gV = fV.findByID(id);
            return gV.json();
        }
        catch (SQLException e ) {
            return e.getSQLState();
        }

    }


}

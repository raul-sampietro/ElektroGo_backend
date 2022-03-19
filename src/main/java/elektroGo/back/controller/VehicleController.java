package elektroGo.back.controller;

import elektroGo.back.data.Finders.FinderDriver;
import elektroGo.back.data.Finders.FinderDriverVehicle;
import elektroGo.back.data.Finders.FinderVehicle;
import elektroGo.back.data.Gateways.GatewayDriverVehicle;
import elektroGo.back.data.Gateways.GatewayVehicle;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class VehicleController {

    @PostMapping("/vehicle/create")
    public void createVehicle(@RequestBody GatewayVehicle gV, @RequestParam String userNDriver) {
        FinderDriver fD = FinderDriver.getInstance();
        try {
            if (fD.findByUserName(userNDriver) != null) {
                gV.insert();
                GatewayDriverVehicle gDV = new GatewayDriverVehicle(gV.getNumberPlate(), userNDriver);
                gDV.insert();
            }
            //Else throw an exception?
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/vehicle/addDriverVehicle")
    public void addDriverVehicle(@RequestParam String nPVehicle, @RequestParam String userDriver) {
        GatewayDriverVehicle gDV = new GatewayDriverVehicle(nPVehicle,userDriver);
        try {
            gDV.insert();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/vehicle/deleteDriverVehicle")
    public void removeDriverVehicle(@RequestParam String nPVehicle, @RequestParam String userDriver) {
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        try {
            GatewayDriverVehicle gDV = fDV.findByNumberPlateDriver(userDriver, nPVehicle);
            if ( gDV != null)
                gDV.remove();
            //Else throw an exception or something?
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


        @GetMapping("/vehicle/read")
    public String readVehicle(@RequestParam String numberPlate) {
        FinderVehicle fV = FinderVehicle.getInstance();
        try {
            GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
            if (gV == null) return null; //null is fine or show some http error or return something that front knows?
            return gV.json();
        }
        catch (SQLException e ) {
            return e.getSQLState();
        }
    }

    @PostMapping("/vehicle/delete")
    public void deleteVehicle(@RequestParam String numberPlate) { //TEST THIS
        FinderVehicle fV = FinderVehicle.getInstance();
        try {
            GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
            if (gV != null) {
                System.out.println(gV.json());
                FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
                ArrayList<GatewayDriverVehicle> aL = fDV.findByNumberPlateV(numberPlate);
                for (GatewayDriverVehicle gDV : aL) gDV.remove();
                gV.remove();
            }
            //Otherwise, show some http error?
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

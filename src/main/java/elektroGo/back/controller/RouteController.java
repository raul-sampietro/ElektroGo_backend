package elektroGo.back.controller;

import elektroGo.back.model.RouteCalculation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/route")
public class RouteController {

    @PostMapping("/calculate")
    public String calculateRoute(@RequestParam int range, @RequestParam Double oriLat, @RequestParam Double oriLon, @RequestParam Double destLat, @RequestParam Double destLon) {
        RouteCalculation routeCalculation = new RouteCalculation(range, oriLat, oriLon, destLat, destLon);
        routeCalculation.execute();
        return routeCalculation.getResult();
    }

}

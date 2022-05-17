/**
 * @file RouteController.java
 * @author Adria Abad
 * @date 19/03/2022
 * @brief Implementacio de la classe RouteController.
 */

package elektroGo.back.controller;

import elektroGo.back.exceptions.DestinationNotReachable;
import elektroGo.back.model.Point;
import elektroGo.back.model.RouteCalculation;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


/**
 * @brief La classe RouteController és la classe que comunicarà front-end i back-end a l'hora de calcular rutes
 */
@RestController
@RequestMapping("/routes")
public class RouteController {

    /**
     * @brief Funcio amb metode 'GET' calcula la ruta entre un punt d'origen i un punt de desti
     * @param oriLat valor de la coordenada de latitud del punt d'origen
     * @param oriLon valor de la coordenada de longitud del punt d'origen
     * @param destLat valor de la coordenada de latitud del punt de desti
     * @param destLon valor de la coordenada de longitud del punt de desti
     * @param range autonomia del vehicle expressada en kilometres
     * @return Es retorna una llista de punts que formen la ruta entre l'origen i el desti
     */
    @GetMapping("/calculate")
    public ArrayList<Double> calculateRoute(@RequestParam Double oriLat, @RequestParam Double oriLon, @RequestParam Double destLat, @RequestParam Double destLon, @RequestParam int range) {
        RouteCalculation routeCalculation = new RouteCalculation(range, oriLat, oriLon, destLat, destLon);
        if (!routeCalculation.execute()) throw new DestinationNotReachable();
        else return routeCalculation.getResult();
    }

}

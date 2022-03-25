/**
 * @file RouteController.java
 * @author Adria Abad
 * @date 19/03/2022
 * @brief Implementacio de la classe RouteController.
 */

package elektroGo.back.controller;

import elektroGo.back.model.Point;
import elektroGo.back.model.RouteCalculation;

import org.springframework.web.bind.annotation.*;


/**
 * @brief La classe RouteController és la classe que comunicarà front-end i back-end a l'hora de calcular rutes
 */
@RestController
@RequestMapping("/route")
public class RouteController {

    /**
     * @brief Funcio amb metode 'GET' calcula la ruta entre un punt d'origen i un punt de desti
     * @param range autonomia del vehicle expressada en kilometres
     * @param oriLat valor de la coordenada de latitud del punt d'origen
     * @param oriLon valor de la coordenada de longitud del punt d'origen
     * @param destLat valor de la coordenada de latitud del punt de desti
     * @param destLon valor de la coordenada de longitud del punt de desti
     * @return Es retorna una llista de punts que formen la ruta entre l'origen i el desti
     */
    @GetMapping("/calculate")
    public Point[] calculateRoute(@RequestParam int range, @RequestParam Double oriLat, @RequestParam Double oriLon, @RequestParam Double destLat, @RequestParam Double destLon) {
        RouteCalculation routeCalculation = new RouteCalculation(range, oriLat, oriLon, destLat, destLon);
        routeCalculation.execute();
        return routeCalculation.getResult();
    }

}

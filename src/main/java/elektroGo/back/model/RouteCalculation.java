/**
 * @file RouteCalculation.java
 * @author Adria Abad
 * @date 13/03/2022
 * @brief Implementacio de la classe RouteCalculation.
 */

package elektroGo.back.model;

import elektroGo.back.data.finders.FinderChargingStations;
import elektroGo.back.data.gateways.GatewayChargingStations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
/**
 * @brief La classe RouteCalculation representa la logica del calcul d'una ruta
 */
public class RouteCalculation {

    /**
     * @brief Llista d'estacions de carrega
     */
    private ArrayList<Point> chargers;

    /**
     * @brief Valor de l'autonomia del vehicle en kilometres
     */
    private final int range; //in kilometers

    /**
     * @brief Punt d'origen de la ruta
     */
    private final Point origin;

    /**
     * @brief Punt de desti de la ruta
     */
    private final Point destination;

    /**
     * @brief Matriu de distancies entre l'origen, el desti i les estacions de carrega
     */
    private ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();

    /**
     * @brief Estacions de carrega que formen part de la ruta
     */
    private ArrayList<Point> definitive = new ArrayList<>();

    /**
     * @brief Distancia en kilometres de la ruta mes curta
     */
    private Integer minDistance;

    /**
     * @brief Instancia de la classe DistanceCalculator que calcula la matriu de distancies
     */
    private final DistanceCalculator distanceCalculator = new DistanceCalculator();

    /**
     * @brief Creadora de la classe RouteCalculation
     * @param range numero de kilometres maxims a fer sense parar
     * @param oriLat coordenada de latitud de l'origen
     * @param oriLon coordenada de longitud de l'origen
     * @param destLat coordenada de latitud del desti
     * @param destLon coordenada de longitud del desti
     * @return Retorna la instancia de la classe RouteCalculation que s'acaba de crear
     */
    public RouteCalculation(int range, Double oriLat, Double oriLon, Double destLat, Double destLon) {
        Point origin = new Point(oriLat, oriLon);
        Point destination = new Point(destLat, destLon);
        this.origin = origin;
        this.destination = destination;
        this.range = range * 1000;
        getSuperchargers();
    }

    /**
     * @brief Funcio que obte de la base de dades totes les estacions de carrega
     * @post L'atribut  \p chargers conte la llista de totes les estacions de carrega
     */
    private void getSuperchargers() {
        FinderChargingStations fCS = FinderChargingStations.getInstance();
        ArrayList<GatewayChargingStations> gCSs = new ArrayList<>();
        try {
            gCSs = fCS.findSuperchargers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Point> temporal = new ArrayList<>();
        for (GatewayChargingStations g : gCSs) {
            Point p = new Point(g.getLatitude().doubleValue(), g.getLongitude().doubleValue());
            temporal.add(p);
        }
        chargers = temporal;
    }

    /**
     * @brief Funcio que implementa el mecanisme de backtrack que permet escollir la ruta optima tenint en compte les parades a estacions de carrega
     * @param i Valor de l'index que s'utilitza com a origen en les crides recursives
     * @param distance Valor de la distancia que acumula la ruta actual
     * @param temporal Llista temporal d'estacions de carrega per les quals passa la ruta
     * @pre S'ha executat abans la funcio execute()
     * @post Quan totes les crides recursives han acabat, l'atribut \p definitive conte la llista, en ordre,
     * de carregadors pels quals s'ha de passar per poder fer la ruta entre l'origen i el desti
     */
    private void backtrack(int i, int distance, ArrayList<Point> temporal) {
        if (distance > minDistance) return;
        if (matrix.get(i).get(0) > range) {
            // Destination not reachable
            for (int j  = 1; j < matrix.size(); ++j) {
                int actualDist = matrix.get(i).get(j);
                if (j > i && actualDist < range) {
                    temporal.add(chargers.get(j - 1));
                    backtrack(j, distance + actualDist, temporal);
                    temporal.clear();
                }
            }
        }
        else {
            // Destination reachable
            if (distance < minDistance) {
                minDistance = distance;
                definitive = new ArrayList<>(temporal);
            }
        }
    }

    /**
     * @brief Funcio que calcula per quines estacions de carrega s'ha de passar per poder anar de l'origen al desti tenint
     * en compte l'autonomia del vehicle
     * @pre S'ha executat abans la funcio getSuperchargers()
     * @post L'atribut definitive conte la llista, en ordre, de carregadors pels quals s'ha de passar per poder fer la ruta entre l'origen i el desti
     */
    public boolean execute() {
        matrix = distanceCalculator.calculateRoadDistanceMatrix(origin, destination, chargers);
        minDistance = Integer.MAX_VALUE;
        ArrayList<Point> temporal = new ArrayList<>();
        backtrack(0, 0, temporal);
        if (definitive.size() == 0) {
            //Check if there is no route possible or the route requires no stops
            ArrayList<ArrayList<Integer>> aux;
            aux = distanceCalculator.calculateRoadDistanceMatrix(origin, destination, new ArrayList<>());
            if (aux.get(0).get(0) > range) return false;
        }
        return true;
    }

    /**
     * @brief Funcio per obtenir la llista de carregadors pels quals s'ha de passar per anar de l'origen al desti
     * @pre S'ha executat abans la funcio execute()
     * @return Retorna la llista de carregadors pels quals s'ha de passar per anar de l'origen al desti
     */
    public ArrayList<Double> getResult() {
        Point[] array = definitive.toArray(new Point[0]);
        ArrayList<Double> result = new ArrayList<>();
        for (Point p : array) {
            result.add(p.getLatitude());
            result.add(p.getLongitude());
        }
        return result;
    }

    /**
     * @brief Funcion que comproba si la API KEY per calcular les distancies es correcte
     * @return Retorna fals si la API KEY no es correcte, cert en cas contrari
     */
    public boolean checkAPIKey() {
        return !Objects.equals(distanceCalculator.getUrlKey(), "&key=");
    }

}

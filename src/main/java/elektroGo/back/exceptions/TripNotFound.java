/**
 * @file TripNotFound.java
 * @author Gerard Castell
 * @date 15/04/2022
 * @brief Implementació de l'excepcio TripNotFound
 */


package elektroGo.back.exceptions;

/**
 * @brief La classe TripNotFound implementa una RuntimeException
 */
public class TripNotFound extends RuntimeException {

    public TripNotFound(){
        super();
    }

    public TripNotFound(Integer id) {
        super("Trip with id " + id + " was not found");
    }
}

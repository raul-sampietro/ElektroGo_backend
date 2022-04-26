/**
 * @file TripAlreadyExists.java
 * @author Gerard Castell
 * @date 15/04/2022
 * @brief Implementació de l'excepcio TripAlreadyExists
 */

package elektroGo.back.exceptions;

/**
 * @brief La classe TripAlreadyExists implementa una RuntimeException
 */
public class TripAlreadyExists extends RuntimeException{

    public TripAlreadyExists() {

    }

    public TripAlreadyExists(Integer id) {
        super("Trip with id " + id + "already exists");
    }
}
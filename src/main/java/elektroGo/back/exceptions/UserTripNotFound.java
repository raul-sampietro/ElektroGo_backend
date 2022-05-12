/**
 * @file UserTripNotFound.java
 * @author Gerard Castell
 * @date 29/04/2022
 * @brief Implementació de l'excepcio UserTripNotFound
 */

package elektroGo.back.exceptions;

/**
 * @brief La classe UserTripNotFound implementa una RuntimeException
 */
public class UserTripNotFound extends RuntimeException{

    public UserTripNotFound() {

    }

    public UserTripNotFound(String username, Integer id) {
        super("User " + username + " is not in Trip " + id);
    }
}
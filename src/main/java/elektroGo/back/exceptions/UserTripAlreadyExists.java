/**
 * @file UserTripAlreadyExists.java
 * @author Gerard Castell
 * @date 29/04/2022
 * @brief Implementació de l'excepcio UserTripAlreadyExists
 */

package elektroGo.back.exceptions;

/**
 * @brief La classe UserTripAlreadyExists implementa una RuntimeException
 */
public class UserTripAlreadyExists extends RuntimeException{

    public UserTripAlreadyExists() {}

    public UserTripAlreadyExists(Integer id, String username) {
        super("UserTrip with user " + username + " and id: "+ id +" already exists");
    }
}

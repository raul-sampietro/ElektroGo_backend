/**
 * @file UserNotFound.java
 * @author Gerard Castell
 * @date 12/03/2023
 * @brief Implementació de l'excepcio UserNotFound
 */


package elektroGo.back.exceptions;

/**
 * @brief La classe UserNotFound implementa una RuntimeException
 */
public class UserNotFound extends RuntimeException {

    public UserNotFound(){
        super();
    }

    public UserNotFound(String userName) {
        super("User " + userName + " was not found");
    }
}

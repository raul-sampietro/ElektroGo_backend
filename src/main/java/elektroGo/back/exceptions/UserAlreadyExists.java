/**
 * @file UserAlreadyExists.java
 * @author Gerard Castell
 * @date 12/03/2023
 * @brief Implementació de l'excepcio UserAlreadyExists
 */

package elektroGo.back.exceptions;

/**
 * @brief La classe UserAlreadyExists implementa una RuntimeException
 */
public class UserAlreadyExists extends RuntimeException{

    public UserAlreadyExists() {

    }

    public UserAlreadyExists(String numberName) {
        super("User with userName " + numberName + "already exists");
    }
}
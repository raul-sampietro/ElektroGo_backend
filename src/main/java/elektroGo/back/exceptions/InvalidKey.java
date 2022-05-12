/**
 * @file DestinationNotReachable.java
 * @author Adria Abad
 * @date 24/03/2022
 * @brief Implementacio de l'excepcio DestinationNotReachable
 */

package elektroGo.back.exceptions;

/**
 * @brief La classe DestinationNotReachable implementa una RuntimeException
 */
public class InvalidKey extends RuntimeException {
    public InvalidKey() {
        super("Incorrect Key");
    }
}
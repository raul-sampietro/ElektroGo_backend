/**
 * @file DriverAlreadyExists.java
 * @author Gerard Castell
 * @date 14/03/2023
 * @brief Implementació de l'excepcio DriverAlreadyExists
 */
package elektroGo.back.exceptions;

/**
 * @brief La classe DriverAlreadyExists implementa una RuntimeException
 */
public class DriverAlreadyExists extends RuntimeException{

    public DriverAlreadyExists() {

    }

    public DriverAlreadyExists(String numberName) {
        super("Driver with userName " + numberName + "already exists");
    }
}
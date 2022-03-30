/**
 * @file VehicleAlreadyExists.java
 * @author Daniel Pulido
 * @date 12/03/2022
 * @brief Implementació de l'excepcio VehicleAlreadyExists
 */

package elektroGo.back.exceptions;

/**
 * @brief La classe VehicleAlreadyExists implementa una RuntimeException
 */
public class VehicleAlreadyExists extends RuntimeException{

    public VehicleAlreadyExists() {

    }

    public VehicleAlreadyExists(String numberPlate) {
        super("Vehicle with number plate " + numberPlate + "already exists");
    }
}

/**
 * @file DriverVehicleNotFound.java
 * @author Daniel Pulido
 * @date 17/03/2022
 * @brief Implementació de l'excepcio DriverVehicleNotFound
 */

package elektroGo.back.exceptions;

/**
 * @brief La classe DriverVehicleNotFound implementa una RuntimeException
 */
public class DriverVehicleNotFound extends RuntimeException{

    public DriverVehicleNotFound() {

    }

    public DriverVehicleNotFound(String driver, String numberPlate) {
        super("Was not found that driver "+driver + " has a vehicle with number plate " + numberPlate);
    }
}

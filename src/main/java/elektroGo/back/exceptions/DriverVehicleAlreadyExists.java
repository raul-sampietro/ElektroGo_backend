package elektroGo.back.exceptions;

/**
 * @brief La classe DriverVehicleAlreadyExists implementa una RuntimeException
 */
public class DriverVehicleAlreadyExists extends RuntimeException {
    public DriverVehicleAlreadyExists() {}

    public DriverVehicleAlreadyExists(String driver, String numberPlate) {
        super("Driver " + driver + " already has the vehicle with number plate " + numberPlate);
    }
}

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

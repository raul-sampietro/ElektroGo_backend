package elektroGo.back.exceptions;

/**
 * @brief La classe VehicleNotFound implementa una RuntimeException
 */
public class VehicleNotFound extends RuntimeException{
    public VehicleNotFound(){
        super();
    }
    public VehicleNotFound(String numberPlate) {
        super("Vehicle with number plate "+  numberPlate + " not found");
    }
}

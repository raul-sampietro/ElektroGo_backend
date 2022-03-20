package elektroGo.back.exceptions;

public class VehicleAlreadyExists extends RuntimeException{

    public VehicleAlreadyExists() {

    }

    public VehicleAlreadyExists(String numberPlate) {
        super("Vehicle with number plate " + numberPlate + "already exists");
    }
}

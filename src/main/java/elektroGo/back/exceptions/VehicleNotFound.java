package elektroGo.back.exceptions;

public class VehicleNotFound extends RuntimeException{
    public VehicleNotFound(){
        super();
    }
    public VehicleNotFound(String numberPlate) {
        super("Vehicle with number plate "+  numberPlate + " not found");
    }
}

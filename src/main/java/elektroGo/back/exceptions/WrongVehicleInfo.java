package elektroGo.back.exceptions;

public class WrongVehicleInfo  extends RuntimeException{
    public WrongVehicleInfo(){
        super();
    }
    public WrongVehicleInfo(String  numberPlate) {
        super("Vehicle with number plate "+  numberPlate + " already exists but with other information");
    }
}

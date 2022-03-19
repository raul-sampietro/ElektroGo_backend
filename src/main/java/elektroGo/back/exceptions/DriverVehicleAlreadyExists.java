package elektroGo.back.exceptions;

public class DriverVehicleAlreadyExists extends RuntimeException {
    public DriverVehicleAlreadyExists() {}

    public DriverVehicleAlreadyExists(String driver, String numberPlate) {
        super("Driver " + driver + "already has the vehicle with number plate " + numberPlate);
    }
}

package elektroGo.back.exceptions;

public class DriverAlreadyExists extends RuntimeException{

    public DriverAlreadyExists() {

    }

    public DriverAlreadyExists(String numberName) {
        super("Driver with userName " + numberName + "already exists");
    }
}
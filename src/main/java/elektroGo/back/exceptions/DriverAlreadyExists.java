package elektroGo.back.exceptions;

/**
 * @brief La classe DriverAlreadyExists implementa una RuntimeException
 */
public class DriverAlreadyExists extends RuntimeException{

    public DriverAlreadyExists() {

    }

    public DriverAlreadyExists(String numberName) {
        super("Driver with userName " + numberName + "already exists");
    }
}
package elektroGo.back.exceptions;

/**
 * @brief La classe DriverNotFound implementa una RuntimeException
 */
public class DriverNotFound extends RuntimeException {

    public DriverNotFound(){
        super();
    }

    public DriverNotFound(String driver) {
        super("Driver " + driver + " was not found");
    }
}

package elektroGo.back.exceptions;

public class DriverNotFound extends RuntimeException {

    public DriverNotFound(){
        super();
    }

    public DriverNotFound(String driver) {
        super("Driver " + driver + " was not found");
    }
}

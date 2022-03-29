package elektroGo.back.exceptions;

/**
 * @brief La classe DestinationNotReachable implementa una RuntimeException
 */
public class DestinationNotReachable extends RuntimeException {
    public DestinationNotReachable() {
        super("No route available from the origin to the destination given");
    }
}

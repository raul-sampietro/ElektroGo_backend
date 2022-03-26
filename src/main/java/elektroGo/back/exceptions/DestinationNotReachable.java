package elektroGo.back.exceptions;

public class DestinationNotReachable extends RuntimeException {
    public DestinationNotReachable() {
        super("No route available from the origin to the destination given");
    }
}

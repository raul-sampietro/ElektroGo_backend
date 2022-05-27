package elektroGo.back.exceptions;

public class TripIsFull extends RuntimeException {

    public TripIsFull() {
        super();
    }

    public TripIsFull(Integer id, Integer offeredSeats, Integer occupiedSeats) {
        super("Trip " + id + "is already full because offeredSeats = " + offeredSeats +" occupiedSeats = "+occupiedSeats);
    }
}

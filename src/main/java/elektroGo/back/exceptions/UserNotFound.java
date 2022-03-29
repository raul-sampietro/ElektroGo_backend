package elektroGo.back.exceptions;

/**
 * @brief La classe UserNotFound implementa una RuntimeException
 */
public class UserNotFound extends RuntimeException {

    public UserNotFound(){
        super();
    }

    public UserNotFound(String userName) {
        super("User " + userName + " was not found");
    }
}

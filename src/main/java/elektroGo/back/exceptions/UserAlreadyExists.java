package elektroGo.back.exceptions;

/**
 * @brief La classe UserAlreadyExists implementa una RuntimeException
 */
public class UserAlreadyExists extends RuntimeException{

    public UserAlreadyExists() {

    }

    public UserAlreadyExists(String numberName) {
        super("User with userName " + numberName + "already exists");
    }
}
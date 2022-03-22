package elektroGo.back.exceptions;

public class UserNotFound extends RuntimeException {

    public UserNotFound(){
        super();
    }

    public UserNotFound(String userName) {
        super("User " + userName + " was not found");
    }
}

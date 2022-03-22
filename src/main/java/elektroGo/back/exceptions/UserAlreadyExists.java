package elektroGo.back.exceptions;

public class UserAlreadyExists extends RuntimeException{

    public UserAlreadyExists() {

    }

    public UserAlreadyExists(String numberName) {
        super("User with userName " + numberName + "already exists");
    }
}
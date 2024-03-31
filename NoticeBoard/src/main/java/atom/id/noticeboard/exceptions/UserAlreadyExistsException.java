package atom.id.noticeboard.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User with that name already exists");
    }
}

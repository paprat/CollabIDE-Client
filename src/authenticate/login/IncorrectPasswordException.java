package authenticate.login;

public class IncorrectPasswordException extends Exception {
    IncorrectPasswordException(String errorMessage) {
        super(errorMessage);
    }
}
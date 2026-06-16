package modules.user.exception;


// datos no cumplen reglas

public class UserValidatorException extends RuntimeException {

    public UserValidatorException(String message) {
        super(message);
    }

}
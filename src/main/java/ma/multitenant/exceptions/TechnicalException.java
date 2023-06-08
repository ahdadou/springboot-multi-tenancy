package ma.multitenant.exceptions;

public class TechnicalException extends RuntimeException{

    public TechnicalException(String message) {
        super(message);
    }
}

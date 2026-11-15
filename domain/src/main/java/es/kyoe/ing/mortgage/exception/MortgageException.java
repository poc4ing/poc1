package es.kyoe.ing.mortgage.exception;

public class MortgageException extends RuntimeException {

    public static final String CODE = "1001";

    public MortgageException() {
        super();
    }

    public MortgageException(String message) {
        super(message);
    }

    public MortgageException(String message, Throwable cause) {
        super(message, cause);
    }
}


package pfe.mandomati.academicms.Exception;

public class ClassCreationException extends RuntimeException{
    public ClassCreationException(String message) {
        super(message);
    }

    public ClassCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}

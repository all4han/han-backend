package ex.han.backend.global.exception;

public class NickNameDuplicatedException extends RuntimeException{
    public NickNameDuplicatedException(String message) {
        super(message);
    }
}

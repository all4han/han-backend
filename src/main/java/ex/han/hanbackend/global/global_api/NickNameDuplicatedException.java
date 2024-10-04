package ex.han.hanbackend.global.global_api;

public class NickNameDuplicatedException extends RuntimeException{
    public NickNameDuplicatedException(String message) {
        super(message);
    }
}

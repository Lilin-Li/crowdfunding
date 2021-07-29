package inc.lilin.crowd.admin.core.exception;

public class LoginFailedException extends RuntimeException{
    public LoginFailedException(String message){
        super(message);
    }
}

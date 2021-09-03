package inc.lilin.crowd.admin.core.exception;

public class AccessForbiddenException extends RuntimeException{
    public AccessForbiddenException(String message){
        super(message);
    }
}

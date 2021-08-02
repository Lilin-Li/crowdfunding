package inc.lilin.crowd.admin.core.exception;

public class DeleteAdminFailedException extends RuntimeException{
    public DeleteAdminFailedException(String errorCodeAndMes) {
        super(errorCodeAndMes);
    }
}

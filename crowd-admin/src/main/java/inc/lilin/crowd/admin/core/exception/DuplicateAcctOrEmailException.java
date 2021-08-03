package inc.lilin.crowd.admin.core.exception;

public class DuplicateAcctOrEmailException  extends RuntimeException {
    public DuplicateAcctOrEmailException(String errorCodeAndMes) {
        super(errorCodeAndMes);
    }
}

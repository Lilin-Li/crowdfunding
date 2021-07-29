package inc.lilin.crowd.common.core.error_code;

public enum ExceptionMessageEnum {
    LOGIN_FAILED(10000,"帳密錯誤，請重新輸入"),
    ACCOUNT_IS_ALREADY_IN_USE(10001,"帳號已被使用");

    private final int errorCode;
    private final String errorMeg;

    private ExceptionMessageEnum(int errorCode,String message){
        this.errorCode = errorCode;
        this.errorMeg = message;
    }

    public int getErrorCode(){
        return errorCode;
    }

    public String getErrorMeg(){
        return errorMeg;
    }

    @Override
    public String toString() {
        return "ExceptionMessageEnum{" +
                "errorCode=" + errorCode +
                ", errorMeg='" + errorMeg + '\'' +
                '}';
    }
}

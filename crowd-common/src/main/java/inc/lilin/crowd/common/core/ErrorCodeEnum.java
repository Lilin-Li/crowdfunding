package inc.lilin.crowd.common.core;

public enum ErrorCodeEnum {
    LOGIN_FAILED(10000, "帳密錯誤，請重新輸入"),
    ACCOUNT_IS_ALREADY_IN_USE(10001, "帳號已被使用"),
    LOGIN_FAILED_ACCT_NOT_EXIST(10002, "帳號不存在"),
    LOGIN_FAILED_ACCT_NOT_UNIQUE(10003, "帳號不唯一"),
    LOGIN_FAILED_RESULT_NULL(10004, "查出結果為null"),
    LOGIN_PASSWORD_ERROR(10005, "密碼比對不吻合");

    private final int errorCode;
    private final String errorMeg;

    private ErrorCodeEnum(int errorCode, String message) {
        this.errorCode = errorCode;
        this.errorMeg = message;
    }

    public String getErrorCodeAndMes() {
        return errorCode + " " + errorMeg;
    }

    @Override
    public String toString() {
        return "ExceptionMessageEnum{" +
                "errorCode=" + errorCode +
                ", errorMeg='" + errorMeg + '\'' +
                '}';
    }
}

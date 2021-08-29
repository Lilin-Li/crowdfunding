package inc.lilin.crowd.entity.vo;

/**
 * 統一整個專案中Ajax請求返回的結果
 *
 */
public class ResultVO<T> {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    // 用來封裝目前請求處理的結果是成功還是失敗
    private String result;

    // 請求處理失敗時返回的錯誤訊息
    private String message;

    // 要返回的數據
    private T data;

    /**
     * 請求處理成功且不需要返回數據時使用的工具方法
     * @return
     */
    public static <Type> ResultVO<Type> successWithoutData() {
        return new ResultVO<Type>(SUCCESS, null, null);
    }

    /**
     * 請求處理成功且需要返回數據時使用的工具方法
     * @param data 要返回的數據
     * @return
     */
    public static <Type> ResultVO<Type> successWithData(Type data) {
        return new ResultVO<Type>(SUCCESS, null, data);
    }

    /**
     * 請求處理失敗后使用的工具方法
     * @param message 失敗的錯誤訊息
     * @return
     */
    public static <Type> ResultVO<Type> failed(String message) {
        return new ResultVO<Type>(FAILED, message, null);
    }

    public ResultVO() {

    }

    public ResultVO(String result, String message, T data) {
        super();
        this.result = result;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "RESTfulResult [result=" + result + ", message=" + message + ", data=" + data + "]";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

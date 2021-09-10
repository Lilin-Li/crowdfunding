package inc.lilin.crowd.common.core.constant;

public class CrowdConstant {

	public static final String MESSAGE_LOGIN_FAILED = "抱歉！賬號密碼錯誤！請重新輸入！";
	public static final String MESSAGE_LOGIN_ACCT_ALREADY_IN_USE = "抱歉！這個賬號已經被使用了！";
	public static final String MESSAGE_ACCESS_FORBIDEN = "請登錄以後再訪問！";
	public static final String MESSAGE_STRING_INVALIDATE = "字串不合法！請不要傳入空字串！";
	public static final String MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE = "系統錯誤：登錄賬號不唯一！";
	public static final String MESSAGE_ACCESS_DENIED = "抱歉！您不能訪問這個資源！";
	public static final String MESSAGE_CODE_NOT_EXISTS = "驗證碼已過期！請檢查手機號是否正確或重新發送！";
	public static final String MESSAGE_CODE_INVALID = "驗證碼不正確！";
	public static final String MESSAGE_HEADER_PIC_EMPTY = "未上傳大頭貼！";
	public static final String MESSAGE_HEADER_PIC_UPLOAD_FAILED = "大頭貼上傳失敗！";
	public static final Object MESSAGE_DETAIL_PIC_EMPTY = "伺服器接收到的詳細圖片大小為0";
	public static final Object MESSAGE_DETAIL_PIC_UPLOAD_FAILED =  "詳細圖片上傳失敗！";;

	public static final String GATEWAY_URL = "http://localhost:80";

	public static final String ATTR_NAME_EXCEPTION = "exception";
	public static final String ATTR_NAME_LOGIN_ADMIN = "loginAdmin";
	public static final String ATTR_NAME_LOGIN_MEMBER = "loginMember";
	public static final String ATTR_NAME_PAGE_INFO = "pageInfo";
	public static final String ATTR_NAME_MESSAGE = "message";
	public static final String ATTR_NAME_TEMPLE_PROJECT = "project"; //建立專案時的臨時session域
	
	public static final String REDIS_CODE_PREFIX = "REDIS_CODE_PREFIX_";


}

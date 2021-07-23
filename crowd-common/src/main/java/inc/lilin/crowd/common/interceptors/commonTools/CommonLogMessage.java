package inc.lilin.crowd.common.interceptors.commonTools;

import org.springframework.web.servlet.HandlerInterceptor;

public class CommonLogMessage {
    public static String commonMessage(HandlerInterceptor handlerInterceptor){
        return "進入攔截器 " + handlerInterceptor.getClass().getSimpleName() + " 執行 " +  Thread.currentThread() .getStackTrace()[2].getMethodName();
    }
}

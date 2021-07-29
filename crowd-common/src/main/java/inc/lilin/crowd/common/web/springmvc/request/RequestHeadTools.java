package inc.lilin.crowd.common.web.springmvc.request;


import javax.servlet.http.HttpServletRequest;

public class RequestHeadUtil {

    /**
     * 判斷目前請求是否為Ajax請求
     *
     * @param request 請求對像
     * @return true：目前請求是Ajax請求
     * false：目前請求不是Ajax請求
     */
    public static boolean isThis_a_JSON_Request(HttpServletRequest request) {

        String acceptHeader = request.getHeader("Accept");
        String xRequestHeader = request.getHeader("X-Requested-With");

        return (acceptHeader != null && acceptHeader.contains("application/json"))
                ||
                (xRequestHeader != null && xRequestHeader.equals("XMLHttpRequest"));
    }

}


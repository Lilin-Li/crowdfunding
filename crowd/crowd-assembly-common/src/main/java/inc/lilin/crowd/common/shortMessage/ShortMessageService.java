package inc.lilin.crowd.common.shortMessage;

import inc.lilin.crowd.entity.vo.ResultVO;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import java.util.HashMap;
import java.util.Map;

public class ShortMessageService {
    /**
     * 給遠端第三方簡訊介面發送請求把驗證碼發送到使用者手機上
     *
     * @param host     簡訊介面呼叫的URL地址
     * @param path     具體發送簡訊功能的地址
     * @param method   請求方式
     * @param phoneNum 接收簡訊的手機號
     * @param appCode  用來呼叫第三方簡訊API的AppCode
     * @param sign     簽名編號
     * @param skin     模板編號
     * @return 返回呼叫結果是否成功
     * 成功：返回驗證碼	 * 	失敗：返回失敗訊息
     * 狀態碼: 200 正常；400 URL無效；401 appCode錯誤； 403 次數用完； 500 API網管錯誤
     */
    public static ResultVO<String> sendCodeByShortMessage(
            String host,
            String path,
            String method,
            String phoneNum,
            String appCode,
            String sign,
            String skin) {

        Map<String, String> headers = new HashMap<String, String>();

        // 最後在header中的格式(中間是英文空格)為Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);

        // 封裝其他參數
        Map<String, String> querys = new HashMap<String, String>();

        // 產生驗證碼
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int random = (int) (Math.random() * 10);
            builder.append(random);
        }

        String code = builder.toString();

        // 要發送的驗證碼，也就是模板中會變化的部分
        querys.put("param", code);

        // 收簡訊的手機號
        querys.put("phone", phoneNum);

        // 簽名編號
        querys.put("sign", sign);

        // 模板編號
        querys.put("skin", skin);
        // JDK 1.8示例程式碼請在這裡下載： http://code.fegine.com/Tools.zip

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);

            StatusLine statusLine = response.getStatusLine();

            // 狀態碼: 200 正常；400 URL無效；401 appCode錯誤； 403 次數用完； 500 API網管錯誤
            int statusCode = statusLine.getStatusCode();

            String reasonPhrase = statusLine.getReasonPhrase();

            if (statusCode == 200) {

                // 操作成功，把產生的驗證碼返回
                return ResultVO.successWithData(code);
            }

            return ResultVO.failed(reasonPhrase);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.failed(e.getMessage());
        }

    }
}

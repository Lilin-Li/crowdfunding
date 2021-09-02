package inc.lilin.crowd.common.util;

import inc.lilin.crowd.common.base_facility.HttpUtils;
import inc.lilin.crowd.common.core.constant.CrowdConstant;
import inc.lilin.crowd.entity.vo.ResultVO;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class CrowdUtil {

	/**
	 * 給遠端第三方簡訊介面發送請求把驗證碼發送到使用者手機上
	 * @param host		簡訊介面呼叫的URL地址
	 * @param path		具體發送簡訊功能的地址
	 * @param method	請求方式
	 * @param phoneNum	接收簡訊的手機號
	 * @param appCode	用來呼叫第三方簡訊API的AppCode
	 * @param sign		簽名編號
	 * @param skin		模板編號
	 * @return 返回呼叫結果是否成功
	 * 	成功：返回驗證碼	 * 	失敗：返回失敗訊息
	 * 	狀態碼: 200 正常；400 URL無效；401 appCode錯誤； 403 次數用完； 500 API網管錯誤
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
		for(int i = 0; i < 4; i++) {
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

			if(statusCode == 200) {

				// 操作成功，把產生的驗證碼返回
				return ResultVO.successWithData(code);
			}

			return ResultVO.failed(reasonPhrase);

		} catch (Exception e) {
			e.printStackTrace();
			return ResultVO.failed(e.getMessage());
		}

	}

	/**
	 * 對明文字串進行MD5加密
	 * @param source 傳入的明文字串
	 * @return 加密結果
	 */
	public static String md5(String source) {

		// 1.判斷source是否有效
		if(source == null || source.length() == 0) {

			// 2.如果不是有效的字串拋出異常
			throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
		}

		try {
			// 3.獲取MessageDigest對像
			String algorithm = "md5";

			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

			// 4.獲取明文字串對應的位元組陣列
			byte[] input = source.getBytes();

			// 5.執行加密
			byte[] output = messageDigest.digest(input);

			// 6.建立BigInteger對像
			int signum = 1;
			BigInteger bigInteger = new BigInteger(signum, output);

			// 7.按照16進位制將bigInteger的值轉換為字串
			int radix = 16;
			String encoded = bigInteger.toString(radix).toUpperCase();

			return encoded;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 判斷目前請求是否為Ajax請求
	 * @param request 請求對像
	 * @return
	 * 		true：目前請求是Ajax請求
	 * 		false：目前請求不是Ajax請求
	 */
	public static boolean judgeRequestType(HttpServletRequest request) {

		// 1.獲取請求訊息頭
		String acceptHeader = request.getHeader("Accept");
		String xRequestHeader = request.getHeader("X-Requested-With");

		// 2.判斷
		return (acceptHeader != null && acceptHeader.contains("application/json"))
				||
				(xRequestHeader != null && xRequestHeader.equals("XMLHttpRequest"));
	}

}

package inc.lilin.crowd.common.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import inc.lilin.crowd.entity.vo.ResultVO;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class OssService {
    /**
     * 專門負責上傳檔案到 OSS 伺服器的工具方法
     *
     * @param endpoint        OSS 參數
     * @param accessKeyId     OSS 參數
     * @param accessKeySecret OSS 參數
     * @param inputStream     要上傳的檔案的輸入流
     * @param bucketName      OSS 參數
     * @param bucketDomain    OSS 參數
     * @param originalName    要上傳的檔案的原始檔名
     * @return 包含上傳結果以及上傳的檔案在 OSS 上的訪問路徑
     */
    public static ResultVO<String> uploadFileToOss(
            String endpoint,
            String accessKeyId,
            String accessKeySecret,
            InputStream inputStream,
            String bucketName,
            String bucketDomain,
            String originalName) {
        // 建立 OSSClient 實例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 產生上傳檔案的目錄
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 產生上傳檔案在 OSS 伺服器上儲存時的檔名
        // 原始檔名：beautfulgirl.jpg
        // 產生檔名：wer234234efwer235346457dfswet346235.jpg
        // 使用 UUID 產生檔案主體名稱
        String fileMainName = UUID.randomUUID().toString().replace("-", "");
        // 從原始檔名中獲取副檔名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));
        // 使用目錄、檔案主體名稱、副檔名稱拼接得到對像名稱
        String objectName = folderName + "/" + fileMainName + extensionName;

        try {
            // 呼叫 OSS 客戶端對象的方法上傳檔案並獲取響應結果數據
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName,
                    inputStream);
            // 從響應結果中獲取具體響應訊息
            ResponseMessage responseMessage = putObjectResult.getResponse();
            // 根據響應狀態碼判斷請求是否成功
            if (responseMessage == null) {
                // 拼接訪問剛剛上傳的檔案的路徑
                String ossFileAccessPath = bucketDomain + "/" + objectName;
                // 目前方法返回成功
                return ResultVO.successWithData(ossFileAccessPath);
            } else {
                // 獲取響應狀態碼
                int statusCode = responseMessage.getStatusCode();
                // 如果請求沒有成功，獲取錯誤訊息
                String errorMessage = responseMessage.getErrorResponseAsString();
                // 目前方法返回失敗
                return ResultVO.failed(" 當 前 響 應 狀 態 碼 =" + statusCode + " 錯 誤 消 息 = "+errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 目前方法返回失敗
            return ResultVO.failed(e.getMessage());
        } finally {
            if (ossClient != null) {
                // 關閉 OSSClient。
                ossClient.shutdown();
            }
        }
    }
}

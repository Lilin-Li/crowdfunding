package inc.lilin.crowd.project.web;

import inc.lilin.crowd.common.core.constant.CrowdConstant;
import inc.lilin.crowd.common.thirdparty_api.CrowdUtil;
import inc.lilin.crowd.entity.vo.ProjectVO;
import inc.lilin.crowd.entity.vo.ResultVO;
import inc.lilin.crowd.project.config.OSSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectHandler {

    @Autowired
    OSSProperties ossProperties;

    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(
            // 接收除了上傳圖片之外的其他普通數據
            ProjectVO projectVO,
            // 接收上傳的頭圖
            MultipartFile headerPicture,
            // 接收上傳的詳情圖片
            List<MultipartFile> detailPictureList,
            // 用來將收集了一部分數據的 ProjectVO 對像存入 Session 域
            HttpSession session,
            // 用來在目前操作失敗后返回上一個表單頁面時攜帶提示訊息
            ModelMap modelMap
    ) throws IOException {
        // 一、完成頭圖上傳
        // 1.獲取目前 headerPicture 對象是否為空
        boolean headerPictureIsEmpty = headerPicture.isEmpty();
        if (headerPictureIsEmpty) {
            // 2.如果沒有上傳頭圖則返回到表單頁面並顯示錯誤訊息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
        // 3.如果使用者確實上傳了有內容的檔案，則執行上傳
        ResultVO<String> uploadHeaderPicResultVO = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename());
        String result = uploadHeaderPicResultVO.getResult();
        // 4.判斷頭圖是否上傳成功
        if (ResultVO.SUCCESS.equals(result)) {
            // 5.如果成功則從返回的數據中獲取圖片訪問路徑
            String headerPicturePath = uploadHeaderPicResultVO.getData();
            // 6.存入 ProjectVO 對像中
            projectVO.setHeaderPicturePath(headerPicturePath);
        } else {
            // 7.如果上傳失敗則返回到表單頁面並顯示錯誤訊息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                    CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            return "project-launch";

        }

        // 二、上傳詳情圖片
        // 1.建立一個用來存放詳情圖片路徑的集合
        List<String> detailPicturePathList = new ArrayList<String>();
        // 2.檢查 detailPictureList 是否有效
        if (detailPictureList == null || detailPictureList.size() == 0) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                    CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
            return "project-launch";

        }

        // 3.遍歷 detailPictureList 集合
        for (MultipartFile detailPicture : detailPictureList) {
            // 4.目前 detailPicture 是否為空
            if (detailPicture.isEmpty()) {
                // 5.檢測到詳情圖片中單個檔案為空也是回去顯示錯誤訊息
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                        CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
                return "project-launch";

            }

            // 6.執行上傳
            ResultVO<String> detailUploadResultVO = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    detailPicture.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    detailPicture.getOriginalFilename());
            // 7.檢查上傳結果
            String detailUploadResult = detailUploadResultVO.getResult();
            if (ResultVO.SUCCESS.equals(detailUploadResult)) {
                String detailPicturePath = detailUploadResultVO.getData();
                // 8.收集剛剛上傳的圖片的訪問路徑
                detailPicturePathList.add(detailPicturePath);
            } else {
                // 9.如果上傳失敗則返回到表單頁面並顯示錯誤訊息
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                        CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
                return "project-launch";

            }

        }

        // 10.將存放了詳情圖片訪問路徑的集合存入 ProjectVO 中
        projectVO.setDetailPicturePathList(detailPicturePathList);
        // 三、後續操作
        // 1.將 ProjectVO 對像存入 Session 域
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);
        // 2.以完整的訪問路徑前往下一個收集回報資訊的頁面
        return "redirect:"+ CrowdConstant.GATEWAY_URL +"/project/return/info/page";
    }
}
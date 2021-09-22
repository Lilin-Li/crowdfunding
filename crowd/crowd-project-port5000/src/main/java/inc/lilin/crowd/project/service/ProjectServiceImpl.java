package inc.lilin.crowd.project.service;

import inc.lilin.crowd.database.*;
import inc.lilin.crowd.entity.po.MemberConfirmInfoPO;
import inc.lilin.crowd.entity.po.MemberLaunchInfoPO;
import inc.lilin.crowd.entity.po.ProjectPO;
import inc.lilin.crowd.entity.po.ReturnPO;
import inc.lilin.crowd.entity.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ReturnPOMapper returnPOMapper;

    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVO, Integer memberId) {

        // 一、儲存ProjectPO對像

        // 1.建立空的ProjectPO對像
        ProjectPO projectPO = new ProjectPO();

        // 2.把projectVO中的屬性複製到projectPO中
        BeanUtils.copyProperties(projectVO, projectPO);

        // 修復bug：把memberId設定到projectPO中
        projectPO.setMemberid(memberId);

        // 修復bug：產生建立時間存入
        String createdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(createdate);
        projectPO.setDeploydate(createdate);

        // 修復bug：status設定成0，表示即將開始
        projectPO.setStatus(0);
        projectPO.setSupporter(0);
        projectPO.setSupportmoney(0L);

        // 3.儲存projectPO
        // 爲了能夠獲取到projectPO儲存后的自增主鍵，需要到ProjectPOMapper.xml檔案中進行相關設定
        // <insert id="insertSelective" useGeneratedKeys="true" keyProperty="id" ……
        projectPOMapper.insertSelective(projectPO);

        // 4.從projectPO對像這裡獲取自增主鍵
        Integer projectId = projectPO.getId();

        // 二、儲存專案、分類的關聯關係資訊
        // 1.從ProjectVO對像中獲取typeIdList
        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationship(typeIdList, projectId);

        // 三、儲存專案、標籤的關聯關係資訊
        // List<Integer> tagIdList = projectVO.getTagIdList();
        // projectPOMapper.insertTagRelationship(tagIdList, projectId);

        // 四、儲存專案中詳情圖片路徑資訊
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertPathList(projectId, detailPicturePathList);

        // 五、儲存專案發起人資訊
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);

        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        // 六、儲存專案回報資訊
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();

        List<ReturnPO> returnPOList = new ArrayList<>();

        for (ReturnVO returnVO : returnVOList) {

            ReturnPO returnPO = new ReturnPO();

            BeanUtils.copyProperties(returnVO, returnPO);

            returnPOList.add(returnPO);
        }

        returnPOMapper.insertReturnPOBatch(returnPOList, projectId);

        // 七、儲存專案確認資訊
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {
        // 1.查詢得到 DetailProjectVO 對像
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);

        // 2.根據 status 確定 statusText
        Integer status = detailProjectVO.getStatus();
        switch (status) {
            case 0:
                detailProjectVO.setStatusText("審覈中");
                break;
            case 1:
                detailProjectVO.setStatusText("眾籌中");
                break;
            case 2:
                detailProjectVO.setStatusText("眾籌成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已關閉");
                break;
            default:
                break;
        }

        // 3.根據 deployeDate 計算 lastDay
        // 2020-10-15
        String deployDate = detailProjectVO.getDeployDate();
        // 獲取目前日期
        Date currentDay = new Date();
        // 把眾籌日期解析成 Date 型別
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date deployDay = format.parse(deployDate);
            // 獲取目前目前日期的時間戳
            long currentTimeStamp = currentDay.getTime();
            // 獲取眾籌日期的時間戳
            long deployTimeStamp = deployDay.getTime();
            // 兩個時間戳相減計算目前已經過去的時間
            long pastDays = (currentTimeStamp - deployTimeStamp) / 1000 / 60 / 60 / 24;
            // 獲取總的眾籌天數
            Integer totalDays = detailProjectVO.getDay();
            // 使用總的眾籌天數減去已經過去的天數得到剩餘天數
            Integer lastDay = (int) (totalDays - pastDays);
            detailProjectVO.setLastDay(lastDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detailProjectVO;
    }
}

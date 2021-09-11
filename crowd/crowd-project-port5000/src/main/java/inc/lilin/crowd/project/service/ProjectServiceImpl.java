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

		// 修復bug：status設定成0，表示即將開始
		projectPO.setStatus(0);

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
		List<Integer> tagIdList = projectVO.getTagIdList();
		projectPOMapper.insertTagRelationship(tagIdList, projectId);

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

}

package inc.lilin.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVO implements Serializable {

	private static final long serialVersionUID = 1L;
	// 分類id集合
	private List<Integer> typeIdList;

	// 標籤id集合
	private List<Integer> tagIdList;

	// 專案名稱
	private String projectName;

	// 專案描述
	private String projectDescription;

	// 計劃籌集的金額
	private Integer money;

	// 籌集資金的天數
	private Integer day;

	// 建立專案的日期
	private String createdate;

	// 頭圖的路徑
	private String headerPicturePath;

	// 詳情圖片的路徑
	private List<String> detailPicturePathList;

	// 發起人資訊
	private MemberLauchInfoVO memberLauchInfoVO;

	// 回報資訊集合
	private List<ReturnVO> returnVOList;

	// 發起人確認資訊
	private MemberConfirmInfoVO memberConfirmInfoVO;
}

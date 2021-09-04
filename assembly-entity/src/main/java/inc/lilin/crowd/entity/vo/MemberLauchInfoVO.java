package inc.lilin.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLauchInfoVO  implements Serializable {

	private static final long serialVersionUID = 1L;
	// 簡單介紹
	private String descriptionSimple;

	// 詳細介紹
	private String descriptionDetail;

	// 聯繫電話
	private String phoneNum;

	// 客服電話
	private String serviceNum;

}
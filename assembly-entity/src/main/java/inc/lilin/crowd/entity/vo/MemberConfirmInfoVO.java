package inc.lilin.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberConfirmInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	// 易付寶賬號
	private String paynum;

	// 法人身份證號
	private String cardnum;
}
package inc.lilin.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnVO implements Serializable {

	private static final long serialVersionUID = 1L;

	// 回報型別：0 - 實物回報， 1 虛擬物品回報
	private Integer type;

	// 支援金額
	private Integer supportmoney;

	// 回報內容介紹
	private String content;

	// 總回報數量，0為不限制
	private Integer count;

	// 是否限制單筆購買數量，0表示不限購，1表示限購
	private Integer signalpurchase;

	// 如果單筆限購，那麼具體的限購數量
	private Integer purchase;

	// 運費，「0」為包郵
	private Integer freight;

	// 是否開發票，0 - 不開發票， 1 - 開發票
	private Integer invoice;

	// 眾籌結束后返還回報物品天數
	private Integer returndate;

	// 說明圖片路徑
	private String describPicPath;

}
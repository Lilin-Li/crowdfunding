package inc.lilin.crowd.entity.po;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 
* 
* 請文件由代碼產生器(myBatisGenerator)產生，任何修改都會在下次產生代碼時被覆蓋。
* 
* @Data = Getter/Setter + ToString + Equals + HashCode
* @Builder = 「此ClassName.builder().id(1).name("Join").build(); 」
* 
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnPO implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer projectid;

    /**
     * 0 - 實物回報， 1 虛擬物品回報
     */
    private Integer type;

    /**
     * 支援金額
     */
    private Integer supportmoney;

    /**
     * 回報內容
     */
    private String content;

    /**
     * 回報產品限額，「0」為不限回報數量
     */
    private Integer count;

    /**
     * 是否設定單筆限購
     */
    private Integer signalpurchase;

    /**
     * 具體限購數量
     */
    private Integer purchase;

    /**
     * 運費，「0」為包郵
     */
    private Integer freight;

    /**
     * 0 - 不開發票， 1 - 開發票
     */
    private Integer invoice;

    /**
     * 專案結束后多少天向支持者發送回報
     */
    private Integer returndate;

    /**
     * 說明圖片路徑
     */
    private String describPicPath;
}
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
public class OrderPO implements Serializable {
    /**
     * 主鍵
     */
    private Integer id;

    /**
     * 訂單號
     */
    private String orderNum;

    /**
     * 支付寶流水號
     */
    private String payOrderNum;

    /**
     * 訂單金額
     */
    private Double orderAmount;

    /**
     * 是否開發票（0 不開，1 開）
     */
    private Integer invoice;

    /**
     * 發票抬頭
     */
    private String invoiceTitle;

    /**
     * 訂單備註
     */
    private String orderRemark;

    /**
     * 收貨地址 id
     */
    private String addressId;
}
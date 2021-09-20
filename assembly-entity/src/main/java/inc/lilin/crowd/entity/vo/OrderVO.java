package inc.lilin.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    // 主鍵
    private Integer id;
    // 訂單號
    private String orderNum;
    // 支付寶流水單號
    private String payOrderNum;
    // 訂單金額
    private Double orderAmount;
    // 是否開發票
    private Integer invoice;
    // 發票抬頭
    private String invoiceTitle;
    // 備註
    private String orderRemark;
    private Integer addressId;
    private OrderProjectVO orderProjectVO;

}
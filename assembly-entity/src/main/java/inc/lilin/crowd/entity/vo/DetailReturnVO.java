package inc.lilin.crowd.entity.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailReturnVO {
    // 回報資訊主鍵
    private Integer returnId;
    // 目前檔位需支援的金額
    private Integer supportMoney;
    // 單筆限購，取值為 0 時無限額，取值為 1 時有限額
    private Integer signalPurchase;
    // 具體限額數量
    private Integer purchase;
    // 目前該檔位支持者數量
    private Integer supproterCount;

    // 運費，取值為 0 時表示包郵
    private Integer freight;
    // 眾籌成功后多少天發貨
    private Integer returnDate;
    // 回報內容
    private String content;
}
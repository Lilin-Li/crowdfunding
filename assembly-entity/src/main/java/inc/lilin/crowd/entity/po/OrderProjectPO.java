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
public class OrderProjectPO implements Serializable {
    /**
     * 主鍵
     */
    private Integer id;

    /**
     * 專案名稱
     */
    private String projectName;

    /**
     * 發起人
     */
    private String launchName;

    /**
     * 回報內容
     */
    private String returnContent;

    /**
     * 回報數量
     */
    private Integer returnCount;

    /**
     * 支援單價
     */
    private Integer supportPrice;

    /**
     * 配送費用
     */
    private Integer freight;

    /**
     * 訂單表的主鍵
     */
    private Integer orderId;
}
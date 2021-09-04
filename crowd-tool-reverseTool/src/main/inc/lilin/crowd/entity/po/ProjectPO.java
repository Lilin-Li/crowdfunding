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
public class ProjectPO implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 專案名稱
     */
    private String projectName;

    /**
     * 專案描述
     */
    private String projectDescription;

    /**
     * 籌集金額
     */
    private Long money;

    /**
     * 籌集天數
     */
    private Integer day;

    /**
     * 0-即將開始，1-眾籌中，2-眾籌成功，3-眾籌失敗


     */
    private Integer status;

    /**
     * 專案發起時間
     */
    private String deploydate;

    /**
     * 已籌集到的金額
     */
    private Long supportmoney;

    /**
     * 支援人數
     */
    private Integer supporter;

    /**
     * 百分比完成度
     */
    private Integer completion;

    /**
     * 發起人的會員 id
     */
    private Integer memberid;

    /**
     * 專案建立時間
     */
    private String createdate;

    /**
     * 關注人數
     */
    private Integer follower;

    /**
     * 頭圖路徑
     */
    private String headerPicturePath;
}
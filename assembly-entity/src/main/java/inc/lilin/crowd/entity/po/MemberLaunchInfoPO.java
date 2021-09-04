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
public class MemberLaunchInfoPO implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 會員 id
     */
    private Integer memberid;

    /**
     * 簡單介紹
     */
    private String descriptionSimple;

    /**
     * 詳細介紹
     */
    private String descriptionDetail;

    /**
     * 聯繫電話
     */
    private String phoneNum;

    /**
     * 客服電話
     */
    private String serviceNum;
}
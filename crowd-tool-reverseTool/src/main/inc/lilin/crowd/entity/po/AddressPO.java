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
public class AddressPO implements Serializable {
    /**
     * 主鍵
     */
    private Integer id;

    /**
     * 收件人
     */
    private String receiveName;

    /**
     * 手機號
     */
    private String phoneNum;

    /**
     * 收貨地址
     */
    private String address;

    /**
     * 使用者 id
     */
    private Integer memberId;
}
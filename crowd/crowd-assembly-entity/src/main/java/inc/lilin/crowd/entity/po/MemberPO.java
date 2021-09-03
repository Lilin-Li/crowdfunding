package inc.lilin.crowd.entity.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class MemberPO implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String loginacct;

    /**
     * 
     */
    private String userpswd;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String email;

    /**
     * 實名認證狀態 0 - 未實名認證， 1 - 實名認證申請中， 2 - 已實名認證
     */
    private Integer authstatus;

    /**
     *  0 - 個人， 1 - 企業
     */
    private Integer usertype;

    /**
     * 
     */
    private String realname;

    /**
     * 
     */
    private String cardnum;

    /**
     * 0 - 企業， 1 - 個體， 2 - 個人， 3 - 政府
     */
    private Integer accttype;
}
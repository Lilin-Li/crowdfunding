package MBG;

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
public class AdminT implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String loginAcct;

    /**
     * 
     */
    private String userPswd;

    /**
     * 
     */
    private String userName;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private String createTime;
}
package inc.lilin.crowd.admin.database.mysql.mybatis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
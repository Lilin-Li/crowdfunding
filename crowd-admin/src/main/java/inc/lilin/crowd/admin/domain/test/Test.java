package inc.lilin.crowd.admin.domain.test;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Table描述：資料庫未在此table定義描述。
* 
* 請注意，請文件由代碼產生器(myBatisGenerator)產生
* 任何修改都會在下次產生代碼時，被覆蓋。
* 
* @Data = Getter/Setter + ToString + Equals + HashCode
* @Builder = 使你可以 「此ClassName.builder().id(1).name("Join").build(); 」new物件並設值。
* 
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Test implements Serializable {
    /**
     * 
     */
    private String userName;
}
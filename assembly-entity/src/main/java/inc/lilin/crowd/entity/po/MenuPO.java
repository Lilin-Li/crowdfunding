package inc.lilin.crowd.entity.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class MenuPO implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer pid;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String url;

    /**
     * 
     */
    private String icon;

    // 儲存子節點的集合，初始化是爲了避免空指針異常
    private List<MenuPO> children = new ArrayList<>();
    // 控制節點是否預設為打開裝，設定為 true 表示預設打開
    private Boolean open = true;
    // 控制點擊連結後，新視窗開啟的位置
    private String target = "_self"; //默認是本視窗


}
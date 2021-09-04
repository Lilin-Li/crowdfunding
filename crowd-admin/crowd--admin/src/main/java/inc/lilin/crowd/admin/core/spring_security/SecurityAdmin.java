package inc.lilin.crowd.admin.core.spring_security;

import inc.lilin.crowd.entity.po.AdminPO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * 考慮到 User 對像中僅僅包含賬號和密碼，爲了能夠獲取到原始的 Admin 對象，專門建立這個類對 User 類進行擴充套件
 *
 * @author Lenovo
 */
public class SecurityAdmin extends User {
    private static final long serialVersionUID = 1L;
    // 原始的 Admin 對象，包含 Admin 對象的全部屬性
    private AdminPO originalAdmin;

    public SecurityAdmin(
            AdminPO originalAdmin,  // 傳入原始的 Admin 對像
            List<GrantedAuthority> authorities) {   // 建立角色、許可權資訊的集合
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
        this.originalAdmin = originalAdmin;
    }

    public AdminPO getOriginalAdmin() {
        return originalAdmin;

    }
}
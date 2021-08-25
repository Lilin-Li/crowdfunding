package inc.lilin.crowd.admin.core.spring_security;

import inc.lilin.crowd.admin.core.service.AdminService;
import inc.lilin.crowd.admin.core.service.AuthService;
import inc.lilin.crowd.admin.core.service.RoleService;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminT;
import inc.lilin.crowd.admin.database.mysql.mybatis.RoleT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class myUserDetailsService  implements UserDetailsService {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.根據賬號名稱查詢 Admin 對像
        AdminT admin = adminService.getAdminByLoginAcct(username);
        // 2.獲取 adminId
        Integer adminId = admin.getId();
        // 3.根據 adminId 查詢角色資訊
        List<RoleT> assignedRoleList = roleService.getAssignedRole(adminId);
        // 4.根據 adminId 查詢許可權資訊
        List<String> authNameList = authService.getAssignedAuthNameByAdminId(adminId);
        // 5.建立集合對像用來儲存 GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 6.遍歷 assignedRoleList 存入角色資訊
        for (RoleT role : assignedRoleList) {
            // 注意：不要忘了加字首！
            String roleName = "ROLE_" + role.getName();
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
            authorities.add(simpleGrantedAuthority);
        }

        // 7.遍歷 authNameList 存入許可權資訊
        for (String authName : authNameList) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authName);
            authorities.add(simpleGrantedAuthority);
        }

        // 8.封裝 SecurityAdmin 對像
        SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);
        return securityAdmin;
    }
}
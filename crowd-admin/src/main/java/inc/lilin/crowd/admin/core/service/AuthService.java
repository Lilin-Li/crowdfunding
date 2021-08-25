package inc.lilin.crowd.admin.core.service;

import inc.lilin.crowd.admin.database.mysql.mybatis.AuthT;

import java.util.List;
import java.util.Map;

public interface AuthService {
    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    List<AuthT> getAll();

    void saveRoleAuthRelathinship(Map<String, List<Integer>> map);

    List<String> getAssignedAuthNameByAdminId(Integer adminId);
}

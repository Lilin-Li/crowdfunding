package inc.lilin.crowd.admin.core.service;

import inc.lilin.crowd.entity.po.AuthPO;

import java.util.List;
import java.util.Map;

public interface AuthService {
    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    List<AuthPO> getAll();

    void saveRoleAuthRelathinship(Map<String, List<Integer>> map);

    List<String> getAssignedAuthNameByAdminId(Integer adminId);
}

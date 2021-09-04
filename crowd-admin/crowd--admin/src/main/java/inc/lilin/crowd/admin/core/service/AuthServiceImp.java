package inc.lilin.crowd.admin.core.service;


import inc.lilin.crowd.database.AuthMapper;
import inc.lilin.crowd.entity.po.AuthPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImp implements AuthService {

    @Autowired
    AuthMapper authMapper;

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public List<AuthPO> getAll() {
        return authMapper.selectAll();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void saveRoleAuthRelathinship(Map<String, List<Integer>> map) {
        // 1.獲取 roleId 的值
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);
        // 2.刪除舊關聯關係數據
        authMapper.deleteOldRelationship(roleId);
        // 3.獲取 authIdList
        List<Integer> authIdList = map.get("authIdArray");
        // 4.判斷 authIdList 是否有效
        if (authIdList != null && authIdList.size() > 0) {
            authMapper.insertNewRelationship(roleId, authIdList);
        }
    }

    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthNameByAdminId(adminId);
    }
}

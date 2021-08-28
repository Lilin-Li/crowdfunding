package inc.lilin.crowd.admin.core.service;

import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.common.database.RolePO;

import java.util.List;

public interface RoleService {
    PageInfo<RolePO> getPageInfo(Integer pageNum, Integer pageSize, String keyword);

    void saveRole(RolePO role);

    void updateRole(RolePO role);

    void removeRole(List<Integer> roleIdList);

    List<RolePO> getAssignedRole(Integer adminId);

    List<RolePO> getUnAssignedRole(Integer adminId);
}

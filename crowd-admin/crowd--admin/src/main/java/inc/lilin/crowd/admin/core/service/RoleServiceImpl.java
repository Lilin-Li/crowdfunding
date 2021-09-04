package inc.lilin.crowd.admin.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.database.RoleMapper;
import inc.lilin.crowd.entity.po.RolePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleDAO;

    @Override
    public PageInfo<RolePO> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum,pageSize);
        List<RolePO> roleList = roleDAO.selectRoleByKeyword(keyword);

        return new PageInfo<>(roleList);
    }

    @Override
    public void saveRole(RolePO role) {
        roleDAO.insert(role);
    }

    @Override
    public void updateRole(RolePO role) {
        roleDAO.updateByPrimaryKey(role);
    }

    public void removeRole(List<Integer> roleIdList){
        roleDAO.deleteByIdList(roleIdList);

    };

    @Override
    public List<RolePO> getAssignedRole(Integer adminId) {
        return roleDAO.selectAssignedRole(adminId);

    }

    @Override
    public List<RolePO> getUnAssignedRole(Integer adminId) {
        return roleDAO.selectUnAssignedRole(adminId);

    }
}

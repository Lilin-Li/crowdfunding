package inc.lilin.crowd.admin.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.database.mysql.mybatis.RoleT;
import inc.lilin.crowd.admin.database.mysql.mybatis.RoleTMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleTMapper roleDAO;

    @Override
    public PageInfo<RoleT> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum,pageSize);
        List<RoleT> roleList = roleDAO.selectRoleByKeyword(keyword);

        return new PageInfo<>(roleList);
    }

    @Override
    public void saveRole(RoleT role) {
        roleDAO.insert(role);
    }

    @Override
    public void updateRole(RoleT role) {
        roleDAO.updateByPrimaryKey(role);
    }

    public void removeRole(List<Integer> roleIdList){
        roleDAO.deleteByIdList(roleIdList);

    };
}

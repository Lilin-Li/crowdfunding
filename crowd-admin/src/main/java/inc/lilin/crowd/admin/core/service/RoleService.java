package inc.lilin.crowd.admin.core.service;

import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.database.mysql.mybatis.RoleT;

import java.util.List;

public interface RoleService {
    PageInfo<RoleT> getPageInfo(Integer pageNum, Integer pageSize, String keyword);

    void saveRole(RoleT role);

    void updateRole(RoleT role);

    void removeRole(List<Integer> roleIdList);
}

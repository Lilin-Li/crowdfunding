package inc.lilin.crowd.admin.core.service;


import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.entity.po.AdminPO;

import java.util.List;

public interface AdminService {

    PageInfo<AdminPO> getAdmins(String keyword, Integer pageNum, Integer pageSize);

    void deleteAdmin(Integer adminId);


    void createAdmin(AdminPO admin);

    AdminPO getAdminByID(Integer adminId);

    void update(AdminPO admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);

    AdminPO getAdminByLoginAcct(String username);
}

package inc.lilin.crowd.admin.core.service;


import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminT;

public interface AdminService {

    PageInfo<AdminT> getAdmins(String keyword, Integer pageNum, Integer pageSize);

    void deleteAdmin(Integer adminId);


    void createAdmin(AdminT admin);

    AdminT getAdminByID(Integer adminId);

    void update(AdminT admin);
}

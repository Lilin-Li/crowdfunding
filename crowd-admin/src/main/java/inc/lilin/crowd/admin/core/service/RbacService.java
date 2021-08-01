package inc.lilin.crowd.admin.core.service;


import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminT;

public interface RbacService {

    PageInfo<AdminT> getUsers(String keyword, Integer pageNum, Integer pageSize);
}

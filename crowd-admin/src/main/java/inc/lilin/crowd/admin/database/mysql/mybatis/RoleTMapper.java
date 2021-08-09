package inc.lilin.crowd.admin.database.mysql.mybatis;

import java.util.List;

public interface RoleTMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleT record);

    RoleT selectByPrimaryKey(Integer id);

    List<RoleT> selectAll();

    int updateByPrimaryKey(RoleT record);
}
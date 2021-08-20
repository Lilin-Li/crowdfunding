package inc.lilin.crowd.admin.database.mysql.mybatis;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleTMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleT record);

    RoleT selectByPrimaryKey(Integer id);

    List<RoleT> selectAll();

    int updateByPrimaryKey(RoleT record);

    List<RoleT> selectRoleByKeyword(@Param("keyword") String keyword);

    void deleteByIdList(@Param("roleIdList")List<Integer> roleIdList);
}
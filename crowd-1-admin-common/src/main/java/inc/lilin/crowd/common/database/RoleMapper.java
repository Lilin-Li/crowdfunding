package inc.lilin.crowd.common.database;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RolePO record);

    RolePO selectByPrimaryKey(Integer id);

    List<RolePO> selectAll();

    int updateByPrimaryKey(RolePO record);

    List<RolePO> selectRoleByKeyword(@Param("keyword") String keyword);

    void deleteByIdList(@Param("roleIdList")List<Integer> roleIdList);

    List<RolePO> selectAssignedRole(Integer adminId);

    List<RolePO> selectUnAssignedRole(Integer adminId);
}
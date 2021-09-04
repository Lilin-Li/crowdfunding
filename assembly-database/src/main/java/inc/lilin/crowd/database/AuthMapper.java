package inc.lilin.crowd.database;

import inc.lilin.crowd.entity.po.AuthPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface AuthMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthPO record);

    AuthPO selectByPrimaryKey(Integer id);

    List<AuthPO> selectAll();

    int updateByPrimaryKey(AuthPO record);

    List<Integer> selectAssignedAuthIdByRoleId(Integer roleId);

    void deleteOldRelationship(Integer roleId);

    void insertNewRelationship(@Param("roleId") Integer roleId,@Param("authIdList") List<Integer> authIdList);

    List<String> selectAssignedAuthNameByAdminId(Integer adminId);
}
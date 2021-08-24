package inc.lilin.crowd.admin.database.mysql.mybatis;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface AuthTMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthT record);

    AuthT selectByPrimaryKey(Integer id);

    List<AuthT> selectAll();

    int updateByPrimaryKey(AuthT record);

    List<Integer> selectAssignedAuthIdByRoleId(Integer roleId);

    void deleteOldRelationship(Integer roleId);

    void insertNewRelationship(@Param("roleId") Integer roleId,@Param("authIdList") List<Integer> authIdList);
}
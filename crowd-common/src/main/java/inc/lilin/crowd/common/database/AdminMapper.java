package inc.lilin.crowd.common.database;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminPO record);

    AdminPO selectByPrimaryKey(Integer id);

    List<AdminPO> selectAll();

    int updateByPrimaryKey(AdminPO record);

    List<AdminPO> selectByAcct(@Param("acct") String loginAcct);

    List<AdminPO> selectAdminByKeyword(String keyword);

    void deleteOLdRelationship(Integer adminId);

    void insertNewRelationship(@Param("adminId")Integer adminId, @Param("roleIdList")List<Integer> roleIdList);
}
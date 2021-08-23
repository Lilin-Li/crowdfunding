package inc.lilin.crowd.admin.database.mysql.mybatis;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminTMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminT record);

    AdminT selectByPrimaryKey(Integer id);

    List<AdminT> selectAll();

    int updateByPrimaryKey(AdminT record);

    List<AdminT> selectByAcct(@Param("acct") String loginAcct);

    List<AdminT> selectAdminByKeyword(String keyword);

    void deleteOLdRelationship(Integer adminId);

    void insertNewRelationship(@Param("adminId")Integer adminId, @Param("roleIdList")List<Integer> roleIdList);
}
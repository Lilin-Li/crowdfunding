package inc.lilin.crowd.database;

import inc.lilin.crowd.entity.po.ReturnPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface ReturnPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ReturnPO record);

    ReturnPO selectByPrimaryKey(Integer id);

    List<ReturnPO> selectAll();

    int updateByPrimaryKey(ReturnPO record);

    void insertReturnPOBatch(@Param("returnPOList") List<ReturnPO> returnPOList, @Param("projectId") Integer projectId);
}
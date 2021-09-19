package inc.lilin.crowd.database;

import inc.lilin.crowd.entity.po.OrderProjectPO;
import inc.lilin.crowd.entity.vo.OrderProjectVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface OrderProjectPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderProjectPO record);

    OrderProjectPO selectByPrimaryKey(Integer id);

    List<OrderProjectPO> selectAll();

    int updateByPrimaryKey(OrderProjectPO record);

    OrderProjectVO selectOrderProjectVO(@Param("projectId") Integer projectId, @Param("returnId") Integer returnId);
}
package inc.lilin.crowd.database;

import inc.lilin.crowd.entity.po.OrderPO;
import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface OrderPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderPO record);

    OrderPO selectByPrimaryKey(Integer id);

    List<OrderPO> selectAll();

    int updateByPrimaryKey(OrderPO record);
}
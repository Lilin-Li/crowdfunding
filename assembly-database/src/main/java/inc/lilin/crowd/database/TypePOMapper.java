package inc.lilin.crowd.database;

import inc.lilin.crowd.entity.po.TypePO;
import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface TypePOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TypePO record);

    TypePO selectByPrimaryKey(Integer id);

    List<TypePO> selectAll();

    int updateByPrimaryKey(TypePO record);
}
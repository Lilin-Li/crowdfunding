package inc.lilin.crowd.common.database;

import inc.lilin.crowd.common.database.Roletpo;
import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface RoletpoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Roletpo record);

    Roletpo selectByPrimaryKey(Integer id);

    List<Roletpo> selectAll();

    int updateByPrimaryKey(Roletpo record);
}
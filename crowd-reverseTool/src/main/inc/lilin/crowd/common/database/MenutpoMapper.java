package inc.lilin.crowd.common.database;

import inc.lilin.crowd.common.database.Menutpo;
import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface MenutpoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Menutpo record);

    Menutpo selectByPrimaryKey(Integer id);

    List<Menutpo> selectAll();

    int updateByPrimaryKey(Menutpo record);
}
package inc.lilin.crowd.database;

import inc.lilin.crowd.entity.po.TagPO;
import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface TagPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TagPO record);

    TagPO selectByPrimaryKey(Integer id);

    List<TagPO> selectAll();

    int updateByPrimaryKey(TagPO record);
}
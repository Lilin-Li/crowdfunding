package inc.lilin.crowd.database;

import inc.lilin.crowd.entity.po.ProjectPO;
import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface ProjectPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPO record);

    ProjectPO selectByPrimaryKey(Integer id);

    List<ProjectPO> selectAll();

    int updateByPrimaryKey(ProjectPO record);
}
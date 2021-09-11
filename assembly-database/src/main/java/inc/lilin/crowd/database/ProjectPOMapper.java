package inc.lilin.crowd.database;

import inc.lilin.crowd.entity.po.ProjectPO;
import inc.lilin.crowd.entity.vo.PortalTypeVO;
import org.apache.ibatis.annotations.Param;

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

    void insertSelective(ProjectPO projectPO);

    void insertTypeRelationship(@Param("typeIdList") List<Integer> typeIdList, @Param("projectId")Integer projectId);

    void insertTagRelationship(@Param("tagIdList") List<Integer> tagIdList, @Param("projectId")Integer projectId);

    List<PortalTypeVO> selectPortalTypeVOList();
}
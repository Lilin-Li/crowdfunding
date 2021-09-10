package inc.lilin.crowd.database;

import inc.lilin.crowd.entity.po.ProjectItemPicPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface ProjectItemPicPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectItemPicPO record);

    ProjectItemPicPO selectByPrimaryKey(Integer id);

    List<ProjectItemPicPO> selectAll();

    int updateByPrimaryKey(ProjectItemPicPO record);

    void insertPathList(@Param("projectId") Integer projectId, @Param("detailPicturePathList")List<String> detailPicturePathList);

}
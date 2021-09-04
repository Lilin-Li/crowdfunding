package inc.lilin.crowd.database;

import inc.lilin.crowd.entity.po.MemberLaunchInfoPO;
import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface MemberLaunchInfoPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberLaunchInfoPO record);

    MemberLaunchInfoPO selectByPrimaryKey(Integer id);

    List<MemberLaunchInfoPO> selectAll();

    int updateByPrimaryKey(MemberLaunchInfoPO record);
}
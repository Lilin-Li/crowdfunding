package inc.lilin.crowd.common.database;

import inc.lilin.crowd.common.database.Innerroleauthpo;
import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface InnerroleauthpoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Innerroleauthpo record);

    Innerroleauthpo selectByPrimaryKey(Integer id);

    List<Innerroleauthpo> selectAll();

    int updateByPrimaryKey(Innerroleauthpo record);
}
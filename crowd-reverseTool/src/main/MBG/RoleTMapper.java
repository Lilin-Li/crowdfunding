package MBG;

import MBG.RoleT;
import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface RoleTMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleT record);

    RoleT selectByPrimaryKey(Integer id);

    List<RoleT> selectAll();

    int updateByPrimaryKey(RoleT record);
}
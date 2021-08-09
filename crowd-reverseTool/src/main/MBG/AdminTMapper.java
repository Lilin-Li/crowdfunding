package MBG;

import MBG.AdminT;
import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface AdminTMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminT record);

    AdminT selectByPrimaryKey(Integer id);

    List<AdminT> selectAll();

    int updateByPrimaryKey(AdminT record);
}
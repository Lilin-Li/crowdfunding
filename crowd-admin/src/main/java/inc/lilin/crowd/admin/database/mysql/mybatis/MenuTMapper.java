package inc.lilin.crowd.admin.database.mysql.mybatis;

import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface MenuTMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MenuT record);

    MenuT selectByPrimaryKey(Integer id);

    List<MenuT> selectAll();

    int updateByPrimaryKey(MenuT record);
}
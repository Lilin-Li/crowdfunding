package inc.lilin.crowd.database;

import inc.lilin.crowd.entity.po.MenuPO;

import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface MenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MenuPO record);

    MenuPO selectByPrimaryKey(Integer id);

    List<MenuPO> selectAll();

    int updateByPrimaryKey(MenuPO record);

    void updateByPrimaryKeySelective(MenuPO menu);
}
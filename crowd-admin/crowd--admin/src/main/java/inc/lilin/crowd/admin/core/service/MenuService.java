package inc.lilin.crowd.admin.core.service;


import inc.lilin.crowd.entity.po.MenuPO;

public interface MenuService {
    MenuPO getTree();

    void saveMenu(MenuPO menu);

    void updateMenu(MenuPO menu);

    void removeMenu(Integer id);
}

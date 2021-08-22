package inc.lilin.crowd.admin.core.service;

import inc.lilin.crowd.admin.database.mysql.mybatis.MenuT;

public interface MenuService {
    MenuT getTree();

    void saveMenu(MenuT menu);

    void updateMenu(MenuT menu);

    void removeMenu(Integer id);
}

package inc.lilin.crowd.admin.core.service;

import inc.lilin.crowd.admin.database.mysql.mybatis.MenuT;
import inc.lilin.crowd.admin.database.mysql.mybatis.MenuTMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImp implements MenuService {
    @Autowired
    MenuTMapper menuDAO;

    @Override
    public MenuT getTree() {
        // 1.查詢全部的 Menu 對像
        List<MenuT> menuList = menuDAO.selectAll();
        // 2.聲明一個變數用來儲存找到的根節點
        MenuT root = null;
        // 3.建立 Map 對像用來儲存 id 和 Menu 對象的對應關係便於查詢父節點
        Map<Integer, MenuT> menuMap = new HashMap<>();
        // 4.遍歷 menuList 填充 menuMap
        for (MenuT menu : menuList) {
            Integer id = menu.getId();
            menuMap.put(id, menu);
        }

        // 5.再次遍歷 menuList 查詢根節點、組裝父子節點
        for (MenuT menu : menuList) {
            // 6.獲取目前 menu 對象的 pid 屬性值
            Integer pid = menu.getPid();
            // 7.如果 pid 為 null，判定為根節點
            if (pid == null) {
                root = menu;
                // 8.如果目前節點是根節點，那麼肯定沒有父節點，不必繼續執行
                continue;
            }
            // 9.如果 pid 不為 null，說明目前節點有父節點，那麼可以根據 pid 到 menuMap 中查詢對應的 Menu 對像
            MenuT father = menuMap.get(pid);
            // 10.將目前節點存入父節點的 children 集合
            father.getChildren().add(menu);
        }
        // 11.經過上面的運算，根節點包含了整個樹形結構，返回根節點就是返回整個樹
        return root;
    }
}

package inc.lilin.crowd.admin.web.springmvc;

import inc.lilin.crowd.admin.core.service.MenuService;
import inc.lilin.crowd.admin.database.mysql.mybatis.MenuT;
import inc.lilin.crowd.common.web.springmvc.responseTools.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MenuController {

    @Autowired
    MenuService menuService;

    @ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
    public ResultVO<MenuT> getWholeTreeNew() {
        MenuT treeRoot = menuService.getTree();
        return ResultVO.successWithData(treeRoot);
    }

    @ResponseBody
    @RequestMapping("/menu/save.json")
    public ResultVO<String> saveMenu(MenuT menu) {
        menuService.saveMenu(menu);
        return ResultVO.successWithoutData();
    }

    @ResponseBody
    @PostMapping("/menu/update.json")
    public ResultVO<String> updateMenu(MenuT menu) {
        menuService.updateMenu(menu);
        return ResultVO.successWithoutData();

    }

    @ResponseBody
    @RequestMapping("/menu/remove.json")
    public ResultVO<String> removeMenu(@RequestParam("id") Integer id) {
        menuService.removeMenu(id);
        return ResultVO.successWithoutData();

    }
}

package inc.lilin.crowd.admin.web;

import inc.lilin.crowd.admin.core.service.MenuService;
import inc.lilin.crowd.common.web.vo.ResultVO;
import inc.lilin.crowd.entity.po.MenuPO;
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
    public ResultVO<MenuPO> getWholeTreeNew() {
        MenuPO treeRoot = menuService.getTree();
        return ResultVO.successWithData(treeRoot);
    }

    @ResponseBody
    @RequestMapping("/menu/save.json")
    public ResultVO<String> saveMenu(MenuPO menu) {
        menuService.saveMenu(menu);
        return ResultVO.successWithoutData();
    }

    @ResponseBody
    @PostMapping("/menu/update.json")
    public ResultVO<String> updateMenu(MenuPO menu) {
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

package inc.lilin.crowd.admin.web.springmvc;

import inc.lilin.crowd.admin.core.service.MenuService;
import inc.lilin.crowd.admin.database.mysql.mybatis.MenuT;
import inc.lilin.crowd.common.web.springmvc.responseTools.RestResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MenuController {

    @Autowired
    MenuService menuService;

    @ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
    public RestResultDTO<MenuT> getWholeTreeNew() {
        MenuT treeRoot = menuService.getTree();
        return RestResultDTO.successWithData(treeRoot);
    }
}

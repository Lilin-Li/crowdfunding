package inc.lilin.crowd.admin.web.springmvc;

import inc.lilin.crowd.admin.core.service.RoleService;
import inc.lilin.crowd.admin.database.mysql.mybatis.RoleT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AssignController {

    @Autowired
    RoleService roleService;

    public String toAssignRolePage(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap) {
        // 1.查詢已分配角色
        List<RoleT> assignedRoleList = roleService.getAssignedRole(adminId);
        // 2.查詢未分配角色
        List<RoleT> unAssignedRoleList = roleService.getUnAssignedRole(adminId);
        // 3.存入模型（本質上其實是：request.setAttribute("attrName",attrValue);
        modelMap.addAttribute("assignedRoleList", assignedRoleList);
        modelMap.addAttribute("unAssignedRoleList", unAssignedRoleList);
        return "assignRole";

    }
}
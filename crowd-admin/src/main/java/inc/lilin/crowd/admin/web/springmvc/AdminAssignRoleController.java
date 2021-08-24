package inc.lilin.crowd.admin.web.springmvc;

import inc.lilin.crowd.admin.core.service.AdminService;
import inc.lilin.crowd.admin.core.service.RoleService;
import inc.lilin.crowd.admin.database.mysql.mybatis.RoleT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminAssignRoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    AdminService adminService;

    @GetMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(
            @RequestParam("adminId") Integer adminId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword,
            ModelMap modelMap) {
        // 1.查詢已分配角色
        List<RoleT> assignedRoleList = roleService.getAssignedRole(adminId);
        // 2.查詢未分配角色
        List<RoleT> unAssignedRoleList = roleService.getUnAssignedRole(adminId);
        modelMap.addAttribute("assignedRoleList", assignedRoleList);
        modelMap.addAttribute("unAssignedRoleList", unAssignedRoleList);
        return "assignRole";
    }

    @RequestMapping("/assign/do/role/assign.html")
    public String saveAdminRoleRelationship(
            @RequestParam("adminId") Integer adminId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword,
            // 我們允許使用者在頁面上取消所有已分配角色再提交表單，所以可以不提供roleIdList 請求參數
            // 設定 required=false 表示這個請求參數不是必須的
        @RequestParam(value = "roleIdList", required = false) List<Integer>roleIdList
    ) {
        adminService.saveAdminRoleRelationship(adminId, roleIdList);
        return "redirect:/users?pageNum=" + pageNum + "&keyword=" + keyword;
    }
}
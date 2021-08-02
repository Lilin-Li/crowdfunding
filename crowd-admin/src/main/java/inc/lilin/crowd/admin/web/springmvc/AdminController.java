package inc.lilin.crowd.admin.web.springmvc;

import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.core.exception.DeleteAdminFailedException;
import inc.lilin.crowd.admin.core.service.RbacService;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminT;
import inc.lilin.crowd.common.core.ErrorCodeEnum;
import inc.lilin.crowd.common.core.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    RbacService rbacService;

    @GetMapping("admin/logout")
    public String gusetLogin(HttpSession session) throws Exception {
        session.invalidate();
        return "index";
    }

    @GetMapping("/users")
    public String getUsers(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize, Map<String, Object> map,
                           @RequestParam(value = "exceptionMsg", defaultValue = "") String exceptionMsg) throws Exception {

        PageInfo<AdminT> pageInfo = rbacService.getUsers(keyword, pageNum, pageSize);
        map.put("pageInfo", pageInfo);
        return "user";
    }

    @GetMapping({"/user/delete/{adminId}/{pageNum}/{keyword}", "/user/delete/{adminId}/{pageNum}/"})
    public String deleteUser(@PathVariable("adminId") Integer adminId,
                         @PathVariable(value = "pageNum") Integer pageNum,
                         @PathVariable(value = "keyword", required = false) String keyword,
                         HttpSession session) throws Exception {

        // 若delete的帳號是目前登入帳號 => 拋例外
        Integer sessionAdminId = ((AdminT) session.getAttribute(SystemConstant.SESSION_LOGIN_ADMIN)).getId();
        if (adminId.equals(sessionAdminId)) {
            throw new DeleteAdminFailedException(ErrorCodeEnum.DELETA_ADMIN_FAILED.getErrorCodeAndMes());
        };

        rbacService.deleteUser(adminId);
        if(keyword == null){
            keyword = "";
        }
        return "redirect:/users?pageNum=" + pageNum + "&keyword=" + keyword;
    }
    @GetMapping("/admin/create")
    public String createUser() throws Exception {

        return "";
    }
}
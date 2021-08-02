package inc.lilin.crowd.admin.web.springmvc;

import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.core.service.RbacService;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminT;
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
                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize, Map<String, Object> map) throws Exception {

        PageInfo<AdminT> pageInfo = rbacService.getUsers(keyword, pageNum, pageSize);
        map.put("pageInfo", pageInfo);
        return "user";
    }

    @GetMapping("/user/delete/{adminId}/{pageNum}/{keyword}")
    public String remove(@PathVariable("adminId") Integer adminId,
                         @PathVariable(value = "pageNum") Integer pageNum,
                         @PathVariable(value = "keyword") String keyword) {
        rbacService.deleteUser(adminId);
        keyword = keyword == null ? "":keyword;
        return "redirect:/users?pageNum="+pageNum+"&keyword="+keyword;
    }
}
package inc.lilin.crowd.admin.web.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class AdminController {
    @GetMapping("admin/logout")
    public String gusetLogin(HttpSession session) throws Exception {
        session.invalidate();
        return "index";
    }

    @GetMapping("/user")
    public String getUsers(HttpSession session) throws Exception {
        return "user";
    }
}
package inc.lilin.crowd.admin.web.springmvc;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class AdminController {
    @PostMapping("admin/logout")
    public String gusetLogin(HttpSession session) throws Exception {
        session.invalidate();
        return "index";
    }
}
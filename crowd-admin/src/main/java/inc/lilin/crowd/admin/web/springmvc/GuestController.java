package inc.lilin.crowd.admin.web.springmvc;

import inc.lilin.crowd.admin.core.service.LoginService;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminT;
import inc.lilin.crowd.common.core.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class GuestController {

    @Autowired
    LoginService loginService;

    @PostMapping("/guestLogin")
    public String gusetLogin(@RequestParam("loginAcct") String loginAcct,
                             @RequestParam("userPswd") String userPswd,
                             HttpSession session) throws Exception {
        AdminT admin = loginService.getAdminByLoginAcct(loginAcct, userPswd);

        session.setAttribute(SystemConstant.SESSION_LOGIN_ADMIN, admin);
        return "index";
        // return "redirect:/admin-main";
    }
}

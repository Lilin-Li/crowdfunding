package inc.lilin.crowd.admin.web;

import inc.lilin.crowd.admin.core.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GuestController {

    @Autowired
    LoginService loginService;

    /*
      改用SpringSecurity
     */
    // @PostMapping("/guestLogin")
    // public String gusetLogin(@RequestParam("loginAcct") String loginAcct,
    //                          @RequestParam("userPswd") String userPswd,
    //                          HttpSession session) throws Exception {
    //     AdminT admin = loginService.getAdminByLoginAcct(loginAcct, userPswd);
    //
    //     session.setAttribute(SystemConstant.SESSION_LOGIN_ADMIN, admin);
    //     return "redirect:/admin-main";
    // }
}

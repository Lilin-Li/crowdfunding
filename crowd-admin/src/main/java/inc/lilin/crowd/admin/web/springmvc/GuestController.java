package inc.lilin.crowd.admin.web.springmvc;

import inc.lilin.crowd.admin.core.exception.LoginFailedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class GuestController {
    @RequestMapping("/gusetLogin")
    public String gusetLogin(Map<String,Object> map) throws Exception{
        throw new LoginFailedException("例外abc");
        // return "exception";
    }
}
